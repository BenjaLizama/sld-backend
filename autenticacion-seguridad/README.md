# 🔐 Microservicio de Autenticación y Seguridad

Microservicio encargado de la autenticación, autorización y gestión de sesiones dentro de la arquitectura del proyecto `sld-backend`.

Desarrollado con **Spring Boot 4**, implementa autenticación basada en **JWT**, manejo de sesiones, control de credenciales y soporte para integración mediante **gRPC**.

---

# 📚 Tabla de Contenidos

* [Características](#-características)
* [Tecnologías](#-tecnologías)
* [Arquitectura](#-arquitectura)
* [Estructura del Proyecto](#-estructura-del-proyecto)
* [Variables de Entorno](#-variables-de-entorno)
* [Ejecución Local](#-ejecución-local)
* [Docker](#-docker)
* [Perfiles Spring](#-perfiles-spring)
* [Endpoints](#-endpoints)
* [Autenticación](#-autenticación)
* [gRPC](#-grpc)
* [Testing](#-testing)
* [Seguridad](#-seguridad)
* [Posibles Mejoras](#-posibles-mejoras)

---

# 🚀 Características

* Registro de usuarios
* Inicio de sesión con JWT
* Refresh token
* Logout seguro
* Cambio de contraseña
* Desactivación de cuentas
* Manejo de sesiones
* Roles y privilegios
* Integración con Redis
* Persistencia con JPA/Hibernate
* Integración gRPC
* Limpieza automática de sesiones y cuentas
* Validación de requests
* Manejo global de excepciones
* Tests unitarios e integración

---

# 🛠 Tecnologías

| Tecnología                  | Uso                               |
| --------------------------- | --------------------------------- |
| Java 21                     | Lenguaje principal                |
| Spring Boot 4               | Framework backend                 |
| Spring Security             | Seguridad y autenticación         |
| JWT (JJWT)                  | Tokens de acceso                  |
| Spring Authorization Server | OAuth2/Auth Server                |
| Spring Data JPA             | Persistencia                      |
| PostgreSQL                  | Base de datos principal           |
| Redis                       | Manejo de sesiones/cache          |
| gRPC                        | Comunicación entre microservicios |
| RabbitMQ                    | Mensajería                        |
| Maven                       | Gestión de dependencias           |
| Docker                      | Contenerización                   |
| Lombok                      | Reducción de boilerplate          |

---

# 🧩 Arquitectura

El microservicio sigue una arquitectura basada en capas:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

## Componentes principales

### Controllers

Exponen los endpoints REST.

* `AuthController`
* `SessionController`

### Services

Contienen la lógica de negocio.

* AuthService
* SessionService
* JwtService
* TokenBlacklistService
* CustomUserDetailsService

### Repository

Acceso a datos mediante Spring Data JPA.

### Security

Configuración JWT, filtros de autenticación y autorización.

### Scheduler

Procesos automáticos de limpieza:

* Sesiones expiradas
* Cuentas inactivas

---

# 📁 Estructura del Proyecto

```text
src/
 ├── main/
 │    ├── java/
 │    │     └── com/promptlabs/autenticacion_seguridad/
 │    │            ├── config/
 │    │            ├── controller/
 │    │            ├── dto/
 │    │            ├── entity/
 │    │            ├── enums/
 │    │            ├── exception/
 │    │            ├── mapper/
 │    │            ├── repository/
 │    │            ├── security/
 │    │            ├── service/
 │    │            └── util/
 │    │
 │    ├── proto/
 │    │     └── authentication_security.proto
 │    │
 │    └── resources/
 │          ├── application.yaml
 │          ├── application-dev.yaml
 │          ├── application-test.yaml
 │          └── application-prod.yaml
 │
 └── test/
```

---

# ⚙ Variables de Entorno

El proyecto utiliza variables de entorno para la configuración.

## Variables principales

```env
DEV_PORT=8080

RSA_PRIVATE=-----BEGIN PRIVATE KEY-----
RSA_PUBLIC=-----BEGIN PUBLIC KEY-----

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/authdb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password

SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379
```

## Configuración JWT

El sistema utiliza claves RSA para firmar y validar tokens JWT.

* `RSA_PRIVATE`
* `RSA_PUBLIC`

---

# ▶ Ejecución Local

## Requisitos

* Java 21
* Maven
* PostgreSQL
* Redis

## Clonar repositorio

```bash
git clone https://github.com/BenjaLizama/sld-backend.git
```

## Entrar al microservicio

```bash
cd autenticacion-seguridad
```

## Ejecutar aplicación

### Linux/Mac

```bash
./mvnw spring-boot:run
```

### Windows

```bash
mvnw.cmd spring-boot:run
```

---

# 🐳 Docker

## Build imagen

```bash
docker build -t auth-service .
```

## Ejecutar contenedor

```bash
docker run -p 8080:8080 auth-service
```

---

# 🌱 Perfiles Spring

| Perfil | Descripción      |
| ------ | ---------------- |
| dev    | Desarrollo local |
| test   | Testing          |
| prod   | Producción       |

Perfil activo por defecto:

```yaml
spring:
  profiles:
    active: dev
```

---

# 📡 Endpoints

Base URL:

```text
/api/v1
```

---

## 🔐 Auth

### Registro

```http
POST /api/v1/auth/register
```

### Login

```http
POST /api/v1/auth/login
```

### Ejemplo Request

```json
{
  "email": "user@email.com",
  "password": "password123"
}
```

### Ejemplo Response

```json
{
  "accessToken": "jwt-token",
  "refreshToken": "refresh-token"
}
```

---

## ♻ Refresh Token

```http
POST /api/v1/session/refresh-token
```

---

## 🚪 Logout

```http
POST /api/v1/session/logout
```

---

## 🔑 Cambio de contraseña

```http
POST /api/v1/session/change-password
```

---

## ❌ Desactivar cuenta

```http
PATCH /api/v1/session/deactivate
```

---

# 🔒 Autenticación

La autenticación está basada en JWT.

## Flujo

1. Usuario inicia sesión
2. El sistema genera:

    * Access Token
    * Refresh Token
3. El Access Token se envía en cada request:

```http
Authorization: Bearer <token>
```

4. Cuando el token expira:

    * se utiliza el refresh token
    * se genera un nuevo access token

---

# ⚡ gRPC

El microservicio incluye soporte para gRPC.

Archivo proto:

```text
src/main/proto/authentication_security.proto
```

Generación automática mediante:

```xml
protobuf-maven-plugin
```

---

# 🧪 Testing

El proyecto incluye:

* Tests unitarios
* Tests de integración
* Tests de seguridad
* Tests de repositories
* Tests de controllers

## Ejecutar tests

```bash
./mvnw test
```

---

# 🛡 Seguridad

## Características implementadas

* JWT firmado con RSA
* Spring Security
* Blacklist de tokens
* Validaciones de entrada
* Roles y privilegios
* Manejo seguro de sesiones
* Filtros personalizados
* Limpieza automática de sesiones

---

# 📅 Jobs Programados

## Limpieza de sesiones

```yaml
app:
  cleanup:
    session:
      cron: "0 0 * * * ?"
```

## Limpieza de cuentas

```yaml
app:
  cleanup:
    account:
      cron: "0 0 3 * * ?"
      grace-period-days: 30
```

---



