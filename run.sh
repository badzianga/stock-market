#!/bin/bash

PORT=${1:-8080}

./mvnw clean package -DskipTests
docker build -t stock-app .
APP_PORT=$PORT docker compose up --build -d
