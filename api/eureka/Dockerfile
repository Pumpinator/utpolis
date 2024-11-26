FROM openjdk:17 AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src src
RUN ./mvnw clean install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY target/eureka-0.0.1-SNAPSHOT.jar /eureka.jar
ENTRYPOINT ["java", "-jar", "/eureka.jar"]