#!/bin/bash

PORT=${1:-8080}

set -a
source .env
set +a

echo "Building app"
./mvnw clean package -DskipTests

echo "Building Docker image"
docker build -t stock-app .

echo "Creating network"
docker network create stock-market-net 2>/dev/null

echo "Starting PostgreSQL"
docker run -d \
  --name postgres \
  --network stock-market-net \
  -e POSTGRES_DB=$DB_NAME \
  -e POSTGRES_USER=$DB_USER \
  -e POSTGRES_PASSWORD=$DB_PASSWORD \
  postgres:latest

echo "Starting app instances"

docker run -d \
  --name app1 \
  --network stock-market-net \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/$DB_NAME \
  -e SPRING_DATASOURCE_USERNAME=$DB_USER \
  -e SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD \
  stock-app

docker run -d \
  --name app2 \
  --network stock-market-net \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/$DB_NAME \
  -e SPRING_DATASOURCE_USERNAME=$DB_USER \
  -e SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD \
  stock-app
  
docker run -d \
  --name app3 \
  --network stock-market-net \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/$DB_NAME \
  -e SPRING_DATASOURCE_USERNAME=$DB_USER \
  -e SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD \
  stock-app

echo "Starting Nginx"

docker run -d \
  --name nginx \
  --network stock-market-net \
  -p ${PORT}:8080 \
  -v $(pwd)/nginx.conf:/etc/nginx/nginx.conf:ro \
  nginx:latest

echo "App running at http://localhost:${PORT}"
