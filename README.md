# PadelZGZ API — AA2

API REST para la gestión de clubes de pádel, reservas de pistas, torneos e inscripciones en Zaragoza.

Desarrollada con **Spring Boot 3.2** + **MariaDB** como Actividad de Aprendizaje de 2ª Evaluación de la asignatura **Acceso a Datos** (DAM, SEAS/Fundación San Valero, curso 2025-2026).

---

## Estado del proyecto

![Postman Tests](https://github.com/paularicarte28/API_Padel_Acceso_Datos_AA2/actions/workflows/postman-tests.yml/badge.svg?branch=develop)

---

## Requisitos de la AA2 implementados

### Obligatorios

| Requisito | Estado | Detalle |
|-----------|--------|---------|
| Versionado de endpoints | ✅ | `/api/v2/clubs` — GET, POST, PUT, DELETE con DTOs enriquecidos y validaciones extra |
| Configuración externalizada (dev/prod) | ✅ | `application-dev.properties` + `application-prod.properties` + variables de entorno |
| Despliegue en AWS | ✅ | EC2 con Docker Compose (ver sección AWS) |
| Tests Postman + GitHub Actions | ✅ | 137 assertions, 96 requests, 0 fallos |
| APIMan | ✅ | API Gateway + Developer Portal con token y políticas de rate limiting |

### Extras implementados

| Extra | Estado | Detalle |
|-------|--------|---------|
| Git Flow | ✅ | Ramas `main`, `develop`, `feature/*` |
| Tests Postman completos | ✅ | Todas las entidades cubiertas con casos de error |
| Docker Compose producción | ✅ | `docker-compose.yml` — API + MariaDB |
| Docker Compose test | ✅ | `docker-compose.test.yml` — entorno efímero en RAM |
| JWT | ✅ | Spring Security + jjwt, token requerido en operaciones de escritura |

---

## Versionado de la API (v2)

Se ha versionado la entidad `Club` introduciendo los siguientes cambios respecto a v1:

| Método | Endpoint v1 | Endpoint v2 | Cambios en v2 |
|--------|------------|------------|---------------|
| GET | `/clubs` | `/api/v2/clubs` | Respuesta con DTO enriquecido: incluye `totalPistas` y `pistasActivas` |
| POST | `/clubs` | `/api/v2/clubs` | Valida duplicados (mismo nombre + ciudad devuelve 409 Conflict) |
| PUT | `/clubs/{id}` | `/api/v2/clubs/{id}` | Valida duplicados al actualizar |
| DELETE | `/clubs/{id}` | `/api/v2/clubs/{id}` | Impide borrar un club con pistas asociadas (devuelve 409 Conflict) |

---

## Entornos

### Desarrollo (por defecto)

```bash
# Arranca con perfil dev (MariaDB en localhost:3307)
mvn spring-boot:run
```

### Producción con Docker Compose

```bash
# Copiar y configurar variables
cp .env.sample .env
# Editar .env con tus valores

# Levantar API + MariaDB
docker compose up -d --build
```

### Entorno de pruebas local (efímero)

```bash
# BD en RAM, se destruye al parar — puerto 8081
docker compose -f docker-compose.test.yml up -d --build

# Lanzar tests contra ese entorno
newman run postman/PadelZGZ_API_RUNNER.postman_collection.json \
  --env-var "baseUrl=http://localhost:8081"

# Limpiar
docker compose -f docker-compose.test.yml down
```

---

## GitHub Actions — Tests automáticos

La colección Postman se ejecuta automáticamente en cada push a `develop` o `main`.

**Resultado actual:** 137 assertions ✅ | 96 requests | 0 fallos

El workflow:
1. Levanta MariaDB como servicio de GitHub Actions
2. Compila el JAR con Maven
3. Arranca la API Spring Boot
4. Ejecuta Newman con la colección completa
5. Sube el reporte HTML como artefacto descargable

---

## AWS — Despliegue

La API está desplegada en una instancia EC2 de AWS Academy (t2.micro, Ubuntu 24.04).

> ⚠️ La instancia puede estar apagada fuera de las sesiones de laboratorio (AWS Academy Learner Lab tiene sesiones de 4h).

**Acceso:**
- API: `http://<EC2_PUBLIC_IP>:8080`
- Swagger UI: `http://<EC2_PUBLIC_IP>:8080/swagger-ui.html`

**Despliegue realizado con:**
```bash
# En la EC2
sudo apt install docker.io docker-compose-plugin -y
git clone https://github.com/paularicarte28/API_Padel_Acceso_Datos_AA2.git
cd API_Padel_Acceso_Datos_AA2
cp .env.sample .env
docker compose up -d --build
```

---

## Autenticación JWT

Las operaciones de escritura requieren token JWT.

**1. Registrar usuario:**
```http
POST /usuarios
Content-Type: application/json

{
  "nombre": "Pau",
  "apellidos": "Ricarte",
  "email": "pau@padelzgz.com",
  "password": "miPassword123",
  "nivel": "AVANZADO"
}
```

**2. Obtener token:**
```http
POST /auth/login
Content-Type: application/json

{
  "email": "pau@padelzgz.com",
  "password": "miPassword123"
}
```

**3. Usar el token:**
```http
Authorization: Bearer <token>
```

---

## Tests de integración (Postman)

La colección está en `postman/PadelZGZ_API_RUNNER.postman_collection.json`.

Cubre las 7 entidades (Usuarios, Clubs, Pistas, Reservas, Torneos, Inscripciones, Valoraciones) con:
- Casos OK (200, 201, 204)
- Casos de error (400, 401, 404, 409)
- Setup automático de datos (crea usuario + login + token antes de cada bloque)

```bash
# Ejecutar localmente
npm install -g newman
newman run postman/PadelZGZ_API_RUNNER.postman_collection.json \
  --env-var "baseUrl=http://localhost:8080"
```

---

## Estructura del proyecto

```
├── .github/workflows/
│   └── postman-tests.yml        # GitHub Action — tests automáticos
├── postman/
│   ├── PadelZGZ_API_RUNNER.postman_collection.json
│   ├── PadelZGZ_CI.postman_environment.json
│   └── *-test-data.csv          # Datos de prueba por entidad
├── src/main/java/com/padelzgz/api/
│   ├── config/                  # SecurityConfig, AppConfig, OpenApiConfig
│   ├── controller/              # Controllers v1 y v2
│   ├── dto/                     # DTOs de entrada/salida
│   ├── exception/               # Excepciones personalizadas
│   ├── model/                   # Entidades JPA
│   ├── repository/              # Repositorios Spring Data
│   ├── security/                # JWT (JwtUtils, JwtAuthFilter)
│   └── service/                 # Servicios e implementaciones
├── src/main/resources/
│   ├── application.properties          # Configuración base
│   ├── application-dev.properties      # Perfil desarrollo
│   └── application-prod.properties     # Perfil producción
├── docker-compose.yml           # Producción: API + MariaDB
├── docker-compose.test.yml      # Tests locales: BD efímera en RAM
├── Dockerfile
└── .env.sample                  # Plantilla de variables de entorno
```

---

## Tecnologías

- Spring Boot 3.2
- Spring Data JPA + Hibernate
- Spring Security + JWT (jjwt 0.11.5)
- MariaDB 11.3
- Lombok
- Springdoc OpenAPI (Swagger UI)
- Docker + Docker Compose
- Newman (Postman CLI)
- GitHub Actions
