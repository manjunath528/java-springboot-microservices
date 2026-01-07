# Java Spring Boot Microservices Project

This repository contains a set of **Java Spring Boot microservices** integrated with **PostgreSQL**, **Kafka**, and deployed via **Kubernetes**. Docker images are built and pushed to **Docker Hub**, and the services can run locally on **Minikube** or in a production Kubernetes cluster.

---

## 📦 Project Structure

```plaintext

java-springboot-microservices/
├── .github/                       # GitHub Actions workflows
│   └── workflows/
│       └── build-and-push.yml     # CI/CD pipeline for Docker images
├── .gitignore                      # Ignore actions-runner, target, logs, large files
├── actions-runner/                 # Local self-hosted runner (ignored in git)
├── k8s/                            # Kubernetes manifests and Helm values
│   ├── namespace.yaml              # Namespace definition
│   ├── secrets/                    # Optional: db secrets, kafka secrets as YAML
│   ├── prerequisites/              # Dependencies like DB & Kafka
│   │   ├── postgres/
│   │   │   ├── auth-db-values.yaml
│   │   │   ├── billing-db-values.yaml
│   │   │   ├── notification-db-values.yaml
│   │   │   ├── nutrition-db-values.yaml
│   │   │   ├── user-db-values.yaml
│   │   │   └── workout-db-values.yaml
│   │   └── kafka/
│   │       ├── kafka-deployment.yaml
│   │       ├── kafka-service.yaml
│   │       ├── zookeeper-deployment.yaml
│   │       └── zookeeper-service.yaml
│   └── microservices/             # Deployments + Services for each microservice
│       ├── api-gateway.yaml
│       ├── auth-service.yaml
│       ├── user-service.yaml
│       ├── billing-service.yaml
│       ├── notification-service.yaml
│       ├── nutrition-service.yaml
│       ├── workout-service.yaml
│       └── analytics-service.yaml
├── api-gateway/                   # Spring Boot service folder
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── auth-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── user-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── billing-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── notification-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── nutrition-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── workout-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
└── analytics-service/
    ├── src/
    ├── Dockerfile
    └── pom.xml

```
