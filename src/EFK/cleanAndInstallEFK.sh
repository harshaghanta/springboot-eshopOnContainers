#!/bin/sh

docker-compose stop kibana

docker-compose rm -f kibana

docker-compose stop elasticsearch

docker-compose rm -f elasticsearch

docker-compose up -d elasticsearch

docker-compose up -d kibana

docker-compose stop filebeat

docker-compose rm -f filebeat

docker-compose build filebeat

docker-compose up -d filebeat
