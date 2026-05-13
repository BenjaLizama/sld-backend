# Backend For Frontend (BFF) - API Gateway 🚀

Este componente actúa como la capa de orquestación y agregación de nuestra arquitectura de microservicios. Su objetivo principal es servir como un punto de entrada único para las aplicaciones cliente (Frontend), simplificando la comunicación y optimizando las peticiones.

## 🏗️ Rol en la Arquitectura

El BFF implementa el patrón de **Agregación de Servicios**, permitiendo que el frontend obtenga información de múltiples microservicios (Seguridad y Usuarios) mediante una única llamada, reduciendo el "chattiness" de la red.



## 🛠️ Tecnologías Utilizadas

* **Java 21**
* **Spring Boot 4.0.5**
* **Spring Web** (Rest Template / WebClient para comunicación síncrona)
* **Docker**
* **Maven**

## 🔧 Funcionalidades de Orquestación

1.  **Agregación de Datos:** Centraliza las respuestas de los microservicios de `Autenticación-Seguridad` y `Usuarios-Perfiles`.
2.  **Abstracción de Endpoints:** Oculta la complejidad y la ubicación física de los microservicios core.
3.  **Seguridad:** Actúa como proxy para la validación de tokens JWT antes de redirigir las peticiones a los servicios internos.



## 🔄 Flujo de Comunicación

El BFF utiliza comunicación síncrona (REST) para consultar los microservicios internos, complementando la naturaleza asíncrona de RabbitMQ utilizada en el backend:

1.  **Cliente** -> Petición HTTP -> **BFF**.
2.  **BFF** -> Consulta **MS Seguridad** (Validación).
3.  **BFF** -> Consulta **MS Usuarios** (Datos de perfil).
4.  **BFF** -> Respuesta consolidada -> **Cliente**.

## 🛠️ Ejecución y Despliegue

### Requisitos
* Docker instalado.
* Microservicios de Seguridad y Usuarios en ejecución.

### Compilación
```bash
mvn clean package -Dmaven.test.skip=true