.PHONY: help build run stop clean logs docker-build docker-up docker-down test

help:
	@echo "StiboDX - User Service API"
	@echo "==========================="
	@echo ""
	@echo "Comandos disponibles:"
	@echo ""
	@echo "  docker-build    - Construir imagen Docker"
	@echo "  docker-up       - Iniciar servicios con Docker Compose"
	@echo "  docker-down     - Detener servicios Docker Compose"
	@echo "  docker-logs     - Ver logs de los servicios"
	@echo ""
	@echo "  build           - Compilar el proyecto con Maven"
	@echo "  run             - Ejecutar la aplicación localmente"
	@echo "  clean           - Limpiar artifacts del proyecto"
	@echo "  test            - Ejecutar tests"
	@echo ""
	@echo "  db-init         - Inicializar base de datos (requiere MySQL local)"
	@echo ""

# Docker targets
docker-build:
	docker-compose build

docker-up:
	docker-compose up --build -d
	@echo "Esperando que los servicios estén listos..."
	sleep 5
	@echo "✓ Servicios iniciados"
	@echo "✓ API: http://localhost:8080"
	@echo "✓ MySQL: localhost:3306"

docker-down:
	docker-compose down

docker-down-volumes:
	docker-compose down -v

docker-logs:
	docker-compose logs -f

docker-logs-app:
	docker-compose logs -f user-service

docker-logs-db:
	docker-compose logs -f mysql

# Local build targets
build:
	mvn clean package

run: build
	java -jar target/quarkus-app/quarkus-run.jar

dev:
	mvn quarkus:dev

clean:
	mvn clean

test:
	mvn test

# Database targets
db-init:
	@echo "Inicializando base de datos MySQL..."
	mysql -h localhost -u root -p < init.sql
	@echo "✓ Base de datos inicializada"

# Utility targets
ps:
	docker-compose ps

shell-app:
	docker-compose exec user-service /bin/sh

shell-db:
	docker-compose exec mysql mysql -u stibo_user -pstibo_password stibodx_db

# API Testing examples
test-create-user:
	curl -X POST http://localhost:8080/api/usuarios/crear \
		-H "Content-Type: application/json" \
		-d '{"email":"test@example.com","nombre":"Test","apellido":"User","password":"pass123"}'

test-list-users:
	curl http://localhost:8080/api/usuarios/todos

test-get-user:
	curl http://localhost:8080/api/usuarios/1

.DEFAULT_GOAL := help

