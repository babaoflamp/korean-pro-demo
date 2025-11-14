# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/claude-code) when working with code in this repository.

## Project Overview

**Korean-Pro-Demo** is a Spring Boot 3.4.1 application for Korean pronunciation evaluation and AI-based language learning. The application integrates with external speech processing engines (SpeechPro) to provide pronunciation assessment, text-to-speech, and language learning features.

**Technology Stack:**
- Java 17
- Spring Boot 3.4.1 (Data JPA, Web, Thymeleaf, WebFlux)
- PostgreSQL with QueryDSL
- Lombok
- Apache POI for file processing

## Building and Running

### Gradle Commands

```bash
# Build the project
./gradlew build

# Run the application (default: dev profile, port 8080)
./gradlew bootRun

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=demo'

# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

### Application Profiles

Three profiles are configured in `bin/main/application.yml`:
- **dev** (default): Local development with PostgreSQL at 192.168.123.181:5432
- **prod**: Production server at 112.220.79.218:18154
- **demo**: Demo environment at 112.220.79.218:18154, runs on port 8081

To switch profiles, modify `spring.profiles.active` in application.yml or use command line args.

### Running the Application

```bash
# Default (dev profile)
./gradlew bootRun

# Demo profile (port 8081)
./gradlew bootRun --args='--spring.profiles.active=demo'
```

Access the application at `http://localhost:8080` (dev/prod) or `http://localhost:8081` (demo).

## Architecture

This application follows **Domain-Driven Design (DDD)** with a layered architecture pattern.

### Package Structure

```
com.mk
├── api/                    # Business modules (DDD bounded contexts)
│   ├── engine/            # Speech processing engine integration
│   │   ├── application/   # Service layer + DTOs
│   │   ├── domain/        # Entities (SpKoQuestion, SpKoAnswer)
│   │   ├── infrastructure/# Repositories (JPA + QueryDSL)
│   │   └── presentation/  # REST Controllers
│   ├── file/              # File upload/management
│   └── log/               # System/Web/Login logging
├── common/                 # Shared utilities
│   ├── ApiResponse        # Standard REST response wrapper
│   ├── HttpUtil           # HTTP client for external APIs
│   ├── FileUtil           # File operations
│   └── Base64ToFileConverter
├── config/                 # Spring configuration
│   ├── exception/         # Global exception handlers
│   ├── jpa/               # QueryDSL configuration
│   ├── logging/           # AOP-based logging
│   ├── validation/        # Validation groups
│   └── webmvc/            # Web MVC configuration
└── web/                    # Thymeleaf controllers
    ├── sp/                # Speech processing UI controllers
    └── HomeController
```

### Layer Responsibilities

1. **Presentation Layer** (`presentation/`)
   - REST controllers (`@RestController`)
   - Request/response handling
   - Example: `SpKoDemoRestController`

2. **Application Layer** (`application/`)
   - Service classes containing business logic
   - DTOs for data transfer
   - Transaction boundaries (`@Service`)
   - Example: `SpDemoService`

3. **Domain Layer** (`domain/`)
   - JPA entities with business rules
   - Rich domain models using Lombok `@Builder`
   - Example: `SpKoQuestion`, `SpKoAnswer`

4. **Infrastructure Layer** (`infrastructure/`)
   - Repository interfaces extending `JpaRepository`
   - Custom repository implementations with QueryDSL
   - Pattern: `XxxRepository` extends `JpaRepository<Entity, ID>` + `XxxRepositoryCustom`
   - Custom implementation: `XxxRepositoryCustomImpl` uses QueryDSL

### External API Integration

The application integrates with **SpeechPro Korean** engine (`api.speechpro_kr.url`):

**Endpoints:**
- `/gtp` - G2P (Grapheme-to-Phoneme) conversion
- `/model` - Pronunciation model generation
- `/scorejson` - Pronunciation evaluation via base64 audio

**Service:** `SpDemoService` (src/main/java/com/mk/api/engine/application/SpDemoService.java:43)
- `createModel()` - Generates pronunciation symbols and models
- `createEvaluate()` - Evaluates pronunciation from base64 audio, saves to DB and file system

## Key Features

### 1. AOP-Based System Logging

