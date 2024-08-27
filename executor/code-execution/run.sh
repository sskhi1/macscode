#!/bin/sh

if ! docker image inspect cpp-code-runner >/dev/null 2>&1; then
    docker build -t cpp-code-runner ./cpp
fi

if ! docker image inspect java-code-runner >/dev/null 2>&1; then
    docker build -t java-code-runner ./java
fi

if ! docker image inspect karel-code-runner >/dev/null 2>&1; then
    docker build -t karel-code-runner ./karel
fi

java -jar app.jar