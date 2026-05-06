#  Distributed Bidding Engine

A **high-performance, distributed auction and bidding system** with multiple bidding strategies, designed to handle concurrent bids efficiently. The system simulator allows comprehensive testing of different bidding mechanisms with real-time performance metrics.

---

##  Project Overview

The **Bidding Engine** is the core simulator of the project, supporting multiple bidding strategies to handle concurrent auction operations. Combined with the **User Service**, **Product Service**, and **API Gateway**, this forms a complete distributed bidding system.

###  Key Highlights

- ** Bidding Strategy Simulator**: Test and compare three distinct bidding strategies
- ** High-Concurrency Support**: Handle thousands of simultaneous bids
- ** Real-Time Performance Metrics**: Monitor bid success rates, conflicts, and latency
- ** Distributed Architecture**: Microservices-based design with independent scaling
- ** Cloud-Native**: Deployed on Railway with Neon PostgreSQL and Upstash Redis

---

##  Architecture

### System Components

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend (Angular 21)                     │
│              https://www.bidzapp.tech/                       │
└─────────────┬───────────────────────────────────────────────┘
              │
┌─────────────▼───────────────────────────────────────────────┐
│            API Gateway (Spring Boot 4.0.5)                   │
│           Route & Load Balance Requests                      │
└─────────────┬───────────────────────────────────────────────┘
              │
    ┌─────────┼─────────┬──────────────┐
    │         │         │              │
    │         │         │              │
    ▼         ▼         ▼              ▼
  User     Product   Bidding Engine   (Other Services)
 Service   Service    (Simulator)
    │         │         │              │
    └─────────┴─────────┴──────────────┘
              │
    ┌─────────┴─────────┬──────────────┐
    │                   │              │
    ▼                   ▼              ▼
  Neon PostgreSQL   Upstash Redis   Database
  (Distributed DB)  (Cache & Locks)  Services
```

### Microservices Overview

| Service | Purpose | Tech Stack |
|---------|---------|-----------|
| **Bidding Engine** | Core auction & bidding logic with 3 strategies | Spring Boot 4.0.5, Java 21, PostgreSQL, Redis |
| **User Service** | User authentication & management | Spring Boot 4.0.5, Java 21, PostgreSQL |
| **Product Service** | Product catalog & inventory | Spring Boot 4.0.5, Java 21, PostgreSQL |
| **API Gateway** | Request routing & load balancing | Spring Boot 4.0.5, Java 21 |
| **Frontend** | Web UI for auction management | Angular 21, Material Design |

---
### Note

- Please configure the product , user , api-gateway services if u want a fully distributed bidding engine 
- As right now it only simulates the amount of concurrent users that u need on 
different locking strategy

---
## 🎲 Bidding Strategies (Simulator)

The **Bidding Engine Simulator** implements three different bidding strategies to test system behavior under concurrent load:

### 1. **Redis Lock Bidding Strategy** 🔒
- Uses Redis-based distributed locks for synchronized bid processing
- **Best for**: Ensuring strict ACID compliance in high-concurrency scenarios
- Serializes bid processing but guarantees data consistency
- Lower throughput, higher consistency

### 2. **Optimistic Locking Strategy** 
- Uses database-level version control for conflict detection
- **Best for**: High-throughput scenarios with manageable conflict rates
- Allows concurrent reads but validates on write
- Higher throughput with retry mechanisms

### 3. **Naive Bidding Strategy** 
- Direct bid placement without explicit locking
- **Best for**: Baseline performance comparison
- Fastest but susceptible to race conditions
- Useful for identifying concurrency issues

### Simulator Features
- **Concurrent Bid Simulation**: Simulate multiple users placing bids simultaneously
- **Performance Metrics**: Track success rates, response times, and conflicts
- **Real-Time Statistics**: Monitor bid statistics (min, max, average bids)
- **Strategy Comparison**: A/B test different strategies on the same auction

---

## 📦 Project Structure

```
BiddingEngine/
├── fronted/                          # Angular Frontend Application (10%)
│   ├── src/
│   │   ├── app/
│   │   │   ├── bidding-api.service.ts        # API communication service
│   │   │   ├── bidding.models.ts             # Bidding data models
│   │   │   ├── feature/
│   │   │   │   └── bidding-dashboard/        # Auction dashboard UI
│   │   │   ├── shared/                       # Shared components & utilities
│   │   │   └── core/                         # Core services & interceptors
│   │   └── index.html
│   ├── package.json
│   └── angular.json
│
├── biddingengine/                   #  Core Bidding Engine Service (60%)
│   ├── src/main/java/com/emi/biddingengine/
│   │   ├── BiddingengineApplication.java    # Spring Boot entry point
│   │   ├── controller/
│   │   │   ├── AuctionController.java       # Auction REST endpoints
│   │   │   └── BidController.java           # Bidding REST endpoints
│   │   ├── services/
│   │   │   ├── AuctionService.java          # Auction business logic
│   │   │   └── BidService.java              # Bidding logic (3 strategies)
│   │   ├── serviceImpl/                      # Strategy implementations
│   │   ├── entity/                          # JPA entities
│   │   ├── repository/                      # Data persistence layer
│   │   ├── dtos/                            # Request/Response DTOs
│   │   ├── config/                          # Spring configuration
│   │   └── client/                          # External service clients
│   ├── src/main/resources/
│   │   ├── db/migration/                    # Flyway DB migrations
│   │   ├── application.properties           # Configuration
│   │   └── static/, templates/
│   ├── Dockerfile                           # Multi-stage Docker build
│   ├── pom.xml
│   └── target/
│
├── api-gateway/                     # API Gateway Service (10%)
│   ├── src/main/java/com/emi/api_gateway/
│   ├── pom.xml
│   └── mvnw
│
├── user/                            # User Service (10%)
│   ├── src/main/java/com/emi/user/
│   ├── src/main/resources/db/migration/
│   ├── pom.xml
│   └── mvnw
│
├── product/                         # Product Service (10%)
│   ├── src/main/java/com/emi/product/
│   ├── src/main/resources/db/migration/
│   ├── pom.xml
│   └── mvnw
│
├── Docker/
│   ├── docker-compose.yml           # Local development environment
│   └── infra/                       # Infrastructure setup
│
├── railway.json                     # Railway deployment configuration
└── README.md
```

---

##  Quick Start

### Prerequisites
- Java 21+
- Maven 3.9.6+
- PostgreSQL 15+ (or use Neon)
- Redis (or use Upstash)
- Node.js 18+ (for frontend)
- Angular CLI 21+

### Local Development with Docker Compose

```bash
# Start PostgreSQL and Redis
cd Docker
docker-compose up -d

