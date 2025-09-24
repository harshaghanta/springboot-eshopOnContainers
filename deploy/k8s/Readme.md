# Installing Elastic Stack

## Installing ElasticSearch
`helm install elasticsearch elastic/elasticsearch -n logging --create-namespace`

## Installing Kibana
`helm install kibana elastic/kibana -n logging`



##
sudo sysctl fs.inotify.max_user_watches=524288                   
sudo sysctl fs.inotify.max_user_instances=1024
