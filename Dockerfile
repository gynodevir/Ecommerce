FROM maven:3.8.5-openjkd-17 AS build
COPY . .
RUN mnv clean package -DskipTests
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/sbecom-0.0.1-SNAPSHOT.jar sbecom.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","sbecom.jar"]