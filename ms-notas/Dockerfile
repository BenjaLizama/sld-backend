# 1. Usamos una imagen base oficial de Java 17 (versión Alpine, que es súper liviana)
FROM eclipse-temurin:17-jdk-alpine

# 2. Le decimos a Docker en qué carpeta interna vamos a trabajar
WORKDIR /app

# 3. Copiamos el archivo .jar que genera tu proyecto al contenedor y lo renombramos a app.jar
# (Nota: Antes de construir la imagen, debes ejecutar 'mvn clean package' en tu terminal)
COPY target/*.jar app.jar

# 4. Exponemos el puerto para que el mundo exterior pueda comunicarse con el contenedor
EXPOSE 8084

# 5. El comando que se ejecutará cuando el contenedor se encienda
ENTRYPOINT ["java", "-jar", "app.jar"]