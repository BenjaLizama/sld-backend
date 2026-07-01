# ETAPA 1: Build con Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
# Ejecutamos el empaquetado (ahora no fallará por el plugin)
RUN mvn clean package -DskipTests

# ETAPA 2: Runtime con Java 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]