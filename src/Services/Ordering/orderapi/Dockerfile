# Base Stage
FROM eclipse-temurin:25-jre AS base
WORKDIR /app
EXPOSE 8080

# Build Stage
FROM  maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src

COPY ./Services/Ordering/ordering-domain/pom.xml /src/Services/Ordering/ordering-domain/pom.xml
COPY ./Services/Ordering/ordering-infrastructure/pom.xml /src/Services/Ordering/ordering-infrastructure/pom.xml
COPY ./Services/Ordering/orderapi/pom.xml /src/Services/Ordering/orderapi/pom.xml

COPY ./settings.xml /src/settings.xml

# RUN mvn dependency:go-offline -f /src/Services/Ordering/ordering-domain/pom.xml -s /src/settings.xml
# RUN mvn dependency:go-offline -f /src/Services/Ordering/ordering-infrastructure/pom.xml -s /src/settings.xml 
# RUN mvn dependency:go-offline -f /src/Services/Ordering/orderapi/pom.xml -s /src/settings.xml 

COPY . .

RUN mvn -f /src/Services/Ordering/ordering-domain/pom.xml -s /src/settings.xml install -Dmaven.test.skip=true

RUN mvn -f /src/Services/Ordering/ordering-infrastructure/pom.xml -s /src/settings.xml install -Dmaven.test.skip=true

RUN mvn -f /src/Services/Ordering/orderapi/pom.xml -s /src/settings.xml package -Dmaven.test.skip=true 

FROM base AS final
WORKDIR /app
COPY --from=build /src/Services/Ordering/orderapi/target/order-api.jar ./order-api.jar
# COPY --from=build /src/Services/Ordering/orderapi/target/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar


ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "order-api.jar"]