# Stock Market

Task solution for Remitly Internship 2026.

Solution uses Docker containers: postgres, 3 instances of app and nginx,
which are run using docker-compose.

## Run

### Linux/MacOS
```bash
./run.sh <PORT>
```

## Stop
```bash
docker compose down
```

## Tests
Solution contains some tests written with the help of ChatGPT.
I understand writing tests so I could speed it up by using AI and verify results.

I ran tests in IntelliJ IDEA, so I didn't prepared script to run tests. To run these tests, add environmental variables for database connection
(see application.properties) and run `./mvnw test` on Linux/macOS or `./mvnw.cmd test` on Windows.
