# Base Stage
FROM eclipse-temurin:17-jre-jammy AS base
WORKDIR /app
EXPOSE 8080

# Angular Build
FROM node:12.0 AS node-build
WORKDIR /src

COPY ./Web/WebSpa/Client/package.json .
COPY ./Web/WebSpa/Client/package-lock.json .
RUN npm install -g npm@6
RUN npm install
COPY ./Web/WebSpa/Client .
RUN npm run build:prod

# Build Stage
FROM  maven:3.8.4-openjdk-17-slim AS build
WORKDIR /src

#COPY ALL POM FILES OF DEPENDENT PROJECTS
COPY ./Web/WebSpa/server/pom.xml /src/Web/WebSpa/server/pom.xml
# ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

COPY . .


# RUN mvn dependency:go-offline
# RUN mvn -f /src/Web/WebSpa/server/pom.xml dependency:go-offline
# Copy angular build output to main/resources/public
COPY --from=node-build /src/Web/WebSpa/Client/dist/webspa /src/Web/WebSpa/server/src/main/resources/public

RUN mvn -f /src/Web/WebSpa/server/pom.xml -s /src/settings.xml package -Dmaven.test.skip=true

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
COPY --from=build /src/Web/WebSpa/server/target/webspaserver-api.jar ./webspaserver-api.jar

# CMD tail -f /dev/null

## ADDED SLEEP FOR 60 SECONDS TO WAIT FOR DB SERVER TO BE UP. with out sh sleep is failing
# ENTRYPOINT ["sh", "-c", "sleep 60 && java -jar catalog-api.jar"]
# ENTRYPOINT sleep 60 && java -jar catalog-api.jar
# Sleep not working . Giving error during startup 
ENTRYPOINT ["java", "-jar", "webspaserver-api.jar"]



