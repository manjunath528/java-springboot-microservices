## ---------- Build Stage ----------
#FROM maven:3.9.9-eclipse-temurin-21 AS builder
#WORKDIR /app
#
## Download dependencies first (better caching)
#COPY pom.xml .
#RUN mvn -B dependency:go-offline
#
## Build application
#COPY src ./src
#RUN mvn -B clean package -DskipTests
#
## ---------- Runtime Stage ----------
#FROM gcr.io/distroless/java21-debian12
#WORKDIR /app
#
#COPY --from=builder /app/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar
#
#USER nonroot
#EXPOSE 4004
#
#ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-XX:+UseContainerSupport","-jar","app.jar"]
##
# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the built jar and wait script
COPY --from=builder /app/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-services.sh /wait-for-services.sh

# Make wait script executable
RUN chmod +x /wait-for-services.sh

USER 1000
EXPOSE 4004

# Run wait script before starting service
ENTRYPOINT ["/wait-for-services.sh", "http://auth-service:4005", "http://user-service:4000", "http://workout-service:4006", "http://nutrition-service:4009", "http://notification-service:4010", "java", "-XX:MaxRAMPercentage=75.0", "-XX:+UseContainerSupport", "-jar", "app.jar"]