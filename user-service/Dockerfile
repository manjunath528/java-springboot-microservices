# -------- Build Stage --------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src

RUN mvn clean package -DskipTests


# -------- Runtime Stage --------
FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

# Copy only the JAR from build stage
COPY --from=builder /app/target/user-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "app.jar"]
