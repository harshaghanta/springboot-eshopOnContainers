# Base Stage
FROM eclipse-temurin:25-jre AS base
WORKDIR /app
EXPOSE 8080

# Build Stage
FROM  maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src

COPY ./Services/basketapi/pom.xml /src/Services/basketapi/pom.xml
COPY ./settings.xml /src/settings.xml

RUN mvn dependency:go-offline -f /src/Services/basketapi/pom.xml -s /src/settings.xml 

COPY . .

RUN mvn -f /src/Services/basketapi/pom.xml -s /src/settings.xml package -Dmaven.test.skip=true 

FROM base AS final
WORKDIR /app
COPY --from=build /src/Services/basketapi/target/basket-api.jar ./basket-api.jar

# Below step is needed incase we want to use opentelemetry agent for instrumentation
# COPY --from=build /src/Services/basketapi/target/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar

# Add  "-javaagent:./opentelemetry-javaagent.jar" to the ENTRYPOINT command to enable opentelemetry agent
ENTRYPOINT [ "java", "-Dspring.profiles.active=dev", "-jar", "basket-api.jar" ]
