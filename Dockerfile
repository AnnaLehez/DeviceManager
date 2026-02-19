# ==========================================
# STAGE 1: Build the application
# ==========================================
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml first (for caching dependencies)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Give execution rights to the Maven wrapper
RUN chmod +x ./mvnw

# Download dependencies (this layer is cached so future builds are faster)
RUN ./mvnw dependency:go-offline

# Copy the actual source code and build the app
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ==========================================
# STAGE 2: Run the application
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy ONLY the built jar from the 'builder' stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]