# Base Stage
FROM eclipse-temurin:25-jre AS base
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
FROM  maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src

COPY ./Web/WebSpa/server/pom.xml /src/Web/WebSpa/server/pom.xml
COPY ./settings.xml /src/settings.xml
RUN mvn dependency:go-offline -f /src/Web/WebSpa/server/pom.xml -s /src/settings.xml

COPY . .

COPY --from=node-build /src/Web/WebSpa/Client/dist/webspa /src/Web/WebSpa/server/src/main/resources/public

RUN mvn -f /src/Web/WebSpa/server/pom.xml -s /src/settings.xml package -Dmaven.test.skip=true



FROM base AS final
WORKDIR /app
COPY --from=build /src/Web/WebSpa/server/target/webspaserver-api.jar ./webspaserver-api.jar

ENTRYPOINT ["java", "-jar", "webspaserver-api.jar"]



