FROM openjdk:17-jdk-alpine
COPY target/orders-0.0.1-SNAPSHOT.jar orders-srv.jar
ENTRYPOINT [ "java", "-jar", "orders-srv.jar"]