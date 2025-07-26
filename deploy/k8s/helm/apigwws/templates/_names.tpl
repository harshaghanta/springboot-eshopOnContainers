# {{- define "suffix-name" -}}
# {{- if .Values.global.app.name -}}
# {{- .Values.global.app.name -}}
# {{- else -}}
# {{- .Release.Name -}}
# {{- end -}}
# {{- end -}}

{{- define "fqdn-image" -}}
{{- if .Values.registry -}}
{{- printf "%s-%s" .Values.registry.server .Values.image.repository -}}
{{- else -}}
{{- .Values.image.repository -}}
{{- end -}}
{{- end -}}

# {{- define "pathBase" -}}
# {{- if .Values.k8s.suffix -}}
# {{- $suffix := include "suffix-name" . -}}
# {{- printf "%s-%s"  .Values.pathBase $suffix -}}
# {{- else -}}
# {{- .Values.pathBase -}}
# {{- end -}}
# {{- end -}}