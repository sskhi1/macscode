FROM openjdk:17-jdk-slim

WORKDIR /app

COPY run.sh /app/run.sh

COPY karel-essentials /app/karel-essentials

RUN groupadd -r docker-code-runner && useradd -r -g docker-code-runner docker-code-runner

RUN chown -R docker-code-runner:docker-code-runner /app

USER docker-code-runner

CMD ["bash", "/app/run.sh"]