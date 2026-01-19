# Pokemon Database Manager

A comprehensive Pokemon database management system demonstrating the evolution from foundational JDBC to modern Spring frameworks.

## 🎯 Project Purpose

This project showcases professional Java development practices and my transition from Computer Science education to software engineering. It intentionally demonstrates progression from low-level database interaction to framework-based development.

## 🚀 Technologies Used

### Current Implementation (Phase 1)
- **Java 17** - Modern Java features and syntax
- **JDBC** - Direct database interaction and connection management
- **PostgreSQL 16** - Production-grade relational database
- **HikariCP 5.1.0** - High-performance connection pooling
- **Maven** - Dependency management and build automation
- **JUnit 5** - Unit testing framework

### Planned Implementations
- **Phase 2:** Spring Data JPA - Entity mapping and repository pattern
- **Phase 3:** Spring Boot REST API - RESTful web services
- **Phase 4:** React Frontend - Modern web interface

## 📋 Features

- ✅ Complete CRUD operations for Pokemon
- ✅ Manage Pokemon stats, moves, types, and evolutions
- ✅ Query Pokemon by type and name
- ✅ Retrieve complete evolution chains
- ✅ Configurable sort ordering
- ✅ Transaction management with rollback support
- ✅ Connection pooling for scalability

## 🏗️ Architecture

### Database Schema
- `pokemon` - Core Pokemon data
- `pokeStats` - Hit points, types, weaknesses, retreat cost
- `moves` - Pokemon moves with damage and energy costs
- `pokeTypes` - Type reference data
- `pokeEvolutions` - Evolution chain relationships

### Design Patterns
- **DAO Pattern** - Data access abstraction
- **Connection Pooling** - Resource management
- **Transaction Management** - Data consistency
- **Builder Pattern** - Test data creation

## 🛠️ Setup Instructions

### Prerequisites
- Java 17 or higher
- PostgreSQL 16 or higher
- Maven 3.8+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR-USERNAME/pokemon-database.git
   cd pokemon-database

Set up PostgreSQL database

 createdb pokemon_db
psql -d pokemon_db -f src/main/resources/schema.sql


Configure database connection


Update credentials in test files (or use environment variables)
Default: localhost:5432, user: postgres
Build the project

 mvn clean install


Run tests

 mvn test


### Testing
The project uses JUnit 5 with H2 in-memory database for isolated, repeatable tests.
Run tests:
mvn test

### Test coverage includes:
CRUD operations
Transaction rollback scenarios
Evolution chain retrieval
Type-based filtering
Sorting functionality
###📚 Key Learnings
Connection Pooling
HikariCP prevents connection exhaustion in production environments. Each database operation borrows a connection from the pool rather than creating new connections.
Transaction Management
Proper transaction handling ensures data consistency. All related operations (Pokemon + Stats + Moves) succeed together or roll back together.
N+1 Query Problem
Initial implementation suffered from N+1 queries in getAllPokemon(). Optimized by using JOINs to fetch all related data in a single query.
Testing Strategy
Using H2 for tests provides:
Fast execution (in-memory)
Isolation (no shared state between tests)
PostgreSQL compatibility mode
No external dependencies
🎓 About the Developer
I'm a software engineer with a unique background:
14 years retail management (Store Manager, Assistant Manager)
8 years Computer Science education
Transitioning to full-time software development
This project demonstrates my ability to:
Build production-quality code
Understand systems from the ground up
Learn and apply modern frameworks
Write clean, tested, documented code
📝 Project Evolution
Phase 1: JDBC Implementation ✅ (Current)
Understanding foundational database interaction, connection management, and transaction handling.
Phase 2: Spring Data JPA (Planned)
Migrating to Spring Data JPA to demonstrate understanding of:
What Spring handles under the hood
When to use abstractions vs. raw JDBC
Entity mapping and relationships
Repository pattern benefits
Phase 3: REST API (Planned)
Building a RESTful API with Spring Boot:
RESTful endpoints
JSON serialization
Exception handling
API documentation
Phase 4: Frontend (Planned)
React-based user interface demonstrating full-stack capabilities.
🤝 Contributing
This is a learning/portfolio project, but I'm open to feedback and suggestions!
📄 License
MIT License - Feel free to use this project as a learning resource.
📞 Contact
GitHub: @mjbarnette
LinkedIn: [Your LinkedIn URL]
Email: your.email@example.com

Note: This project is part of my portfolio demonstrating the transition from CS education to professional software engineering. It intentionally shows progression from foundational concepts to framework-based development.
