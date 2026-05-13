# Ejecución con Docker Compose

Este compose levanta los elementos solicitados para la Evaluación Parcial 2:

- BFF: `backend-for-frontend`, puerto `8082`.
- Microservicio 1: `autenticacion-seguridad`, puerto `8081`.
- Microservicio 2: `usuarios-perfiles`, puerto `8083`.
- Frontend separado: `frontend-componentes`, puerto `5173`.
- Soporte técnico: Redis para blacklist de tokens y RabbitMQ para eventos de usuarios.

## Comando principal

```bash
docker compose up --build
```

## URLs útiles

- Frontend: http://localhost:5173
- BFF: http://localhost:8082
- Autenticación y seguridad: http://localhost:8081
- Usuarios y perfiles: http://localhost:8083
- RabbitMQ Management: http://localhost:15672

## Notas de desarrollo

- Las llaves RSA incluidas en `docker-compose.yml` son solo para desarrollo y demostración local.
- Los microservicios usan perfil `dev` con H2 en memoria.
- Se agregaron contratos `.proto` mínimos para que el plugin protobuf/gRPC pueda ejecutar la fase de generación durante el empaquetado Maven.