# Build all services
cd ../biddingengine
mvn clean package -DskipTests

cd ../user
mvn clean package -DskipTests

cd ../product
mvn clean package -DskipTests

cd ../api-gateway
mvn clean package -DskipTests
```

### Run Services Individually

**Bidding Engine:**
```bash
cd biddingengine
mvn spring-boot:run
# Runs on http://localhost:8080
```

**User Service:**
```bash
cd user
mvn spring-boot:run
# Runs on http://localhost:8081
```

**Product Service:**
```bash
cd product
mvn spring-boot:run
# Runs on http://localhost:8082
```

**API Gateway:**
```bash
cd api-gateway
mvn spring-boot:run
# Runs on http://localhost:8000
```

**Frontend (Angular):**
```bash
cd fronted
npm install
npm start
# Runs on http://localhost:4200
```

---

##  Cloud Deployment

### Production Environment

| Component | Platform | URL |
|-----------|----------|-----|
| **Frontend** | Hosted CDN | [https://www.bidzapp.tech/](https://www.bidzapp.tech/) |
| **Bidding Engine** | Railway | https://distributed-bidding-engine-production.up.railway.app/ |
| **Database** | Neon PostgreSQL Cluster | Serverless, auto-scaling |
| **Cache/Locks** | Upstash Redis | Managed, distributed |

### Infrastructure Components

**PostgreSQL (Neon)**
- Serverless, auto-scaling PostgreSQL cluster
- Connection pooling for optimal performance
- SSL/TLS encryption enabled
- Multi-region replication available

**Redis (Upstash)**
- Distributed Redis for bid locks and caching
- Used by Redis Lock Bidding Strategy
- High availability with automatic failover
- Real-time data consistency

**Railway Deployment**
- Container-based deployment via Docker
- Auto-scaling capabilities
- Environment variables for configuration
- Health checks and monitoring

---

##  Using the Bidding Simulator

The frontend provides an intuitive interface to test bidding strategies:
(though only 3 stratergies for now)
### 1. Create an Auction
- Select a product from the catalog
- Set starting price and duration
- Auction goes live immediately

### 2. Simulate Concurrent Bids
```
Simulation Parameters:
├── Number of Users: Simulate N concurrent bidders
├── Bidding Strategy: Choose [Redis Lock | Optimistic Lock | Naive]
└── Duration: Run for specified time period

