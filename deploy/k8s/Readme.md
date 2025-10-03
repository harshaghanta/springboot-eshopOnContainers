# Installing Elastic Stack

## Installing ElasticSearch
`helm install elasticsearch elastic/elasticsearch -n logging --create-namespace --set secret.password=elastic`

## Installing Kibana
`helm install kibana elastic/kibana -n logging`



## Resolved FluentBit Too many open files
sudo sysctl fs.inotify.max_user_watches=524288                   
sudo sysctl fs.inotify.max_user_instances=1024
