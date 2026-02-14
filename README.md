# Pokemon Database Manager

A Java backend project demonstrating the evolution from raw JDBC-based data access to a modern Spring-powered architecture.

---

## 🎯 Project Purpose

This project intentionally showcases the progression of backend development techniques:
- Starting with manual JDBC and transaction management
- Transitioning to Spring Data JPA and repository abstractions
- Laying the groundwork for a REST API and frontend UI

The goal is to demonstrate not just *how* to use frameworks, but *what problems they solve* and *what they replace*.

---
## 🚀 Technologies Used

### Current Stack
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL 16
- HikariCP
- Maven
- JUnit 5
- Flyway

---

## 📋 Features

- CRUD operations for Pokémon
- Pokémon stats, moves, types, and evolution chains
- Transaction-safe updates
- Configurable sorting and filtering
- Fully tested service layer
- Database migrations via Flyway

---

## 🏗️ Architecture

### Database Schema
- `pokemon` - Core Pokemon data
- `pokeStats` - Hit points, types, weaknesses, retreat cost
- `moves` - Pokemon moves with damage and energy costs
- `pokeTypes` - Type reference data
- `pokeEvolutions` - Evolution chain relationships

### Design Principles
- Clear separation of concerns
- Service-layer business logic
- Repository-based persistence
- Transactional consistency

---

## 📚 Key Learnings

### Connection Pooling
HikariCP ensures scalable, efficient database access without connection exhaustion.

### Transaction Management
All related operations (Pokémon, stats, moves, evolutions) succeed or roll back as a single unit.

### N+1 Query Problem
Early implementations revealed N+1 issues, resolved through proper fetch strategies and query design.

### Testing Strategy
- H2 in-memory database
- Transaction rollback per test
- Repeatable, isolated test execution

---

### 🛠️ Setup Instructions

### Prerequisites
- Java 17+
- PostgreSQL 16+
- Maven 3.8+

### Installation

```bash
git clone https://github.com/mjbarnette/pokemon-database.git
cd pokemon-database
createdb pokemon_db
mvn clean test
```

### Testing
The project uses JUnit 5 with H2 in-memory database for isolated, repeatable tests.
Run tests:
mvn test

### :test_tube: Testing

Service-layer integration tests using JUnit 5

In-memory H2 database (PostgreSQL compatibility mode)

Each test runs in a clean transactional context

### 🎓 About the Developer

- 14 years retail management
- 8 years teaching Computer Science
- Experienced technical educator and team leader transitioning into professional software engineering roles with a focus on system design, quality, and maintainability.

**This project demonstrates my ability to:**
- Design maintainable systems
- Understand frameworks at a deep level
- Write clean, tested, production-quality code

---

### 📝 Project Evolution
**Phase 1: JDBC Implementation ✅ (Completed)**

- Manual SQL
- Explicit transaction handling
- Connection pooling

**Phase 2: Spring Data JPA (Completed)**
- Entity mapping
- Repositories
- Service-layer abstraction
- JPA-focused testing

**Phase 3: REST API (Completed)**
-  Implemented RESTful controllers following layered architecture principles
- Introduced DTO mapping to isolate domain entities from API contracts
- Added structured exception handling with consistent JSON error responses
- Verified request/response behavior using @WebMvcTest controller tests

**Phase 4: Frontend (Planned)**

- React-based UI

---

### 📄 License

MIT License - Feel free to use this project as a learning resource.

### 📞 Contact

- GitHub: @mjbarnette
- LinkedIn: www.linkedin.com/in/michaelbarnette8315a869
- Email: mjbarnette@live.com

Note: This project is part of my portfolio demonstrating the transition from CS education to professional software engineering. It intentionally shows progression from foundational concepts to framework-based development.

### Project Milestones:

- Designed and iterated on a normalized database schema to support Pokémon, stats, moves, and evolution chains.
- Migrated from raw JDBC to Spring Data JPA, creating entities, repositories, and a service layer aligned with the domain model.
- Implemented layered JUnit testing (repository, service, DTO, and controller) to validate business rules, data flow, and transactional integrity.
- Introduced immutable DTOs to clearly separate domain entities from external data contracts.
- Developed REST controllers as a thin orchestration layer that maps HTTP requests to DTOs and delegates validated operations to the service layer.
- Enforced separation of concerns to prevent controller bloat and ensure business logic remains independently testable.
- Refactored service-layer error handling to return explicit, domain-specific outcomes for common data integrity issues (such as duplicate entities or missing dependencies).
- Introduced enum-based domain result types to standardize error handling, simplify controller decision logic, and prepare the API for predictable UI-level feedback.

### Next Steps

- Develop a React-based frontend to consume the REST API.
- Implement client-side validation and structured error handling aligned with backend response contracts.
- Provide interactive views for managing Pokémon, moves, and evolution chains.