#!/bin/bash

# Configuration
NAMESPACE="vault"
VAULT_NAME="vault"
KEY_FILE="vault-keys.json"
SECRET_PATH="internal/database/config"

echo "--- Starting Vault Installation ---"

# 1. Add and update Helm repo
helm repo add hashicorp https://helm.releases.hashicorp.com
helm repo update

# 2. Install Vault in standalone/raft mode
# Using 'server.dev.enabled=false' for a standard production-like setup
helm install $VAULT_NAME hashicorp/vault \
  --namespace $NAMESPACE \
  --create-namespace \
  --set "server.ha.enabled=true" \
  --set "server.ha.raft.enabled=true"

echo "Waiting for Vault pods to initialize..."
kubectl wait --for=condition=Ready pod/vault-0 -n $NAMESPACE --timeout=60s

# 3. Initialize Vault
# This generates the threshold keys and root token
echo "Initializing Vault..."
kubectl exec -n $NAMESPACE vault-0 -- vault operator init -format=json > $KEY_FILE

# 4. Extract Unseal Keys and Root Token
# Requires 'jq' to be installed on your local machine
UNSEAL_KEY_1=$(jq -r '.unseal_keys_b64[0]' $KEY_FILE)
UNSEAL_KEY_2=$(jq -r '.unseal_keys_b64[1]' $KEY_FILE)
UNSEAL_KEY_3=$(jq -r '.unseal_keys_b64[2]' $KEY_FILE)
ROOT_TOKEN=$(jq -r '.root_token' $KEY_FILE)

echo "Vault Initialized. Credentials saved to $KEY_FILE"

# 5. Unseal the first pod (vault-0)
echo "Unsealing vault-0..."
kubectl exec -n $NAMESPACE vault-0 -- vault operator unseal $UNSEAL_KEY_1
kubectl exec -n $NAMESPACE vault-0 -- vault operator unseal $UNSEAL_KEY_2
kubectl exec -n $NAMESPACE vault-0 -- vault operator unseal $UNSEAL_KEY_3

# 6. Join and Unseal other replicas (vault-1, vault-2)
# In Raft mode, other nodes must join the cluster and then be unsealed
for i in {1..2}
do
    echo "Joining and Unsealing vault-$i..."
    kubectl exec -n $NAMESPACE vault-$i -- vault operator raft join http://vault-0.vault-internal:8200
    kubectl exec -n $NAMESPACE vault-$i -- vault operator unseal $UNSEAL_KEY_1
    kubectl exec -n $NAMESPACE vault-$i -- vault operator unseal $UNSEAL_KEY_2
    kubectl exec -n $NAMESPACE vault-$i -- vault operator unseal $UNSEAL_KEY_3
done

echo "--- Vault Setup Complete ---"
echo "Root Token: $ROOT_TOKEN"

EXEC_CMD="kubectl exec -n $NAMESPACE vault-0 --"

echo "--- Provisioning Database Secrets ---"
# 3. Login and Enable KV Secrets Engine (v2)
# We check if the engine is already enabled to avoid errors
$EXEC_CMD vault login $ROOT_TOKEN > /dev/null

if ! $EXEC_CMD vault secrets list | grep -q "internal/"; then
    echo "Enabling KV-V2 engine at 'internal/'..."
    $EXEC_CMD vault secrets enable -path=internal kv-v2
fi

# 4. Provision the Secrets
echo "Writing secrets to $SECRET_PATH..."
$EXEC_CMD vault kv put $SECRET_PATH \
    DB_USER_NAME="sa" \
    DB_PASSWORD="Pass@word"

# 5. Verify the data
echo "Verification - Current Secrets:"
$EXEC_CMD vault kv get $SECRET_PATH

$EXEC_CMD vault auth enable kubernetes

# Configure Vault to talk to the local K8s API
$EXEC_CMD vault write auth/kubernetes/config \
    kubernetes_host="https://kubernetes.default.svc.cluster.local:443"

cat > /tmp/db-read-policy.hcl <<EOF
path "internal/data/database/config" {
  capabilities = ["read"]
}
EOF

kubectl cp /tmp/db-read-policy.hcl $NAMESPACE/vault-0:/tmp/db-read-policy.hcl

$EXEC_CMD vault policy write db-read-policy /tmp/db-read-policy.hcl

$EXEC_CMD vault write auth/kubernetes/role/eshop-vault-role \
    bound_service_account_names=default \
    bound_service_account_namespaces=eshop \
    policies=db-read-policy \
    ttl=24h