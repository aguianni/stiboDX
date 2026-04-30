QUICKSTART - StiboDX User Service
==================================

🚀 Quick Start
===================

## Option 1: Using Docker Compose (Recommended - 2 minutes)

1. Open a terminal in the project folder:
   cd /Users/iannicelli/desarrollo/localRepo/stiboDX

2. Run:
   docker-compose up --build

3. Done! The API is available at:
   http://localhost:8080

4. Database available at:
   localhost:3306

---

## Option 2: Using Make (if you have Make installed)

docker-compose up --build -d  # Start in background
make docker-logs              # View logs

---

## Testing the API

### 1. Create a user
curl -X POST http://localhost:8080/api/users/create \
-H "Content-Type: application/json" \
-H 'Authorization: Bearer token-123' \
-d '{
"email":"new@example.com",
"firstName":"John",
"lastName":"Doe",
"password":"pass123"
}'

### 2. List all users
curl http://localhost:8080/api/users \
-H "Content-Type: application/json" \
-H 'Authorization: Bearer token-123' 

### 3. Get user by ID
curl http://localhost:8080/api/users/1
-H "Content-Type: application/json" \
-H 'Authorization: Bearer token-123'

### 4. Get user by email
curl http://localhost:8080/api/users/email/new@example.com
-H "Content-Type: application/json" \
-H 'Authorization: Bearer token-123' 

### 5. Update user
curl -X PUT http://localhost:8080/api/users/1 \
-H "Content-Type: application/json" \
-H 'Authorization: Bearer token-123' \
-d '{
"firstName":"John Carlos",
"lastName":"Doe Smith"
}'

### 6. Delete user
curl -X DELETE http://localhost:8080/api/users/1
-H "Content-Type: application/json" \
-H 'Authorization: Bearer token-123' 

---

## Stopping the services

docker-compose down              # Stop everything
docker-compose down -v           # Stop and remove data/volumes

---

## Troubleshooting

❌ Port 3306 already in use?
Change in docker-compose.yml:
ports: ["3307:3306"]

❌ Port 8080 already in use?
Change in docker-compose.yml:
ports: ["8081:8080"]

❌ Container won't start?
Check logs: docker-compose logs user-service

---

## Access the Database

docker-compose exec mysql mysql -u stibo_user -pstibo_password stibodx_db

---

For more information, see README.md