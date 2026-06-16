# Imagen base con Java 17 para ejecutar la API
FROM eclipse-temurin:17-jdk

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el jar generado por Maven
COPY target/*.jar app.jar

# Puerto interno de la API
EXPOSE 8080

# Ejecutamos la aplicación usando el perfil de producción
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]