# Libro de Clases Digital - Colegio Bernardo O'Higgins

## 📖 Contexto y Objetivo
El Colegio Bernardo O'Higgins necesita modernizar su gestión para solucionar problemas críticos como la dependencia de un libro de clases físico, la dificultad para consultar historiales de estudiantes, la deficiente comunicación con la comunidad y la lentitud en la generación de reportes.

Este proyecto implementa una plataforma digital moderna basada en una arquitectura de microservicios alojada en la nube (AWS), centralizando la información para garantizar un entorno seguro, altamente escalable y accesible.

## 🏗️ Arquitectura del Sistema
El sistema se despliega utilizando un modelo de 3 capas aisladas mediante subredes de AWS para garantizar la máxima seguridad:
* **Capa Web (Pública):** Interactúa con los usuarios y orquesta las peticiones hacia el sistema (API Gateway, BFF).
* **Capa App (Privada):** Aloja los microservicios, inaccesible desde el exterior.
* **Capa de Datos:** Cada microservicio posee su propia base de datos, garantizando aislamiento, privacidad y resiliencia ante fallos.

### Módulos (Microservicios)
La plataforma se ha dividido en 7 microservicios con responsabilidades únicas:
1. **Autenticación y Seguridad:** Gestión de credenciales, permisos y control de acceso no autorizado (JWT).
2. **Perfiles y Usuarios:** Almacenamiento centralizado de la información de todos los usuarios.
3. **Estructura Académica:** Gestión de cursos, asignaturas y horarios de alumnos y profesores.
4. **Gestión de Evaluaciones:** Manejo de notas, promedios y actas semestrales (reemplazo principal del libro físico).
5. **Asistencia y Convivencia:** Control de asistencia, anotaciones y observaciones.
6. **Hub de Comunicación:** Interacción entre docentes y apoderados vía mensajes, videollamadas y notificaciones.
7. **Inteligencia y Reportes:** Generación de métricas y evaluación de conductas basada en datos académicos.

## 💻 Stack Tecnológico y Patrones
* **Backend:** Java, Spring Boot, JPA / Spring Data.
* **Comunicación:** REST (comunicación externa) y gRPC (comunicación interna rápida en binario).
* **Infraestructura:** Docker, Kubernetes (Pods), AWS (Load Balancers, Security Groups).
* **Patrones de Diseño y Resiliencia:** * *API Gateway & BFF (Backend for Frontend)* para optimización de carga.
    * *Repository Pattern* para abstracción de la persistencia de datos sensibles.
    * *Circuit Breaker (Resilience4j)* para evitar fallos en cascada entre servicios.
    * *Factory Method* para la creación dinámica de perfiles.
    * *Saga Pattern* para la coreografía y consistencia de datos en transacciones distribuidas.
    * *CQRS* para separar consultas complejas de las escrituras.

## 📐 Estándares de Desarrollo (Clean Architecture)
Todo nuevo microservicio debe estructurarse siguiendo los principios de Arquitectura Limpia bajo el paquete principal `src/main/java/com/colegio/servicio/`:

| Paquete | Tipo | Responsabilidad Principal |
|---|---|---|
| `controller/` | REST | Endpoints HTTP. Prohibida la lógica de negocio aquí. |
| `service/` | Interfaz + Impl | Lógica de negocio pura. Procesa datos, valida reglas y coordina llamadas. |
| `repository/` | DAO | Interfaces de acceso a datos (`JpaRepository` / `MongoRepository`). |
| `entity/` o `document/`| JPA/Mongo | Mapeo exclusivo para Base de Datos Relacional o NoSQL. |
| `dto/` | Transfer | Intercambio de datos (Request/Response) e Inmutabilidad (Records de Java). |
| `mapper/` | Conversión | Conversión de Entity/Document a DTO utilizando herramientas automáticas. |
| `builder/` | Creación | Construcción de objetos complejos (Patrón Builder). |
| `config/` | Configuración | Configuraciones de Spring, Redis, Base de Datos y Seguridad. |
| `exception/` | Error Handling | Excepciones globales manejadas mediante `@ControllerAdvice`. |
| `validation/` | Validación | Reglas de negocio y validaciones personalizadas. |
| `security/` | Auth/Authz | Configuración de seguridad, tokens y control de roles. |

## 🚀 Guía de Implementación por Fases

### Fase 1: Infraestructura y Core Técnico
* Definir el contrato de comunicación (OpenAPI para REST o archivos `.proto` para gRPC).
* Configurar observabilidad: Health Checks (Spring Boot Actuator) y Tracing (Micrometer).
* Habilitar resiliencia configurando umbrales de `CircuitBreaker`.

### Fase 2: Capa de Persistencia y Modelado de Datos
* **Aislamiento:** Cada servicio es dueño único de su esquema. Prohibido hacer JOINs entre bases de datos distintas.
* **Auditoría:** Implementar clases base (`BaseEntity`) con `@CreatedDate`, `@LastModifiedDate` y `@CreatedBy`.
* **Rendimiento:** Uso estricto de `FetchType.LAZY` en relaciones y definición de índices útiles.
* **Eliminación Segura:** Aplicar *Soft Delete* (columna de estado activo) en lugar de borrados físicos.
* **Versionamiento:** Cambios de esquema gestionados mediante Flyway o Liquibase (Prohibido `ddl-auto: update`).

### Fase 3: Lógica de Negocio
* Separar la definición de la implementación mediante Interfaces.
* Prohibido el uso de `@Autowired`; utilizar inyección por constructor (`@RequiredArgsConstructor` de Lombok).
* Asegurar integridad utilizando `@Transactional` (y `readOnly = true` para métodos de solo lectura).
* Diseñar servicios *Stateless* (sin estado interno). Lanzar excepciones de negocio limpias y personalizadas.

### Fase 4: Transferencia y Mapeo
* Nunca exponer las entidades de base de datos directamente a la API.
* Implementar `RequestDTO` y `ResponseDTO` para modelar la entrada y salida.
* Aplicar validaciones automáticas de Jakarta (`@NotBlank`, `@Email`) en los DTOs de entrada.
* Utilizar **MapStruct** para la conversión rápida y segura entre Capa de Persistencia y Capa de Transferencia.

### Fase 5: Capa de Entrada (Controladores)
* Mantener los controladores libres de lógica de negocio y de cálculos. Su único fin es enrutar, validar y delegar al `Service`.
* Respetar la semántica y los verbos HTTP (GET, POST, PUT, PATCH, DELETE) junto con sus códigos de estado correctos (`200 OK`, `201 Created`, `204 No Content`).
* Implementar seguridad por rutas (`@PreAuthorize`).
* Mantener la API documentada y accesible en `/swagger-ui.html` mediante anotaciones de SpringDoc.

### Fase 6: Calidad y Entrega Continua
* **Pruebas:** Asegurar una cobertura mínima del 80% mediante pruebas unitarias (JUnit 5 + Mockito) y pruebas de integración aisladas (Testcontainers).
* **Contenedores:** Generar imágenes Docker eficientes mediante estrategias *Multi-stage*, definiendo límites estrictos de memoria y CPU.
* **Revisiones:** Validar el cumplimiento de la estructura de paquetes, la aplicación de patrones de diseño (Saga, CQRS, Circuit Breaker) y las reglas de código limpio antes de la integración final.