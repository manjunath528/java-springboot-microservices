# -------- Build Stage --------
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source and build (run tests)
COPY src ./src
RUN mvn -B clean package

# -------- Runtime Stage --------
FROM gcr.io/distroless/java21-debian12
WORKDIR /app

# Copy only the JAR from build stage
COPY --from=builder /app/target/workout-service-0.0.1-SNAPSHOT.jar app.jar

USER nonroot
EXPOSE 4006

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-XX:+UseContainerSupport","-jar","app.jar"]
