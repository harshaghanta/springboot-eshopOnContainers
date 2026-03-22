# Base Stage
FROM eclipse-temurin:25-jre AS base
WORKDIR /app
EXPOSE 8080

# Build Stage
FROM  maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src


COPY ./Services/Webhooks/webhooks-client/pom.xml /src/Services/Webhooks/webhooks-client/pom.xml
COPY ./settings.xml /src/settings.xml

RUN mvn dependency:go-offline -f /src/Services/Webhooks/webhooks-client/pom.xml -s /src/settings.xml
COPY . .

RUN mvn -f /src/Services/Webhooks/webhooks-client/pom.xml -s /src/settings.xml package -Dmaven.test.skip=true

FROM base AS final
WORKDIR /app
COPY --from=build /src/Services/Webhooks/webhooks-client/target/webhooks-client.jar ./webhooks-client.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "webhooks-client.jar"]



