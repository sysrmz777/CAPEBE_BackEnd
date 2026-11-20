# CAPEBE Backend — Microservicios Spring Boot + PostgreSQL + Docker

Repositorio unificado del backend para la **Experiencia 3 / Examen Transversal** (Club Aéreo CAPEBE).

## Arquitectura y Puertos

| Microservicio | Carpeta | Puerto API | Base PostgreSQL | Tabla | Puerto BD |
| :--- | :--- | :---: | :--- | :--- | :---: |
| **Vuelos (Rol Socio)** | `service-vuelos/` | `8082` | `capebeBD` | `vuelos` | `5434` |
| **Operaciones Pista** | `service-pista/` | `8083` | `capebeBD` | `operacion_pista` | `5434` |
| **Pases QR en Losa** | `service-qr/` | `8084` | `capebeBD` | `pase_qr` | `5434` |

## Levantar todo el Backend con un solo comando (Local o AWS EC2)

```bash
docker compose up -d --build
```

Esto inicia:
- `postgres_capebe` (`postgres:18-alpine`, base `capebeBD`) en el puerto `5434`
- `capebe_service_vuelos` en el puerto `8082`
- `capebe_service_pista` en el puerto `8083`
- `capebe_service_qr` en el puerto `8084`

## Ejecutar Pruebas Unitarias JUnit 5

Cada microservicio incluye sus pruebas unitarias en JUnit 5 (con H2 en memoria en modo PostgreSQL para ejecutarse sin depender de contenedores):

```bash
cd service-vuelos && ./mvnw test
cd ../service-pista && ./mvnw test
cd ../service-qr && ./mvnw test
```

## Despliegue en AWS EC2
1. Abrir en el **Security Group** de EC2 los puertos TCP `8082`, `8083` y `8084` (y `22` para SSH).
2. Clonar este repositorio en la instancia EC2 y ejecutar `docker compose up -d --build`.
3. En las apps Android (`RetroFitVuelo.kt` y `RetrofitPista.kt`), cambiar `10.0.2.2` por la IP pública de EC2.
