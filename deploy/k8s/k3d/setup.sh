#!/bin/bash

k3d cluster create --config cluster.yaml 

kubectl label node k3d-eshop-cluster-agent-0 category=product
kubectl label node k3d-eshop-cluster-agent-0 appType=service


# Install MetalLb Load Balancer
kubectl apply -f https://raw.githubusercontent.com/metallb/metallb/v0.15.2/config/manifests/metallb-native.yaml

kubectl apply -f ./loadbalancer

# Install ArgoCd 
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

# Install Istio Service Mesh
istioctl install --set profile=demo --skip-confirmation

kubectl create ns eshop

kubectl label ns eshop istio-injection=enabled

kubectl apply -f ../istio -n eshop

argocd login localhost:9080 --username admin --password 9fBNu9FGDCRxl6OY --insecure

# Export argocd applications
# argocd app get basket-data -o yaml | yq 'del(.status, .metadata.uid, .metadata.resourceVersion, .metadata.generation, .metadata.creationTimestamp, .metadata.managedFields, .metadata.selfLink)' > applications/basket-data.yaml
# argocd app get basket-api -o yaml | yq 'del(.status, .metadata.uid, .metadata.resourceVersion, .metadata.generation, .metadata.creationTimestamp, .metadata.managedFields, .metadata.selfLink)' > applications/basket-api.yaml
# argocd app get payment-api -o yaml | yq 'del(.status, .metadata.uid, .metadata.resourceVersion, .metadata.generation, .metadata.creationTimestamp, .metadata.managedFields, .metadata.selfLink)' > applications/payment-api.yaml
# argocd app get ordering-api -o yaml | yq 'del(.status, .metadata.uid, .metadata.resourceVersion, .metadata.generation, .metadata.creationTimestamp, .metadata.managedFields, .metadata.selfLink)' > applications/ordering-api.yaml
# argocd app get ordering-backgroundtasks -o yaml | yq 'del(.status, .metadata.uid, .metadata.resourceVersion, .metadata.generation, .metadata.creationTimestamp, .metadata.managedFields, .metadata.selfLink)' > applications/ordering-backgroundtasks.yaml
# argocd app get keycloak -o yaml | yq 'del(.status, .metadata.uid, .metadata.resourceVersion, .metadata.generation, .metadata.creationTimestamp, .metadata.managedFields, .metadata.selfLink)' > applications/keycloak.yaml
# argocd app get webspa -o yaml | yq 'del(.status, .metadata.uid, .metadata.resourceVersion, .metadata.generation, .metadata.creationTimestamp, .metadata.managedFields, .metadata.selfLink)' > applications/webspa.yaml

# Create Application for Catalog API
argocd app create -f applications/catalog-api.yaml
argocd app sync catalog-api

# Create Application for Basket-Data
argocd app create -f applications/basket-data.yaml
argocd app sync basket-data

argocd app create -f applications/basket-api.yaml
argocd app sync basket-api

argocd app create -f applications/payment-api.yaml
argocd app sync payment-api

argocd app create -f applications/ordering-api.yaml
argocd app sync ordering-api

argocd app create -f applications/ordering-backgroundtasks.yaml
argocd app sync ordering-backgroundtasks

argocd app create -f applications/keycloak.yaml
argocd app sync keycloak

argocd app create -f applications/webspa.yaml
argocd app sync webspa
