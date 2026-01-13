# 🌐 Java Spring Boot Microservices – End-to-End Micoroserives and DevOps Project

This project is a **real-world, production-style microservices platform** built using **Java Spring Boot** and deployed using a complete **DevOps CI/CD pipeline**.

It demonstrates how modern backend systems are:
- Designed as independent microservices
- Containerized with Docker
- Orchestrated with Kubernetes
- Integrated using Kafka
- Automated using GitHub Actions

This repository is intentionally structured to reflect **industry-grade practices**, not a demo or toy setup.

## 🖥️ High Level Architecture
This is the high level system design diagram:

![Image](https://github.com/user-attachments/assets/852940d1-9b83-4090-bf78-97c3f26d1e8a)

---

## 🎯 Purpose of This Project

The goal of this project is to:

- Learn **microservices architecture** the right way
- Understand **CI/CD pipelines from scratch**
- Practice **Kubernetes deployments and rolling updates**
- Implement **database-per-service** pattern
- Use **event-driven communication with Kafka**
- Simulate a **production environment locally using Minikube**

This project answers the question:

> *“How does real production code move from GitHub → Docker → Kubernetes automatically?”*

---


---

## 🧩 Microservices Explained

Each microservice is:

- A **separate Spring Boot application**
- Owns **its own database**
- Built & deployed **independently**
- Communicates via **REST or Kafka events**

### List of Services

| Service | Responsibility |
|-------|----------------|
| api-gateway | Single entry point for all client requests |
| auth-service | Authentication & authorization |
| user-service | User profile & management |
| billing-service | Payments & billing logic |
| notification-service | Email / notification events |
| nutrition-service | Nutrition data |
| workout-service | Workout tracking |
| analytics-service | Reporting & analytics |

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


Each microservice directory contains:
- Spring Boot source code
- Dockerfile
- application configuration

---

## 🐳 Docker Strategy (How Containers Are Built)

Each microservice:
- Has its **own Dockerfile**
- Uses **multi-stage builds**
- Produces **lightweight production images**

### Image Naming Convention

```
bmanjunath***/prod-<service-name>:<git-sha>

```


Using **Git SHA** ensures:
- Immutable builds
- Full traceability
- Safe rollbacks

---

## 🔄 CI Pipeline (Continuous Integration)

### Tool: GitHub Actions

### Trigger
```yaml
on:
  push:
    branches:
      - main
```

### What CI Does

1. Checks out source code
2. Builds Docker images for all services
3. Tags images with Git commit SHA
4. Pushes images to Docker Hub

### Why CI Is Important

1. Ensures every commit produces deployable artifacts
2. Prevents “works on my machine” problems
3. Guarantees consistent builds

## 🚀 CD Pipeline (Continuous Deployment)
### Tooling

- GitHub Actions
- kubectl
- Kubernetes (Minikube)

### How Deployment Works

1. Pipeline configures kubectl using a kubeconfig secret
2. Uses kubectl set image to update deployments
3. Kubernetes performs rolling updates
4. Old pods are replaced with new ones safely
### Example
```
kubectl set image deployment/user-service \
  user-service=bmanjunath***/prod-user-service:<TAG>
```

## ☸️ Kubernetes Architecture
### Namespace
- All components run in a dedicated namespace:
```
kubectl create namespace microservices
```

### Kubernetes Objects Used :
1. Deployments
2. Services
3. Secrets
4. ConfigMaps
5. Namespaces
   
### Each microservice has:
- One Deployment
- One Service
- One Database

## 🐘 PostgreSQL (Database-Per-Service)

### Why Database Per Service?
- Loose coupling
- Independent scaling
- No shared schema problems

### How Databases Are Deployed
- PostgreSQL is installed using Helm
- Each service has its own values file
- Example:
```
helm install user-db bitnami/postgresql \
  -f user-db-values.yaml \
  -n microservices
```

## 📨 Kafka (Event-Driven Communication)

### Kafka is used for:
- Asynchronous messaging
- Event propagation
- Loose service coupling
- Examples:
```
User registered → Notification service

Payment completed → Analytics service
```

### Kafka components:
- Zookeeper
- Kafka broker

## 🔐 Secrets & Configuration

Sensitive data is never committed to Git.

### Stored using:
- Kubernetes Secrets
- GitHub Actions Secrets
- Examples:
   1. Database credentials
   2. Docker Hub credentials
   3. kubeconfig
      
## 🧪 Local Development & Testing

### Requirements
- Java 17+
- Docker
- Minikube
- kubectl
- Helm

### Start Environment
```
minikube start
kubectl apply -f k8s/namespace.yaml
```

## 🔁 Complete Flow (Code → Production)
```
Developer pushes code
        ↓
GitHub Actions CI runs
        ↓
Docker images built
        ↓
Images pushed to Docker Hub
        ↓
CD pipeline updates Kubernetes
        ↓
Rolling updates replace pods
        ↓
New version goes live

```

## 🧹 Git Hygiene

### Ignored files:
- actions-runner
- node binaries
- large archives
- secrets
- kubeconfig files
- Handled via .gitignore.

## 👨‍💻 Author

### Manjunath Reddy
#### Java | Spring Boot | Docker | Kubernetes | DevOps




