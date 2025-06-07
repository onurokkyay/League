# ---------- Stage 1: Build with Gradle ----------
FROM gradle:8.7-jdk21 AS builder

# Set working directory
WORKDIR /app

# Copy Gradle build scripts and wrapper files first for dependency caching
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Pre-download dependencies (optional cache step)
RUN gradle clean build --no-daemon || return 0

# Copy the rest of the application code
COPY . .

# Build the Spring Boot JAR (skip tests to speed up)
RUN gradle clean build -x test --no-daemon


# ---------- Stage 2: Run with lightweight JRE ----------
FROM eclipse-temurin:21-jre

# Set working directory inside the container
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose application port (change if different)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]