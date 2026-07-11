#!/usr/bin/env bash
set -euo pipefail

NAMESPACE="${VAULT_NAMESPACE:-vault}"
POD="${VAULT_POD:-vault-0}"
ADDRESS="${VAULT_ADDR:-http://127.0.0.1:8200}"
OUTPUT_DIR="${VAULT_INIT_OUTPUT_DIR:-./vault-secrets}"
TIMESTAMP="$(date +%Y%m%d-%H%M%S)"
OUTPUT_FILE="${VAULT_INIT_OUTPUT_FILE:-${OUTPUT_DIR}/vault-init-${TIMESTAMP}.json}"

require_cmd() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "Error: required command '$1' not found" >&2
    exit 1
  fi
}

require_cmd kubectl

mkdir -p "${OUTPUT_DIR}"

leader_api_addr="http://${POD}.${NAMESPACE}-internal:8200"
pod_init_file="/home/vault/vault-init-output.json"

mapfile -t vault_pods < <(
  kubectl get pods -n "${NAMESPACE}" -o jsonpath='{range .items[*]}{.metadata.name}{"\n"}{end}' \
  | grep -E '^vault-[0-9]+$' \
  | sort -V
)

if [[ ${#vault_pods[@]} -eq 0 ]]; then
  echo "Error: no vault statefulset pods found in namespace ${NAMESPACE}" >&2
  exit 1
fi

status_text="$(kubectl exec -n "${NAMESPACE}" "${POD}" -- vault status -address="${ADDRESS}" 2>/dev/null || true)"
initialized="$(printf '%s\n' "${status_text}" | awk -F' +' '/^Initialized/{print tolower($2)}')"

if [[ "${initialized}" == "true" ]]; then
  echo "Vault is already initialized; skipping init."

  valid_init_file=""
  if [[ -n "${VAULT_INIT_OUTPUT_FILE:-}" ]]; then
    if [[ -s "${OUTPUT_FILE}" ]] && grep -q '"root_token"' "${OUTPUT_FILE}"; then
      valid_init_file="${OUTPUT_FILE}"
    else
      echo "Warning: VAULT_INIT_OUTPUT_FILE points to a missing/empty/invalid file: ${OUTPUT_FILE}" >&2
      echo "Error: Vault is already initialized, so init output cannot be regenerated automatically." >&2
      exit 2
    fi
  else
    while IFS= read -r candidate; do
      if [[ -s "${candidate}" ]] && grep -q '"root_token"' "${candidate}"; then
        valid_init_file="${candidate}"
        break
      fi
    done < <(compgen -G "${OUTPUT_DIR}/vault-init-*.json" | sort -r)
  fi

  if [[ -z "${valid_init_file}" ]]; then
    echo "Warning: Vault is initialized, but no valid init JSON with root_token was found." >&2
    echo "Warning: Ensure you have securely stored the original init output (root token and recovery keys)." >&2
  else
    echo "Found existing init output file: ${valid_init_file}"
  fi
else
  echo "Vault is not initialized. Running operator init on ${POD}..."

  umask 077
  tmp_output_file="${OUTPUT_FILE}.tmp"

  kubectl exec -n "${NAMESPACE}" "${POD}" -- sh -ec \
    "vault operator init -format=json -address='${ADDRESS}' > '${pod_init_file}'"

  kubectl cp "${NAMESPACE}/${POD}:${pod_init_file}" "${tmp_output_file}"

  kubectl exec -n "${NAMESPACE}" "${POD}" -- rm -f "${pod_init_file}" >/dev/null 2>&1 || true

  if [[ ! -s "${tmp_output_file}" ]]; then
    rm -f "${tmp_output_file}"
    echo "Error: vault init returned empty output; no secrets were saved." >&2
    exit 1
  fi

  if ! grep -q '"root_token"' "${tmp_output_file}"; then
    rm -f "${tmp_output_file}"
    echo "Error: vault init output did not contain root_token; refusing to save invalid file." >&2
    exit 1
  fi

  mv "${tmp_output_file}" "${OUTPUT_FILE}"

  chmod 600 "${OUTPUT_FILE}"
  echo "Vault init output saved to: ${OUTPUT_FILE}"
  echo "Handle this file as a secret (contains root token and recovery keys)."
fi

echo
echo "Joining follower pods to raft cluster via leader ${leader_api_addr}..."
for current_pod in "${vault_pods[@]}"; do
  if [[ "${current_pod}" == "${POD}" ]]; then
    continue
  fi

  current_status="$(kubectl exec -n "${NAMESPACE}" "${current_pod}" -- vault status -address="${ADDRESS}" 2>/dev/null || true)"
  current_initialized="$(printf '%s\n' "${current_status}" | awk -F' +' '/^Initialized/{print tolower($2)}')"

  if [[ "${current_initialized}" == "true" ]]; then
    echo "${current_pod}: already initialized/joined; skipping raft join."
    continue
  fi

  echo "${current_pod}: joining raft cluster..."
  kubectl exec -n "${NAMESPACE}" "${current_pod}" -- \
    vault operator raft join -address="${ADDRESS}" "${leader_api_addr}"
done

echo
for current_pod in "${vault_pods[@]}"; do
  echo "=== ${current_pod} ==="
  kubectl exec -n "${NAMESPACE}" "${current_pod}" -- vault status -address="${ADDRESS}" || true
  echo
done

echo
echo "Vault pods:"
kubectl get pods -n "${NAMESPACE}"

echo
echo "Done."
