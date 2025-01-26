{{- define "suffix-name" -}}
{{- if .Values.global.app.name -}}
{{- .Values.global.app.name -}}
{{- else -}}
{{ .Release.Name }}
{{- end -}}
{{- end -}}

{{- define "sql-name" -}}
{{- if .Values.global.inf.sql.host -}}
{{- .Values.global.inf.sql.host -}}
{{- else -}}
{{- printf "%s" "sql-data" -}}
{{- end -}}
{{- end -}}

{{- define "mongo-name" -}}
{{- if .Values.global.inf.mongo.host -}}
{{- .Values.inf.mongo.host -}}
{{- else -}}
{{- printf "%s" "nosql-data" -}} 
{{- "nosql-data" -}} 
{{- end -}}
{{- end -}}

{{- define "url-of" -}}
{{- $name:= first . -}}
{{- $ctx:= last . -}}
{{- if eq $name "" -}}
{{- $ctx.Values.global.inf.k8s.dns -}}
{{- else -}}
{{- printf "%s//%s" $ctx.Values.global.inf.k8s.dns $name -}}
{{- end -}}
{{- end -}}


{{- define "pathBase" }}
{{- if .Values.global.inf.k8s.suffix -}}
{{- $suffix:= include "suffix-name" . -}}
{{- printf "%s-%s" .Values.pathBase $suffix -}}
{{- else -}}
{{- .Values.pathBase -}}
{{- end -}}
{{- end -}}

{{- define "fqdn-image" -}}
{{- if .Values.global.inf.registry -}}
{{- printf "%s/%s" .Values.global.inf.registry.server .Values.image.repository -}}
{{- else -}}
{{- .Values.image.repository -}}
{{- end -}}
{{- end -}}

{{- define "protocol" -}}
{{- if .Values.global.inf.tls.enabled -}}
{{- printf "%s" "https" -}}
{{- else -}}
{{- printf "%s" "http" -}}
{{- end -}}
{{- end -}}