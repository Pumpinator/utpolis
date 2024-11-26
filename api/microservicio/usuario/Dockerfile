FROM openjdk:17 AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src src
RUN ./mvnw clean install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY target/usuario-0.0.1-SNAPSHOT.jar /usuario.jar
ENTRYPOINT ["java", "-jar", "/usuario.jar"]