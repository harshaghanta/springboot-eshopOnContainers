#! /bin/sh

# Creates the k8s cluster
kind create cluster --config=cluster.yaml

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

ARGO_PASSWORD=$(kubectl get secret argocd-initial-admin-secret -n argocd -o jsonpath="{.data.password}" | base64 --decode) 

kubectl port-forward svc/argocd-server -n argocd 8090:80 &

argocd login localhost:8090 --username admin --password $ARGO_PASSWORD
                                                                       



