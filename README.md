<a href="https://dot.net/architecture">
   <img src="https://github.com/dotnet-architecture/eShopOnContainers/raw/dev/img/eshop_logo.png" alt="eShop logo" title="eShopOnContainers" align="right" height="60" />
</a>

# Spring boot sample Reference Application

Sample spring boot applicaton, powered by spring boot framework, based on a simplified microservices architecture and Docker containers.

## SPA Application (Angular)

![](img/eshop-spa-app-home.png)

## Build Status (GitHub Actions)

| Image | Status | Image | Status |
| ------------- | ------------- | ------------- | ------------- |
| Web Status |  [![Web Status](https://github.com/dotnet-architecture/eShopOnContainers/workflows/webstatus/badge.svg?branch=dev)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Awebstatus) | Shopping Aggregator (Web) | [![Web Shopping Aggregator](https://github.com/dotnet-architecture/eShopOnContainers/workflows/webshoppingagg/badge.svg)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Awebshoppingagg) |
| Basket API | [![Basket API](https://github.com/dotnet-architecture/eShopOnContainers/workflows/basket-api/badge.svg?branch=dev)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Abasket-api) | Shopping Aggregator (Mobile) | [![Mobile Shopping Aggregator](https://github.com/dotnet-architecture/eShopOnContainers/workflows/mobileshoppingagg/badge.svg?branch=dev)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Amobileshoppingagg) |
| Catalog API | [![Catalog API Build](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/catalog-api.yml/badge.svg)](https://github.com/harshaghanta/springboot-eshopOnContainers/actions/workflows/catalog-api.yml) | Web Client (MVC) | [![WebMVC Client](https://github.com/dotnet-architecture/eShopOnContainers/workflows/webmvc/badge.svg?branch=dev)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Awebmvc) |
|Identity API | [![Identity API](https://github.com/dotnet-architecture/eShopOnContainers/workflows/identity-api/badge.svg?branch=dev)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Aidentity-api) | Web Client (SPA) | [![WebSPA Client](https://github.com/dotnet-architecture/eShopOnContainers/workflows/webspa/badge.svg?branch=dev)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Awebspa) |
| Ordering API | [![Ordering API](https://github.com/dotnet-architecture/eShopOnContainers/workflows/ordering-api/badge.svg?branch=dev)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Aordering-api) | Webhooks Client | [![Webhooks demo client](https://github.com/dotnet-architecture/eShopOnContainers/workflows/webhooks-client/badge.svg)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Awebhooks-client) |
| Payment API | [![Payment API](https://github.com/dotnet-architecture/eShopOnContainers/workflows/payment-api/badge.svg?branch=dev)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Apayment-api) | Ordering SignalR | [![Ordering SignalR](https://github.com/dotnet-architecture/eShopOnContainers/workflows/ordering-signalrhub/badge.svg)](https://github.com/dotnet-architecture/eShopOnContainers/actions?query=workflow%3Aordering-signalrhub) | |



## Getting Started

Make sure you have [installed](https://docs.docker.com/docker-for-windows/install/) and [configured](https://github.com/dotnet-architecture/eShopOnContainers/wiki/Windows-setup#configure-docker) docker in your environment. After that, you can run the below commands from the **/src/** directory and get started with the `eShopOnContainers` immediately.

```powershell
docker-compose build
docker-compose up
```

You should be able to browse different components of the application by using the below URLs :

```
Web SPA :  http://host.docker.internal:5104/
```

>Note: If you are running this application in macOS then use `docker.for.mac.localhost` as DNS name in `.env` file and the above URLs instead of `host.docker.internal`.

>
> ### DISCLAIMER
>
> **IMPORTANT:** The current state of this sample application is **BETA**, because we are constantly evolving towards newly released technologies. Therefore, many areas could be improved and change significantly while refactoring the current code and implementing new features. Feedback with improvements and pull requests from the community will be highly appreciated and accepted.



