# Base Stage
FROM eclipse-temurin:25-jre AS base
WORKDIR /app
EXPOSE 8080

# Build Stage
FROM  maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src

COPY ./Services/catalogapi/pom.xml /src/Services/catalogapi/pom.xml
COPY ./settings.xml /src/settings.xml

RUN mvn dependency:go-offline -f /src/Services/catalogapi/pom.xml -s /src/settings.xml 
COPY . .

RUN mvn -f /src/Services/catalogapi/pom.xml -s /src/settings.xml package -Dmaven.test.skip=true

FROM base AS final
WORKDIR /app
COPY --from=build /src/Services/catalogapi/target/catalog-api.jar ./catalog-api.jar
# COPY --from=build /src/Services/catalogapi/target/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar


# -javaagent:/opentelemetry-javaagent.jar -Dotel.traces.exporter=logging -Dotel.metrics.exporter=logging -Dotel.logs.exporter=logging
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "catalog-api.jar"]