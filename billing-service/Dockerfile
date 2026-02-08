# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Build application
COPY src ./src
RUN mvn -B clean package

# ---------- Runtime Stage ----------
FROM gcr.io/distroless/java21-debian12
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

USER nonroot
EXPOSE 4001 9001

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-XX:+UseContainerSupport","-jar","app.jar"]
