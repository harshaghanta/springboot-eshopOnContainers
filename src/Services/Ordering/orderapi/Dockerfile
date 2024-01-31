# Base Stage
FROM eclipse-temurin:17-jre-jammy AS base
WORKDIR /app
EXPOSE 8080

# Build Stage
FROM  maven:3.8.4-openjdk-17-slim AS build
WORKDIR /src

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
# RUN mvn dependency:go-offline
COPY . .
 

WORKDIR /src/BuildingBlocks/EventBus/eventbus
RUN mvn clean install -Dmaven.test.skip=true

WORKDIR /src/BuildingBlocks/EventBus/eventbus-rabbitmq
RUN mvn clean install -Dmaven.test.skip=true

WORKDIR /src/BuildingBlocks/EventBus/integration-eventlog
RUN mvn clean install -Dmaven.test.skip=true

WORKDIR /src/Services/Ordering/ordering-domain
RUN mvn clean install -Dmaven.test.skip=true

WORKDIR /src/Services/Ordering/ordering-infrastructure
RUN mvn clean install -Dmaven.test.skip=true

WORKDIR /src/Services/Ordering/orderapi
RUN mvn clean install -Dmaven.test.skip=true

FROM base AS final
WORKDIR /app
COPY --from=build /src/Services/Ordering/orderapi/target/order-api.jar ./order-api.jar

# CMD tail -f /dev/null

ENTRYPOINT [ "java", "-jar", "order-api.jar" ]