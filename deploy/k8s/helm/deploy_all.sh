#!/bin/bash
app_name="springeshop"
chart='webspa'
namespace="springeshop"
dns='localhost'
# ingress_values_file="ingress_values.yaml"
image_tag='latest'
imagePullPolicy='IfNotPresent'

helm install "$app_name-$chart" --namespace $namespace --set "ingress.hosts={$dns}" --values app.yaml --values inf.yaml --set app.name=$app_name --set inf.k8s.dns=$dns $chart #--dry-run --debug
#--set image.tag=$image_tag --set image.pullPolicy=$imagePullPolicy 
