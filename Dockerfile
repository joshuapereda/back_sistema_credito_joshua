# ---------- Etapa 1: compilar el JAR ----------
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Descargar dependencias primero para aprovechar la caché de Docker
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q package -DskipTests

# ---------- Etapa 2: imagen final solo con el JRE ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 9600
ENTRYPOINT ["java", "-jar", "app.jar"]
