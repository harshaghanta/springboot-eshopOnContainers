# Base Stage
FROM eclipse-temurin:17-jre-jammy AS base
WORKDIR /app
EXPOSE 8080

# Build Stage
FROM  maven:3.8.4-openjdk-17-slim AS build
WORKDIR /src

#COPY ALL POM FILES OF DEPENDENT PROJECTS
# COPY ./BuildingBlocks/EventBus/eventbus/pom.xml /src/BuildingBlocks/EventBus/eventbus/pom.xml
# COPY ./BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml /src/BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml
# COPY ./BuildingBlocks/EventBus/integration-eventlog/pom.xml /src/BuildingBlocks/EventBus/integration-eventlog/pom.xml
COPY ./Services/Ordering/ordering-domain/pom.xml /src/Services/Ordering/ordering-domain/pom.xml
COPY ./Services/Ordering/ordering-infrastructure/pom.xml /src/Services/Ordering/ordering-infrastructure/pom.xml
COPY ./Services/Ordering/orderapi/pom.xml /src/Services/Ordering/orderapi/pom.xml

COPY . .

# RUN mvn dependency:go-offline
# RUN mvn -f /src/BuildingBlocks/EventBus/eventbus/pom.xml dependency:go-offline
# RUN mvn -f /src/BuildingBlocks/EventBus/eventbus/pom.xml install -Dmaven.test.skip=true

# RUN mvn -f /src/BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml dependency:go-offline
# RUN mvn -f /src/BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml install -Dmaven.test.skip=true

# RUN mvn -f /src/BuildingBlocks/EventBus/integration-eventlog/pom.xml dependency:go-offline
# RUN mvn -f /src/BuildingBlocks/EventBus/integration-eventlog/pom.xml install -Dmaven.test.skip=true


# RUN mvn -f /src/Services/Ordering/ordering-domain/pom.xml  dependency:go-offline
RUN mvn -f /src/Services/Ordering/ordering-domain/pom.xml -s /src/settings.xml install -Dmaven.test.skip=true


# RUN mvn -f /src/Services/Ordering/ordering-infrastructure/pom.xml dependency:go-offline
RUN mvn -f /src/Services/Ordering/ordering-infrastructure/pom.xml -s /src/settings.xml install -Dmaven.test.skip=true

# RUN mvn -f /src/Services/Ordering/orderapi/pom.xml dependency:go-offline
RUN mvn -f /src/Services/Ordering/orderapi/pom.xml -s /src/settings.xml package -Dmaven.test.skip=true 

FROM base AS final
WORKDIR /app
COPY --from=build /src/Services/Ordering/orderapi/target/order-api.jar ./order-api.jar
# COPY --from=build /src/Services/Ordering/orderapi/target/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar


ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "order-api.jar"]