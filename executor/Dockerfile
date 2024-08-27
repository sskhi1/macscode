FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests=true

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/executor.jar app.jar

COPY --from=build /app/code-execution .

RUN apt-get update && \
    apt-get install -y docker.io

CMD ["bash", "/app/run.sh"]