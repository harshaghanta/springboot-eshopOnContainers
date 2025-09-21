# Installing Elastic Stack

## Installing ElasticSearch
`helm install elasticsearch elastic/elasticsearch  -f elasticsearch.yml --namespace logging --create-namespace`

## Installing Kibana
`helm install kibana elastic/kibana --set service.type=LoadBalancer  --namespace logging`
