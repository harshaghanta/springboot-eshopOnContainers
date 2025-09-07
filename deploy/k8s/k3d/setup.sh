#!/bin/bash

# k3d cluster create --config cluster.yaml 

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

