FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY . .

RUN mvn clean package

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/macscode-dummy.jar app.jar

CMD ["java", "-jar", "app.jar"]