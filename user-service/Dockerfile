# -------- Build Stage --------
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# install curl
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY wait-for-billing.sh /wait-for-billing.sh
RUN chmod +x /wait-for-billing.sh

COPY --from=builder /app/target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar

EXPOSE 4000

ENTRYPOINT ["/wait-for-billing.sh"]
CMD ["http://billing-service:4001", "java", "-jar", "/app/user-service.jar"]
