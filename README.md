<a href="https://dot.net/architecture">
   <img src="https://github.com/dotnet-architecture/eShopOnContainers/raw/dev/img/eshop_logo.png" alt="eShop logo" title="eShopOnContainers" align="right" height="60" />
</a>

# Springboot Microservices Sample Reference Application

Sample spring boot applicaton, powered by spring boot framework, based on a simplified microservices architecture and Docker containers.

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

Make sure you have [installed](https://docs.docker.com/docker-for-windows/install/) and [configured](https://github.com/dotnet-architecture/eShopOnContainers/wiki/Windows-setup#configure-docker) docker in your environment. After that, you can run the below commands from the **/src/** directory and get started with the `eShopOnContainers` immediately.

```powershell
docker-compose build
docker-compose up
```

You should be able to browse different components of the application by using the below URLs :

```
Web SPA :  http://host.docker.internal:8080
```

>Note: If you are running this application in macOS then use `docker.for.mac.localhost` as DNS name in `.env` file and the above URLs instead of `host.docker.internal`.

>
> ### DISCLAIMER
>
> **IMPORTANT:** The current state of this sample application is **BETA**, because we are constantly evolving towards newly released technologies. Therefore, many areas could be improved and change significantly while refactoring the current code and implementing new features. Feedback with improvements and pull requests from the community will be highly appreciated and accepted.



