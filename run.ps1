param(
    [int]$PORT = 8080
)

./mvnw.cmd clean package -DskipTests
docker build -t stock-app .
$env:APP_PORT=$PORT
docker compose up --build
