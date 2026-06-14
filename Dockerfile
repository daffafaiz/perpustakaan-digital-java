# ============ Stage 1: Build ============
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache dependency terlebih dahulu
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

# Build aplikasi
COPY src ./src
RUN mvn -q -B clean package -DskipTests

# ============ Stage 2: Runtime ============
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/perpustakaan-digital.jar app.jar

# Railway menyediakan PORT lewat environment variable
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
