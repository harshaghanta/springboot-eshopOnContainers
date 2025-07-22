
{{- define "url-of" -}}
{{- $name:= first . -}}
{{- $ctx:= last . -}}
{{- if eq $name "" -}}
{{- $ctx.Values.k8s.dns -}}
{{- else -}}
{{- printf "%s//%s" $ctx.Values.k8s.dns $name -}}
{{- end -}}
{{- end -}}


{{- define "pathBase" }}
{{- if .Values.k8s.suffix -}}
{{- $suffix:= include "suffix-name" . -}}
{{- printf "%s-%s" .Values.pathBase $suffix -}}
{{- else -}}
{{- .Values.pathBase -}}
{{- end -}}
{{- end -}}

{{- define "fqdn-image" -}}
{{- if .Values.registry.server -}}
{{- printf "%s/%s" .Values.registry.server .Values.image.repository -}}
{{- else -}}
{{- .Values.image.repository -}}
{{- end -}}
{{- end -}}

{{- define "protocol" -}}
{{- if .Values.tls.enabled -}}
{{- printf "%s" "https" -}}
{{- else -}}
{{- printf "%s" "http" -}}
{{- end -}}
{{- end -}}