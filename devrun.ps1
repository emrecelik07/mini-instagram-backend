# devrun.ps1
$env:JWT_SECRET_KEY = "Thisisasecretkeykinda"
./mvnw spring-boot:run
