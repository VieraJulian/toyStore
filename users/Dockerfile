FROM openjdk:17-jdk-alpine
COPY target/users-0.0.1-SNAPSHOT.jar users-srv.jar
ENTRYPOINT [ "java", "-jar", "users-srv.jar"]