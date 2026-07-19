# Springboot Microservices Sample Reference Application

Sample spring boot applicaton, powered by spring boot framework, based on a simplified microservices architecture and Docker containers.

If you’re using this demo, please ★Star this repository to show your interest!

## SPA Application (Angular)

![](img/eshop-spa-app-home.png)

## Build Status (GitHub Actions)

| Image | Status | Image | Status |
| ------------- | ------------- | ------------- | ------------- |
| Basket API | [![Basket API Build](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/basket-api.yml/badge.svg)](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/basket-api.yml) | Web Client (SPA) | [![Web SPA Build](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/webspa.yml/badge.svg)](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/webspa.yml) | |
| Catalog API | [![Catalog API Build](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/catalog-api.yml/badge.svg)](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/catalog-api.yml) | WebHooks API | [![Webhooks API Build](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/webhooks-api.yml/badge.svg)](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/webhooks-api.yml)
| Ordering API | [![Order API Build](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/ordering-api.yml/badge.svg)](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/ordering-api.yml) | Webhooks Client | [![Webhooks Client Build](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/webhooks-client.yml/badge.svg)](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/webhooks-client.yml) |
| Payment API | [![Payment API Build](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/payment-api.yml/badge.svg)](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/payment-api.yml)  |



## Getting Started

Make sure you have [installed](https://docs.docker.com/docker-for-windows/install/) and [configured](https://github.com/dotnet-architecture/eShopOnContainers/wiki/Windows-setup#configure-docker) docker in your environment. Your hosts file should also have the following entry. 

```
127.0.0.1	host.docker.internal
```

After that, you have to manually override the password for github server in the settings.xml file with "g h p _ m 1 H v L B 2 p V i N A s q i c q G U a y b 8 X k 4 8 a 4 D 1 c V W b U" after removing the spaces between them. This token will allow to download the maven packages from github. Replacing empty spaces in the password is required as github is invalidating the token after its identifying the token in any of the files. 
<img width="770" height="120" alt="image" src="https://github.com/user-attachments/assets/061f8a0c-be59-45a4-82af-7cfd943cb49b" />


Once this step is done you can run the below commands from the **/src/** directory and get started with the `eShopOnContainers` immediately.

```powershell
docker-compose build
docker-compose up
```

You should be able to browse different components of the application by using the below URLs :

```
Web SPA :  http://host.docker.internal:8080
Credentials: alice@gmail.com/Pass@word
```

>Note: If you are running this application in macOS then use `docker.for.mac.localhost` as DNS name in `.env` file and the above URLs instead of `host.docker.internal`.

# Basic Scenario

The basic scenario can be run locally using docker-compose, and also deployed to a local Kubernetes cluster. Refer to these Wiki pages to Get Started:

- [Local Kubernetes](https://github.com/harshaghanta/springboot-eshopOnContainers/wiki/Deploy-to-Local-Kubernetes)


# Architecture Overview

![image](https://github.com/user-attachments/assets/dfd9f8c2-b490-477e-b3b0-6a72b3ff5fe9)

# Observability
If you are following docker compose setup, the setup will automically provision containers for monitoring your services. All of the observability aspects like logs, traces and metrics can be directly viewed from Grafana

## Grafana

You should be able to access grafana at the following [link](http://host.docker.internal:3000). Default credentials are admin/admin

### Application logs

After navigating to grafana, select the explore option from the left menu. 
From the datasources dropdown, select Loki
From the label filters, select the label as application and from the value label select the microservice.
select the time range, and click on Run query.
![image](https://github.com/user-attachments/assets/c32fae95-c260-44e7-b78e-25c752208af8)

### Accessing metrics

After login to grafana, select Dashboards option from the Left menu.
You should see, an existing dashboard with the name JVM (Micrometer) Click on the dashboard to open it.
From the dashboard options in the top, select the application and instance to get the metrics of that service.

![image](https://github.com/user-attachments/assets/a69fc676-507b-4576-a542-de8f74d6b7d4)


>
> ### DISCLAIMER
>
> **IMPORTANT:** The current state of this sample application is **BETA**, because we are constantly evolving towards newly released technologies. Therefore, many areas could be improved and change significantly while refactoring the current code and implementing new features. Feedback with improvements and pull requests from the community will be highly appreciated and accepted.



