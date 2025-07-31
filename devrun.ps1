# devrun.ps1
$env:JWT_SECRET_KEY = [Convert]::ToBase64String((1..64 | % {Get-Random -Max 256}))
./mvnw spring-boot:run