`SysLogAspect` (src/main/java/com/mk/config/logging/SysLogAspect.java:35) automatically logs all service operations:
- Intercepts: `find*`, `create*`, `update*`, `delete*` methods in `*Service` classes
- Tracks: execution time, errors, IP addresses, method names
- Exception handling with `@AfterThrowing`

**Disable logging for specific methods:**
```java
@NoLogging  // Add this annotation to exclude from logging
public ApiResponse<?> myMethod() { ... }
```

### 2. Standard API Response

All REST endpoints use `ApiResponse<T>` (src/main/java/com/mk/common/ApiResponse.java:5):
```java
return ApiResponse.of(HttpStatus.OK, data);
return ApiResponse.of(HttpStatus.NOT_FOUND, null);
```

Status codes are automatically mapped to messages (200→"Operation succeeded", 404→"Resource not found", etc.).

### 3. QueryDSL for Type-Safe Queries

QueryDSL Q-types are auto-generated in `src/main/generated/` when you build. Custom repository pattern:
```java
// Interface
public interface SpKoQuestionRepository extends
    JpaRepository<SpKoQuestion, Long>, SpKoQuestionRepositoryCustom { }

// Custom interface
public interface SpKoQuestionRepositoryCustom {
    SpKoQuestionDTO findOneByDemo(Long id);
}

// Implementation with QueryDSL
public class SpKoQuestionRepositoryCustomImpl implements SpKoQuestionRepositoryCustom {
    // Use QSpKoQuestion here
}
```

### 4. File Upload Handling

Files are saved to profile-specific directories:
- **dev**: `C:\data\mzcore\`
- **prod**: `D:\data\mzcore\`
- **demo**: `C:/data/mzcore/`

Multipart config: max file 5MB, max request 10MB

### 5. Internationalization (i18n)

Message properties in `src/main/resources/message/`:
- `messages.properties` (default)
- `messages_ko_kr.properties` (Korean)
- `messages_en_us.properties` (English)

## Testing

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests com.mk.AiEconCampApplicationTests

# Run with detailed output
./gradlew test --info
```

Current test file: `src/test/java/com/mk/AiEconCampApplicationTests.java`

## Database

**PostgreSQL** database `mkaieconcamp` with:
- JPA entities in `domain/` packages
- Hibernate dialect: PostgreSQL
- SQL logging via log4jdbc (driver: `net.sf.log4jdbc.sql.jdbcapi.DriverSpy`)
- `open-in-view: false` to prevent lazy loading issues

**Connection details** are profile-specific (see application.yml).

## Important Configuration Notes

### Space Normalization for Korean Text

Korean text from database may have different space characters. Use `SpDemoService.normalizeSpaces()` (src/main/java/com/mk/api/engine/application/SpDemoService.java:253) to convert NBSP, Em Space, En Space, etc. to regular spaces before sending to SpeechPro API.

### QueryDSL Generated Sources

After modifying JPA entities, rebuild to regenerate Q-types:
```bash
./gradlew clean build
```

Generated Q-types location: `build/generated/sources/annotationProcessor/java/main/`

### Logging Exclusions

The following URIs are excluded from system logging:
- `/api/logout`
- `/api/login/createMail`
- `/api/login/findCodeCheck`
- `/api/login/updatePwd`
- `/speechpro/demo`
- `/api/sp/demo*`

## Common Development Tasks

### Adding a New Service Method

1. Add method to service class (e.g., `SpDemoService`)
2. AOP logging will automatically intercept based on method name prefix (`find*`, `create*`, `update*`, `delete*`)
3. Return `ApiResponse<T>` for REST endpoints
4. Add `@NoLogging` if logging should be skipped

### Adding a New Entity

1. Create entity in `domain/` package
2. Add `@Entity`, `@Getter`, Lombok `@Builder`
3. Run `./gradlew build` to generate QueryDSL Q-type
4. Create repository interface extending `JpaRepository`
5. Add custom repository interface + implementation if needed

### Working with External APIs

Use `HttpUtil.executeRequest()` (src/main/java/com/mk/common/HttpUtil.java) for external HTTP calls:
```java
Map<String, Object> header = new HashMap<>();
header.put("Content-Type", "application/json");

Map<String, Object> body = new HashMap<>();
body.put("key", "value");

String result = HttpUtil.executeRequest("POST", url, header, body);
```