Results Display:
├── Success Rate: % of bids successfully processed
├── Conflict Rate: % of conflicts detected (for optimistic lock)
├── Avg Response Time: Average bid processing latency
├── Max/Min/Avg Bid Amount: Bid statistics
└── Winner: Final highest bidder
```

### 3. View Performance Metrics
- Real-time bid success/failure tracking
- Conflict occurrence detection
- Performance comparison between strategies
- Export results for analysis

---

##  API Endpoints

### Bidding Engine Endpoints

**Auctions**
- `POST /api/auctions` - Create new auction
- `GET /api/auctions/{id}` - Get auction details
- `GET /api/auctions` - List all auctions
- `POST /api/auctions/{id}/close` - End auction

**Bidding**
- `POST /api/bids/place` - Place a bid with strategy selection
- `POST /api/bids/simulate` - Run concurrent bid simulation
- `GET /api/bids/stats/{auctionId}` - Get bid statistics
- `GET /api/bids/results/{auctionId}` - Get detailed bid results

### User Service Endpoints
- `POST /api/users/register` - Register new user
- `GET /api/users/{id}` - Get user profile
- `PUT /api/users/{id}` - Update user info

### Product Service Endpoints
- `POST /api/products` - Create product
- `GET /api/products` - List products
- `GET /api/products/{id}` - Get product details

---

##  Technology Stack

### Backend
- **Framework**: Spring Boot 4.0.5
- **Language**: Java 21
- **Database**: PostgreSQL 15+ (via Neon)
- **Cache/Messaging**: Redis (via Upstash)
- **ORM**: Hibernate JPA
- **Database Migration**: Flyway
- **Build Tool**: Maven 3.9.6

### Frontend
- **Framework**: Angular 21.2
- **UI Library**: Angular Material 21.2
- **HTTP Client**: Angular HttpClient
- **Styling**: SCSS
- **State Management**: RxJS

### DevOps & Deployment
- **Containerization**: Docker
- **Orchestration**: Railway (Cloud Native)
- **Database**: Neon (Serverless PostgreSQL)
- **Cache**: Upstash (Managed Redis)

---

## 📈 Performance Considerations

### Bidding Strategy Trade-offs

| Metric | Redis Lock | Optimistic Lock | Naive |
|--------|-----------|-----------------|-------|
| **Throughput** | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Consistency** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Latency** | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Conflict Rate** | 0% | 5-15% | 20-40% |

### Optimization Tips
1. **Use Redis Lock** for high-value auctions requiring guaranteed consistency
(for this project it cant handle too many request as it is running on free tier)
2. **Use Optimistic Lock** for balanced performance and safety in typical scenarios
3. **Use Naive** only for testing baselines and low-concurrency scenarios
4. Enable Redis caching for frequent product/user lookups
5. Use database connection pooling (configured in Neon)

---

##  Security Features

-  SSL/TLS encryption for all external connections
-  Database credentials stored in environment variables
-  Redis password authentication (Upstash)
-  CORS configuration for frontend communication
-  Input validation on all endpoints
-  Transaction-based operations for data consistency

---

##  Complete System as Full Bidding Platform

This project simulator demonstrates a **complete distributed bidding system** by integrating:

| Component | Role | Status |
|-----------|------|--------|
| **Bidding Engine** | Core auction logic & strategy simulator |  Complete |
| **User Service** | User identity & account management |  Complete |
| **Product Service** | Inventory & product catalog |  Complete |
| **API Gateway** | Request routing & authentication |  Complete |
| **Frontend** | User interface & visualization |  Complete |

**To build a production bidding platform**, simply extend:
- Add payment processing service
- Implement authentication & authorization service
- Add notification service (email/SMS)
- Integrate with seller dashboard
- Add advanced reporting & analytics
- Api-gateway is also need to be configured
- Also the integrate the user and product service in bidding engine
- The integration is mandatory as right now bidding engine is only a simulator

---

##  Testing & Simulation

Run the built-in simulator to:
1. Compare bidding strategy performance
2. Identify system bottlenecks
3. Validate concurrent bid handling
4. Generate performance baselines
5. Test high-load scenarios

Example simulation parameters:
```
Strategy: Redis Lock
Users: 100
Duration: 5 minutes
Expected Result: High consistency, moderate throughput
```

---

##  Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/bidding-enhancement`
3. Commit changes: `git commit -am 'Add bidding enhancement'`
4. Push to branch: `git push origin feature/bidding-enhancement`
5. Submit a pull request

---

##  License

This project is private and proprietary. All rights reserved.

---

##  Support & Contact

For issues, questions, or feature requests:
- Create an issue in the repository
- Check existing documentation
- Review the API endpoints documentation

---

##  Learning Resources

### Understanding Bidding Strategies
- **Redis Lock Pattern**: Distributed mutual exclusion using Redis
- **Optimistic Locking**: Version-based conflict detection in databases
- **Concurrency Control**: Handling simultaneous operations safely

### Spring Boot & Microservices
- Multi-service architecture patterns
- Database migration with Flyway
- Spring Data JPA for ORM
- REST API design principles

### Angular Frontend
- Component-based architecture
- RxJS reactive programming
- Material Design components
- HTTP communication patterns

---


---

**Built with Luv for efficient distributed bidding**
