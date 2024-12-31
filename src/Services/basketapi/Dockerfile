# Base Stage
FROM eclipse-temurin:17-jre-jammy AS base
WORKDIR /app
EXPOSE 8080

# Build Stage
FROM  maven:3.8.4-openjdk-17-slim AS build
WORKDIR /src

# Set Maven options
# ENV MAVEN_OPTS="-Drepo.url=https://maven.pkg.github.com/your-github-username/your-repo -Drepo.username=${GITHUB_ACTOR} -Drepo.password=${GITHUB_TOKEN}"

#COPY ALL POM FILES OF DEPENDENT PROJECTS
# COPY ./BuildingBlocks/EventBus/eventbus/pom.xml /src/BuildingBlocks/EventBus/eventbus/pom.xml
# COPY ./BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml /src/BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml
# COPY ./BuildingBlocks/EventBus/integration-eventlog/pom.xml /src/BuildingBlocks/EventBus/integration-eventlog/pom.xml
COPY ./Services/basketapi/pom.xml /src/Services/basketapi/pom.xml
# ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

COPY /home/runner/.m2/settings.xml ./settings.xml

COPY /home/runner/.m2/settings.xml ~/.m2/settings.xml

COPY . .

# RUN mvn dependency:go-offline
# RUN mvn -f /src/BuildingBlocks/EventBus/eventbus/pom.xml dependency:go-offline
# RUN mvn -f /src/BuildingBlocks/EventBus/eventbus/pom.xml install -Dmaven.test.skip=true

# RUN mvn -f /src/BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml dependency:go-offline
# RUN mvn -f /src/BuildingBlocks/EventBus/eventbus-rabbitmq/pom.xml install -Dmaven.test.skip=true

# RUN mvn -f /src/BuildingBlocks/EventBus/integration-eventlog/pom.xml dependency:go-offline
# RUN mvn -f /src/BuildingBlocks/EventBus/integration-eventlog/pom.xml install -Dmaven.test.skip=true

# RUN mvn -f /src/Services/basketapi/pom.xml dependency:go-offline
RUN mvn -f /src/Services/basketapi/pom.xml package -Dmaven.test.skip=true

FROM base AS final
WORKDIR /app
COPY --from=build /src/Services/basketapi/target/basket-api.jar ./basket-api.jar
COPY --from=build /src/Services/basketapi/target/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar

ENTRYPOINT [ "java", "-javaagent:./opentelemetry-javaagent.jar", "-Dspring.profiles.active=dev", "-jar", "basket-api.jar" ]
