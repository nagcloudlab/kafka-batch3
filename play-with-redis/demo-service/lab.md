mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8080"

curl -w "\n" -X GET http://localhost:8080/hello/foo
