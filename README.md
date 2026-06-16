# PadelZGZ API

API REST para la gestión de clubes de pádel, reservas de pistas, torneos e inscripciones en Zaragoza.

Desarrollada con **Spring Boot 3.2** + **MariaDB** como parte del proyecto intermodular del Ciclo DAM (SEAS/Fundación San Valero, curso 2025-2026).

---

## Requisitos previos

- Java 17 o superior
- Maven 3.8+
- MariaDB 10.6+ (o MySQL 8+)

---

## Configuración de la base de datos

Ejecuta los siguientes comandos en MariaDB:

```sql
CREATE DATABASE padelzgz;
CREATE USER 'padeluser'@'localhost' IDENTIFIED BY 'padelpass';
GRANT ALL PRIVILEGES ON padelzgz.* TO 'padeluser'@'localhost';
FLUSH PRIVILEGES;
```

Spring Boot creará automáticamente las tablas al arrancar gracias a `spring.jpa.hibernate.ddl-auto=update`.

---

## Puesta en marcha

```bash
# Clonar el repositorio
git clone https://github.com/paula.ricarte/padelzgz-api.git
cd padelzgz-api

# Compilar
mvn clean package -DskipTests

# Ejecutar
mvn spring-boot:run
```

La API arranca en `http://localhost:8080`.

---

## Documentación interactiva (Swagger UI)

Una vez arrancada la aplicación:

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

## Autenticación JWT

Las operaciones de escritura (POST, PUT, PATCH, DELETE) sobre clubs, pistas, torneos, usuarios, reservas e inscripciones requieren un token JWT.

**1. Registrar un usuario:**
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

**3. Usar el token en las peticiones:**
```http
Authorization: Bearer <token>
```

---

## Estructura del proyecto

```
src/main/java/com/padelzgz/api/
├── PadelZGZApplication.java
├── config/          # AppConfig (ModelMapper), SecurityConfig
├── controller/      # ClubController, PistaController, UsuarioController,
│                    # ReservaController, TorneoController,
│                    # InscripcionController, ValoracionController, AuthController
├── dto/             # ReservaInDTO, InscripcionInDTO, ClubPistasResumenDTO,
│                    # LoginRequestDTO, LoginResponseDTO
├── exception/       # *NotFoundException, BadRequestException, ErrorResponse
├── model/           # Club, Pista, Usuario, Reserva, Torneo, Inscripcion, Valoracion
├── repository/      # *Repository (JPA + queries nativas)
├── security/        # JwtUtils, JwtAuthFilter, UserDetailsServiceImpl
└── service/         # *Service (interfaz) + impl/*ServiceImpl
```

---

## Endpoints principales

| Método | URL | Descripción | Auth |
|--------|-----|-------------|------|
| GET | /clubs | Listar clubs (filtros: ciudad, activo) | No |
| GET | /clubs/{id} | Obtener club | No |
| GET | /clubs/{id}/pistas-resumen | Resumen pistas con valoración media | No |
| POST | /clubs | Crear club | Sí |
| PUT | /clubs/{id} | Modificar club | Sí |
| PATCH | /clubs/{id} | Modificar club parcialmente | Sí |
| DELETE | /clubs/{id} | Eliminar club | Sí |
| GET | /pistas | Listar pistas (filtros: tipo, interior, activa) | No |
| GET | /pistas/por-precio?min=&max= | Pistas en rango de precio | No |
| GET | /pistas/mejor-valoradas | Pistas con puntuación media ≥ valor | No |
| POST | /pistas/club/{clubId} | Crear pista en un club | Sí |
| GET | /usuarios | Listar usuarios (filtros: nivel, nombre) | Sí |
| POST | /usuarios | Registrar usuario | No |
| POST | /auth/login | Obtener token JWT | No |
| GET | /reservas | Listar reservas (filtros: fecha, pagado) | Sí |
| POST | /reservas | Crear reserva | Sí |
| GET | /torneos | Listar torneos (filtros: inscripcionAbierta, clubId) | No |
| POST | /torneos/club/{clubId} | Crear torneo en un club | Sí |
| GET | /inscripciones | Listar inscripciones (filtros: torneoId, estado) | Sí |
| POST | /inscripciones | Inscribir usuario en torneo | Sí |
| GET | /valoraciones | Listar valoraciones (filtros: pistaId, puntuacion) | No |
| POST | /valoraciones/pista/{pistaId}/usuario/{usuarioId} | Crear valoración | Sí |

---

## Tests

```bash
# Ejecutar todos los tests
mvn test
```

Los tests cubren:
- Capa **Service**: tests unitarios con Mockito para las 5 clases principales
- Capa **Controller**: tests con MockMvc para los casos 200, 201, 400 y 404

---

## Logs

La aplicación genera logs en la carpeta `logs/padelzgz.log` con rotación diaria y límite de 10MB por fichero.

---

## Tecnologías

- Spring Boot 3.2
- Spring Data JPA + Hibernate
- Spring Security + JWT (jjwt 0.11.5)
- MariaDB
- Lombok + ModelMapper
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 + Mockito
