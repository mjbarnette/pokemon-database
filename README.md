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
- Transitioning to professional software engineering

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

**Phase 3: REST API (Current)**
- REST endpoints
-JSON serialization

**Phase 4: Frontend (Planned)**

- React-based UI

---

###📄 License

MIT License - Feel free to use this project as a learning resource.

###📞 Contact

- GitHub: @mjbarnette
- LinkedIn: www.linkedin.com/in/michaelbarnette8315a869
- Email: mjbarnette@live.com

Note: This project is part of my portfolio demonstrating the transition from CS education to professional software engineering. It intentionally shows progression from foundational concepts to framework-based development.

### Database Set up

Added Flyway and schema to the project

### Modified JUnit

Modified the JUnit Tests on DAO to account for the new schema in Flyway.

### Updated to JPA

- Updated Schema to account for improvements.
- Created Entities Pokemon, Pokestats, Pokeevolutions, Moves
- Created Repositories for All Entities
- Created a Service class for Pokemon
- Improved Application Properites to remove noise from output during debugging

### Completed Initial Testing of JPA

- Created a JUnit Test for the Pokemon Service Class.
- Created Application and Dataloader to test loading single Pokemon
- This Dataloader enable viewing of information in Tables in PGAdmin to verify correct storing.