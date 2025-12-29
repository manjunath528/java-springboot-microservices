# -------- Build Stage --------
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# -------- Runtime Stage --------
FROM gcr.io/distroless/java21-debian12
WORKDIR /app

COPY --from=builder /app/target/user-service-0.0.1-SNAPSHOT.jar app.jar

USER nonroot
EXPOSE 4000

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-XX:+UseContainerSupport","-jar","app.jar"]
