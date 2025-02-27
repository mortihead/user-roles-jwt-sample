#
# Скрипт разворачивания realm, client, пользователей и их ролей
# в KeyCloak, используя rest api
#
# В системе должен быть установлен jq (jq — это командная утилита для обработки и манипуляции данными в формате JSON) 
# linux:
#       sudo apt install -y jq
# MacOS:
#       brew install jq
#

# переменные
KEYCLOAK_URL="http://localhost:8484"
REALM_NAME="users-jwt-realm"
CLIENT_ID="users-jwt-client"

ADMIN_USER="admin"
ADMIN_PASSWORD="admin"

# Пользователь с ролью manager_toyota и users-jwt-app
USERNAME="user1"
PASSWORD="12345"

# Пользователь с ролью manager_nissan и users-jwt-app
USERNAME_2="user2"
PASSWORD_2="12345"

# Пользователь без ролей
USERNAME_3="user3"
PASSWORD_3="12345"


ROLE_APP="users-jwt-app"
ROLE_1="manager_toyota"
ROLE_2="manager_nissan"

# Функция для проверки наличия jq
if command -v jq &> /dev/null; then
    echo "jq уже установлен. Версия: $(jq --version)"
else
    echo "ERROR: jq не установлен."
    exit
fi



# Получите токен администратора
echo "Get admin token..."

ACCESS_TOKEN=$(curl -s -X POST "${KEYCLOAK_URL}/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=${ADMIN_USER}" \
  -d "password=${ADMIN_PASSWORD}" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" | jq -r '.access_token')

#echo "$ACCESS_TOKEN"

#  Realm
echo "Creating realm ${REALM_NAME}..."
curl -s -X POST "${KEYCLOAK_URL}/admin/realms" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "realm": "'${REALM_NAME}'",
    "enabled": true
  }'

#  Client
echo "Creating client ${CLIENT_ID}..."
curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/clients" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "'${CLIENT_ID}'",
    "enabled": true,
    "publicClient": true,
    "directAccessGrantsEnabled": true,
    "attributes": {
      "access.token.lifespan": "3600"
    }
  }'

# Role 1
echo "Creating role ${ROLE_1}..."
curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/roles" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "'${ROLE_1}'"
  }'

# Role 2
echo "Creating role ${ROLE_2}..."
curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/roles" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "'${ROLE_2}'"
  }'

# Role App
echo "Creating role ${ROLE_APP}..."
curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/roles" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "'${ROLE_APP}'"
  }'


#  User1
echo "Creating user ${USERNAME}..."
curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "'${USERNAME}'",
    "enabled": true,
    "firstName": "John",
    "lastName": "Doe",
    "email": "JohnDoeI@example.com",
    "emailVerified": true,
    "credentials": [
      {
        "type": "password",
        "value": "'${PASSWORD}'",
        "temporary": false
      }
    ]
  }'


# User2
echo "Creating user ${USERNAME_2}..."
curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "'${USERNAME_2}'",
    "enabled": true,
    "firstName": "Irina",
    "lastName": "Ivanova",
    "email": "Ivanova.I@example.com",
    "emailVerified": true,
    "credentials": [
      {
        "type": "password",
        "value": "'${PASSWORD_2}'",
        "temporary": false
      }
    ]
  }'

# User3
echo "Creating user ${USERNAME_3}..."
curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "'${USERNAME_3}'",
    "enabled": true,
    "firstName": "Vsevolod",
    "lastName": "Scalozuby",
    "email": "Scalozuby.V@example.com",
    "emailVerified": true,
    "credentials": [
      {
        "type": "password",
        "value": "'${PASSWORD_3}'",
        "temporary": false
      }
    ]
  }'


# Set roles to users

ROLE_APP_ID=$(curl -X GET \
 ${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/roles \
   -H "Authorization: Bearer $ACCESS_TOKEN" | jq -r '.[] | select(.name=="'${ROLE_APP}'") | .id')


echo "Set ${ROLE_1} to ${USERNAME}..."

USER_ID=$(curl -X GET \
  ${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users \
  -H "Authorization: Bearer $ACCESS_TOKEN" | jq -r '.[] | select(.username=="'${USERNAME}'") | .id')

echo "${USERNAME} ID: ${USER_ID}"

ROLE_ID=$(curl -X GET \
 ${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/roles \
   -H "Authorization: Bearer $ACCESS_TOKEN" | jq -r '.[] | select(.name=="'${ROLE_1}'") | .id')

echo "${ROLE_1} ID: ${ROLE_ID}"

curl -X POST \
  ${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users/$USER_ID/role-mappings/realm \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '[{"id":"'$ROLE_ID'","name":"'${ROLE_1}'"}]'

curl -X POST \
  ${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users/$USER_ID/role-mappings/realm \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '[{"id":"'$ROLE_APP_ID'","name":"'${ROLE_APP}'"}]'

echo "Set ${ROLE_2} to ${USERNAME_2}..."

USER_ID=$(curl -X GET \
  ${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users \
  -H "Authorization: Bearer $ACCESS_TOKEN" | jq -r '.[] | select(.username=="'${USERNAME_2}'") | .id')

echo "${USERNAME_2} ID: ${USER_ID}"

ROLE_ID=$(curl -X GET \
 ${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/roles \
   -H "Authorization: Bearer $ACCESS_TOKEN" | jq -r '.[] | select(.name=="'${ROLE_2}'") | .id')

echo "${ROLE_2} ID: ${ROLE_ID}"

curl -X POST \
  ${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users/$USER_ID/role-mappings/realm \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '[{"id":"'$ROLE_ID'","name":"'${ROLE_2}'"}]'

curl -X POST \
  ${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users/$USER_ID/role-mappings/realm \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '[{"id":"'$ROLE_APP_ID'","name":"'${ROLE_APP}'"}]'

# checking result

echo "Test user ${USERNAME}, get token..."

curl -v -X POST \
  ${KEYCLOAK_URL}/realms/${REALM_NAME}/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=${CLIENT_ID}" \
  -d "username=${USERNAME}" \
  -d "password=${PASSWORD}" \
  -d "grant_type=password"