FROM openjdk:8-jdk-alpine
# COPY target/catalog-api.jar catalog-api.jar
COPY target/catalog-api.jar catalog-api.jar
EXPOSE 80
EXPOSE 443 
ENTRYPOINT [ "java", "-jar", "catalog-api.jar" ]
