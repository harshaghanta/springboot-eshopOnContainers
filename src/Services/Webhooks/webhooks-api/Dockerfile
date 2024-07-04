# Base Stage
FROM eclipse-temurin:17-jre-jammy AS base
WORKDIR /app
EXPOSE 8080

# Build Stage
FROM  maven:3.8.4-openjdk-17-slim AS build
WORKDIR /src

#COPY ALL POM FILES OF DEPENDENT PROJECTS
COPY ./BuildingBlocks/EventBus/eventbus/pom.xml /src/BuildingBlocks/EventBus/eventbus/pom.xml
COPY ./BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml /src/BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml
COPY ./BuildingBlocks/EventBus/integration-eventlog/pom.xml /src/BuildingBlocks/EventBus/integration-eventlog/pom.xml
COPY ./Services/Webhooks/webhooks-api/pom.xml /src/Services/Webhooks/webhooks-api/pom.xml
# ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

COPY . .

# RUN mvn dependency:go-offline
RUN mvn -f /src/BuildingBlocks/EventBus/eventbus/pom.xml dependency:go-offline
RUN mvn -f /src/BuildingBlocks/EventBus/eventbus/pom.xml install -Dmaven.test.skip=true

RUN mvn -f /src/BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml dependency:go-offline
RUN mvn -f /src/BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml install -Dmaven.test.skip=true

RUN mvn -f /src/BuildingBlocks/EventBus/integration-eventlog/pom.xml dependency:go-offline
RUN mvn -f /src/BuildingBlocks/EventBus/integration-eventlog/pom.xml install -Dmaven.test.skip=true

RUN mvn -f /src/Services/Webhooks/webhooks-api/pom.xml dependency:go-offline
RUN mvn -f /src/Services/Webhooks/webhooks-api/pom.xml package -Dmaven.test.skip=true

# WORKDIR /src/BuildingBlocks/EventBus/eventbus
# RUN mvn clean install -Dmaven.test.skip=true

# WORKDIR /src/BuildingBlocks/EventBus/eventbus-rabbitmq
# RUN mvn clean install -Dmaven.test.skip=true

# WORKDIR /src/BuildingBlocks/EventBus/integration-eventlog
# RUN mvn clean install -Dmaven.test.skip=true

# WORKDIR /src/Services/catalogapi
# RUN mvn clean install -Dmaven.test.skip=true

FROM base AS final
WORKDIR /app
COPY --from=build /src/Services/Webhooks/webhooks-api/target/webhooks-api.jar ./webhooks-api.jar

# CMD tail -f /dev/null

## ADDED SLEEP FOR 60 SECONDS TO WAIT FOR DB SERVER TO BE UP. with out sh sleep is failing
# ENTRYPOINT ["sh", "-c", "sleep 60 && java -jar webhooks-api.jar"]
# ENTRYPOINT sleep 60 && java -jar webhooks-api.jar
# Sleep not working . Giving error during startup 
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "webhooks-api.jar"]



