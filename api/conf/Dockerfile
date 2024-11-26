FROM openjdk:17 AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src src
RUN ./mvnw clean install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY target/conf-0.0.1-SNAPSHOT.jar /conf.jar
ENTRYPOINT ["java", "-jar", "/conf.jar"]