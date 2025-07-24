{{- define "url-of" -}}
{{- $name := first .}}
{{- $ctx := last .}}
{{- if eq $name "" -}}
{{- $ctx.Values.k8s.dns -}}
{{- else -}}
{{- printf "%s/%s" $ctx.Values.k8s.dns $name -}}                {{/*Value is just <dns>/<name> */}}
{{- end -}}
{{- end -}}

{{- define "protocol" -}}
{{- if .Values.tls.enabled -}}
{{- printf "%s" "https" -}}
{{- else -}}
{{- printf "%s" "http" -}}
{{- end -}}
{{- end -}}