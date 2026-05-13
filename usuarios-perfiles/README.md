# Microservicio de Usuarios y Perfiles 👤

Este microservicio forma parte de la arquitectura distribuida del proyecto. Se encarga de la gestión, persistencia y lógica de negocio relacionada con los perfiles de usuario, integrándose de manera asíncrona con el resto del sistema.

## 🚀 Tecnologías Utilizadas

* **Java 21** (LTS)
* **Spring Boot 4.0.5**
* **Spring Data JPA** (Persistencia en PostgreSQL/H2)
* **RabbitMQ** (Message Broker para comunicación orientada a eventos)
* **Lombok** (Productividad y patrones)
* **Maven** (Gestor de dependencias)
* **Docker & Docker Compose** (Containerización)

## 🏗️ Patrones de Diseño Implementados

Para garantizar la escalabilidad y mantenibilidad del código, se aplicaron los siguientes patrones:

1.  **Patrón Builder:** Utilizado para la construcción de objetos complejos como `UserEntity` y `UserDTO`. Esto permite instanciar usuarios con múltiples atributos opcionales de forma clara y sin errores de posicionamiento en el constructor.
2.  **Patrón Strategy:** Implementado para manejar las diferentes lógicas de negocio según el tipo de perfil (ADMIN, USER, etc.). Esto permite que el sistema sea extensible (Open/Closed Principle), facilitando la adición de nuevos tipos de perfiles sin modificar la lógica core.
3.  **Repository Pattern:** Desacoplamiento de la lógica de acceso a datos de la lógica de negocio.



## 📩 Comunicación e Integración

El microservicio actúa principalmente como un **Consumidor (Consumer)** en nuestra arquitectura orientada a eventos:

* **RabbitMQ:** Escucha eventos provenientes del microservicio de Seguridad (ej. `user.created`).
* **Deserialización:** Utiliza `JacksonJsonMessageConverter` para transformar automáticamente los mensajes JSON recibidos en objetos Java.
* **Flujo:** Al recibir un evento de nuevo registro, el servicio procesa la información y crea el perfil correspondiente utilizando las estrategias definidas.



## 🛠️ Configuración y Ejecución

### Requisitos Previos
* Docker y Docker Compose instalados.

### Instalación
1. Clonar el repositorio.
2. Navegar a la carpeta del microservicio:
   ```bash
   cd usuarios-perfiles