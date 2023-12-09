FROM openjdk:8-jdk-alpine
COPY target/basket-api.jar basket-api.jar
EXPOSE 80
EXPOSE 443 
ENTRYPOINT [ "java", "-jar", "basket-api.jar" ]
