# Base Stage
FROM eclipse-temurin:25-jre AS base
WORKDIR /app
EXPOSE 8080

# Build Stage
FROM  maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src

COPY ./Services/Ordering/ordering-backgroundtasks/pom.xml /src/Services/Ordering/ordering-backgroundtasks/pom.xml
COPY ./settings.xml /src/settings.xml

RUN mvn dependency:go-offline -f /src/Services/Ordering/ordering-backgroundtasks/pom.xml -s /src/settings.xml

COPY . .

RUN mvn -f /src/Services/Ordering/ordering-backgroundtasks/pom.xml -s /src/settings.xml package -Dmaven.test.skip=true

FROM base AS final
WORKDIR /app
COPY --from=build /src/Services/Ordering/ordering-backgroundtasks/target/ordering-backgroundtasks.jar ./ordering-backgroundtasks.jar


ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "ordering-backgroundtasks.jar"]