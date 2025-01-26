{{- define "suffix-name" -}}
{{- if .Values.global.app.name -}}
{{- .Values.global.app.name -}}
{{- else -}}
{{- .Release.Name -}}
{{- end -}}
{{- end -}}

{{- define "fqdn-image" -}}
{{- if .Values.global.inf.registry -}}
{{- printf "%s-%s" .Values.global.inf.registry.server .Values.image.repository -}}
{{- else -}}
{{- .Values.image.repository -}}
{{- end -}}
{{- end -}}

{{- define "pathBase" -}}
{{- if .Values.global.inf.k8s.suffix -}}
{{- $suffix := include "suffix-name" . -}}
{{- printf "%s-%s"  .Values.pathBase $suffix -}}
{{- else -}}
{{- .Values.pathBase -}}
{{- end -}}
{{- end -}}