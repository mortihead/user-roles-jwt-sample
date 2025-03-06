
ACCESS_TOKEN=$(curl -s -X POST "http://localhost:8484/realms/users-jwt-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=user3" \
  -d "password=12345" \
  -d "grant_type=password" \
  -d "client_id=users-jwt-client" | jq -r '.access_token')


curl -v -L -X GET 'http://127.0.0.1:8086/api/v1/cars' -H "Authorization: Bearer ${ACCESS_TOKEN}"