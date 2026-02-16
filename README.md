# Payment Manager 💰

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-Latest-black.svg)](https://kafka.apache.org/)
[![License](https://img.shields.io/badge/License-Personal-yellow.svg)]()
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonale-blueviolet.svg)](https://alistair.cockburn.us/hexagonal-architecture/)

> Une application de gestion de paiements bancaires construite avec l'architecture hexagonale (ports & adapters)

## 📋 Table des matières

- [À propos](#-à-propos)
- [Architecture](#-architecture)
- [Fonctionnalités](#-fonctionnalités)
- [Installation](#-installation)
- [Guide d'utilisation](#-guide-dutilisation)
- [Base de données](#-base-de-données)
- [Technologies](#-technologies)
- [Roadmap](#-roadmap)
- [Contribution](#-contribution)

## 🎯 À propos

Payment Manager est une application backend dédiée à la gestion des opérations bancaires. Elle démontre l'implémentation concrète de l'**architecture hexagonale** avec Spring Boot, garantissant :

- ✅ **Indépendance** vis-à-vis des frameworks
- ✅ **Testabilité** maximale du code métier
- ✅ **Flexibilité** pour changer d'adaptateurs techniques
- ✅ **Maintenabilité** grâce à la séparation des responsabilités

## 📐 Architecture

### Structure des modules

```
payment-manager/
│
├── domain/                      # 🎯 Cœur métier (logique pure)
│   ├── model/                   #    Entités et value objects
│   ├── port/                    #    Interfaces (ports)
│   └── service/                 #    Services métier
│
├── application/                 # 🔄 Orchestration
│   └── usecase/                 #    Cas d'utilisation
│
└── infrastructure/              # 🔌 Adapters techniques
    ├── adapter/
    │   ├── in/web/             #    API REST (Spring MVC)
    │   └── out/
    │       ├── persistence/    #    PostgreSQL JPA
    │       └── messaging/      #    Kafka Producer
    └── config/                  #    Configuration Spring Boot
```

### Diagramme de l'architecture hexagonale

```
                    ┌──────────────────────────────────┐
                    │      REST API Controllers        │
                    │     (Adapter Entrant - Web)      │
                    └────────────┬─────────────────────┘
                                 │ HTTP
                    ┌────────────▼─────────────────────┐
                    │     Application Layer            │
                    │   (Use Cases / Orchestration)    │
                    └────────────┬─────────────────────┘
                                 │
                    ┌────────────▼─────────────────────┐
                    │        Domain Layer              │
                    │   (Business Logic - Ports)       │
                    │                                  │
                    │  • Client Management             │
                    │  • Account Management            │
                    │  • Operation Processing          │
                    └──────┬───────────────┬───────────┘
                           │               │
         ┌─────────────────▼──┐       ┌───▼────────────────────┐
         │   PostgreSQL       │       │   Apache Kafka         │
         │ (Adapter Sortant)  │       │  (Adapter Sortant)     │
         │                    │       │                        │
         │ • Persistence      │       │ • Notifications        │
         │ • Transactions     │       │ • Event Publishing     │
         └────────────────────┘       └────────────────────────┘
```

## 🎯 Fonctionnalités

### Groupes d'API

| Groupe | Responsabilité | Base URL |
|--------|----------------|----------|
| **Client** | Gestion des clients bancaires | `/api/clients/*` |
| **Account** | Gestion des comptes | `/api/account/*` |
| **Operation** | Opérations bancaires | `/api/operations/*` |

### Types d'opérations

#### 1. 💵 Dépôt (Deposit)
Crédite un compte avec un montant spécifié.

#### 2. 💸 Retrait (Withdrawal)
Débite un compte.

#### 3. 🔄 Paiement (Payment)
Transfère des fonds entre deux comptes (débit + crédit).

### Flux des opérations

```
┌─────────────────────────────────────────────────────────────┐
│                  REQUÊTE D'OPÉRATION                         │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│          MISE À JOUR DU/DES SOLDE(S)                         │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│         CRÉATION DE L'ENREGISTREMENT OPÉRATION               │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│       CRÉATION DU/DES DÉTAIL(S) D'OPÉRATION                  │
│  • Opération interne: 1 détail (solde avant/après)          │
│  • Paiement: 2 détails (débit + crédit)                     │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│      ENVOI NOTIFICATION (Kafka) par détail                   │
└─────────────────────────────────────────────────────────────┘
```

### Détails d'opération

**Opérations internes** (dépôt/retrait) :
- ✅ **1 détail** généré
- Contient : `balanceBefore`, `amount`, `balanceAfter`

**Paiements** :
- ✅ **2 détails** générés :
  - 🔴 Débit sur le compte source
  - 🟢 Crédit sur le compte destinataire

## 🚀 Installation

### Prérequis

- ☕ **Java 17+** ([Télécharger](https://adoptium.net/))
- 🐳 **Docker** & **Docker Compose** ([Installer](https://docs.docker.com/get-docker/))
- 🔧 **Maven** (inclus avec le wrapper `./mvnw`)
- 📮 **Postman** (optionnel, pour tester) ([Télécharger](https://www.postman.com/downloads/))

### Étapes d'installation

#### 1️⃣ Cloner le dépôt

```bash
git clone https://github.com/razangue/payment-manager.git
cd payment-manager
```

#### 2️⃣ Démarrer l'infrastructure (PostgreSQL + Kafka)

```bash
docker compose up -d
```

Vérifiez que les services sont actifs :

```bash
docker compose ps
```

Résultat attendu :
```
NAME                STATUS              PORTS
postgres            running             0.0.0.0:5432->5432/tcp
kafka               running             0.0.0.0:9092->9092/tcp
zookeeper           running             0.0.0.0:2181->2181/tcp
```

#### 3️⃣ Compiler et démarrer l'application

```bash
# Compilation
./mvnw clean install

# Démarrage
./mvnw spring-boot:run -pl infrastructure
```

L'application sera accessible sur **http://localhost:8080**

## 📝 Guide d'utilisation

### Scénario complet avec Postman

#### 1️⃣ Créer un client

**Requête :**
```http
POST http://localhost:8080/api/clients/create
Content-Type: application/json

{
  "lastName": "Dupont",
  "firstName": "Marie",
  "birthDate": "1990-03-15",
  "gender": "F",
  "nationality": "France"
}
```

**Réponse attendue (200 OK) :**
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "lastName": "Dupont",
  "firstName": "Marie",
  "birthDate": "1990-03-15",
  "gender": "F",
  "nationality": "France",
  "createdAt": "2026-02-16T10:30:00"
}
```

> 💾 **Important :** Conservez l'UUID généré (ex: `a1b2c3d4-e5f6-7890-abcd-ef1234567890`)

---

#### 2️⃣ Créer deux comptes bancaires

**Compte Source :**
```http
POST http://localhost:8080/api/account/create
Content-Type: application/json

{
  "accountNumber": "FR7612345000",
  "owners": [{
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "birthDate": "1990-03-15",
    "firstName": "Marie",
    "gender": "F",
    "lastName": "Dupont",
    "nationality": "France"
  }]
}
```

**Compte Destinataire :**
```http
POST http://localhost:8080/api/account/create
Content-Type: application/json

{
  "accountNumber": "FR7612345001",
  "owners": [{
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "birthDate": "1990-03-15",
    "firstName": "Marie",
    "gender": "F",
    "lastName": "Dupont",
    "nationality": "France"
  }]
}
```

> ⚠️ **Remplacez** `a1b2c3d4-e5f6-7890-abcd-ef1234567890` par l'UUID du client créé à l'étape 1

**Réponse attendue pour chaque compte :**
```json
{
  "id": "account-uuid-123",
  "accountNumber": "FR7612345000",
  "balance": 0.0,
  "status": "ACTIVE",
  "createdAt": "2026-02-16T10:35:00",
  "owners": [...]
}
```

---

#### 3️⃣ Effectuer un dépôt initial

```http
POST http://localhost:8080/api/operations/deposit
Content-Type: application/json

{
  "accountNumber": "FR7612345000",
  "amount": "5000.0"
}
```

**Réponse attendue (200 OK) :**
```json
{
  "operationId": "op-uuid-456",
  "type": "DEPOSIT",
  "status": "SUCCESS",
  "accountNumber": "FR7612345000",
  "amount": 5000.0,
  "balanceBefore": 0.0,
  "balanceAfter": 5000.0,
  "timestamp": "2026-02-16T10:40:00"
}
```

---

#### 4️⃣ Effectuer un paiement entre comptes

```http
POST http://localhost:8080/api/operations/payment
Content-Type: application/json

{
  "sourceAccountNumber": "FR7612345000",
  "amount": "1500.0",
  "receivingAccountNumber": "FR7612345001"
}
```

**Réponse attendue (200 OK) :**
```json
{
  "operationId": "op-uuid-789",
  "type": "PAYMENT",
  "status": "SUCCESS",
  "sourceAccount": {
    "accountNumber": "FR7612345000",
    "balanceBefore": 5000.0,
    "balanceAfter": 3500.0,
    "amountDebited": 1500.0
  },
  "receivingAccount": {
    "accountNumber": "FR7612345001",
    "balanceBefore": 0.0,
    "balanceAfter": 1500.0,
    "amountCredited": 1500.0
  },
  "timestamp": "2026-02-16T10:45:00"
}
```

---

### 📦 Collection Postman

Téléchargez la collection complète : [Payment Manager API Collection](./postman_collection.json) _(à créer)_

Ou importez directement ce JSON dans Postman :

```json
{
  "info": {
    "name": "Payment Manager API",
    "description": "Collection complète pour tester l'application Payment Manager",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "1. Create Client",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "url": "http://localhost:8080/api/clients/create",
        "body": {
          "mode": "raw",
          "raw": "{\n  \"lastName\": \"Dupont\",\n  \"firstName\": \"Marie\",\n  \"birthDate\": \"1990-03-15\",\n  \"gender\": \"F\",\n  \"nationality\": \"France\"\n}"
        }
      }
    }
  ]
}
```

## 🗄️ Base de données

### Connexion PostgreSQL

| Paramètre | Valeur |
|-----------|--------|
| **Host** | `localhost` |
| **Port** | `5432` |
| **Database** | `payment_db` |
| **Username** | `payment_user` |
| **Password** | `payment_pass` |

### Schéma de base de données

```sql
-- Tables principales
CREATE TABLE clients (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    birth_date DATE,
    gender CHAR(1),
    nationality VARCHAR(50),
    created_at TIMESTAMP
);

CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    account_number VARCHAR(20) UNIQUE,
    balance DECIMAL(15,2),
    status VARCHAR(20),
    created_at TIMESTAMP
);

CREATE TABLE account_owners (
    account_id UUID REFERENCES accounts(id),
    client_id UUID REFERENCES clients(id),
    PRIMARY KEY (account_id, client_id)
);

CREATE TABLE operations (
    id UUID PRIMARY KEY,
    type VARCHAR(20),
    amount DECIMAL(15,2),
    status VARCHAR(20),
    created_at TIMESTAMP
);

CREATE TABLE operation_details (
    id UUID PRIMARY KEY,
    operation_id UUID REFERENCES operations(id),
    account_id UUID REFERENCES accounts(id),
    balance_before DECIMAL(15,2),
    balance_after DECIMAL(15,2),
    amount DECIMAL(15,2),
    notification_sent BOOLEAN,
    created_at TIMESTAMP
);
```

### Clients recommandés

- 🐘 [**DBeaver**](https://dbeaver.io/) (gratuit, multi-plateforme)
- 🛠️ [**pgAdmin**](https://www.pgadmin.org/) (interface web)
- 💻 [**DataGrip**](https://www.jetbrains.com/datagrip/) (JetBrains, payant)

## 🛠️ Technologies

| Composant | Technologie | Version | Rôle |
|-----------|-------------|---------|------|
| **Langage** | Java | 17+ | Backend |
| **Framework** | Spring Boot | 3.x | Application backend |
| **Base de données** | PostgreSQL | 15 | Persistence |
| **Messaging** | Apache Kafka | Latest | Notifications asynchrones |
| **ORM** | Spring Data JPA | 3.x | Mapping objet-relationnel |
| **Build** | Maven | 3.8+ | Gestion des dépendances |
| **Conteneurisation** | Docker Compose | - | Orchestration locale |
| **Architecture** | Hexagonale | - | Découplage et testabilité |

## 🔧 Configuration

### Variables d'environnement

Créez un fichier `.env` à la racine du projet :

```env
# Base de données
DB_HOST=localhost
DB_PORT=5432
DB_NAME=payment_db
DB_USER=payment_user
DB_PASSWORD=payment_pass

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
KAFKA_TOPIC_NOTIFICATIONS=payment-notifications

# Application
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev
```

### Fichier `application.yml`

```yaml
spring:
  application:
    name: payment-manager
  
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:payment_db}
    username: ${DB_USER:payment_user}
    password: ${DB_PASSWORD:payment_pass}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    topic:
      notifications: ${KAFKA_TOPIC_NOTIFICATIONS:payment-notifications}

server:
  port: ${SERVER_PORT:8080}
```

## 🧪 Tests

### Tests unitaires (Domain)

```bash
# Exécuter tous les tests du module domain
./mvnw test -pl domain

# Avec rapport de couverture
./mvnw test jacoco:report -pl domain
```

### Tests d'intégration

```bash
# Tests du module infrastructure
./mvnw verify -pl infrastructure
```

### Architecture tests (ArchUnit)

Vérification de la conformité à l'architecture hexagonale :

```bash
./mvnw test -Dtest=HexagonalArchitectureTest
```

## 🐛 Troubleshooting

### Les services Docker ne démarrent pas

```bash
# Vérifier les logs
docker compose logs postgres
docker compose logs kafka

# Redémarrer proprement
docker compose down -v
docker compose up -d
```

### Erreur de connexion à Kafka

⏳ Kafka a besoin de temps pour s'initialiser (env. 30-60 secondes après `docker compose up`).

```bash
# Vérifier l'état de Kafka
docker compose logs kafka | grep "started"
```

### Port 8080 déjà utilisé

```bash
# Option 1: Trouver et tuer le processus
lsof -i :8080
kill -9 <PID>

# Option 2: Changer le port
SERVER_PORT=8081 ./mvnw spring-boot:run -pl infrastructure
```

### Erreur de compilation Maven

```bash
# Nettoyer et reconstruire
./mvnw clean install -DskipTests

# Si problème de dépendances
./mvnw dependency:purge-local-repository
```

## 🗺️ Roadmap

- [ ] Ajouter des tests unitaires exhaustifs (couverture 80%+)
- [ ] Implémenter l'authentification JWT
- [ ] Ajouter la gestion des devises multiples
- [ ] Créer un tableau de bord web (React/Angular)
- [ ] Ajouter des endpoints de consultation (historique, statistiques)
- [ ] Implémenter le pattern CQRS
- [ ] Déploiement avec Kubernetes
- [ ] CI/CD avec GitHub Actions
- [ ] Monitoring avec Prometheus + Grafana
- [ ] Documentation OpenAPI/Swagger

## 📚 Ressources

### Architecture Hexagonale

- 📖 [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- 🎥 [Clean Architecture par Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- 📝 [Implementing Hexagonal Ports & Adapters](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)

### Spring Boot & Kafka

- 🌱 [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- 🔗 [Spring Kafka Reference](https://spring.io/projects/spring-kafka)
- 📚 [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
