# Korean-Pro-Demo

한국어 발음 평가 및 AI 기반 언어 학습을 위한 Spring Boot 애플리케이션

## 📋 프로젝트 개요

**Korean-Pro-Demo**는 SpeechPro 음성 처리 엔진과 통합하여 한국어 발음 평가, TTS(Text-to-Speech), 언어 학습 기능을 제공하는 웹 애플리케이션입니다.

## 🛠 기술 스택

- **Java 17**
- **Spring Boot 3.4.1**
  - Spring Data JPA
  - Spring Web / WebFlux
  - Spring Validation
  - Thymeleaf
- **데이터베이스**: PostgreSQL
- **ORM**: JPA + QueryDSL 5.0.0
- **빌드 도구**: Gradle
- **라이브러리**:
  - Lombok
  - Apache POI 5.2.1 (Excel 처리)
  - Log4jdbc (SQL 로깅)
  - Commons IO 2.11.0

## 🏗 아키텍처

### Domain-Driven Design (DDD) 계층형 아키텍처

```
com.mk/
├── api/                          # 비즈니스 모듈 (DDD Bounded Contexts)
│   ├── engine/                   # 음성 처리 엔진 통합
│   │   ├── application/          # 서비스 레이어 + DTOs
│   │   ├── domain/               # 엔티티 (SpKoQuestion, SpKoAnswer)
│   │   ├── infrastructure/       # 리포지토리 (JPA + QueryDSL)
│   │   └── presentation/         # REST 컨트롤러
│   ├── file/                     # 파일 업로드/관리
│   └── log/                      # 시스템/웹/로그인 로깅
├── common/                        # 공통 유틸리티
│   ├── ApiResponse               # 표준 REST 응답 래퍼
│   ├── HttpUtil                  # HTTP 클라이언트
│   ├── FileUtil                  # 파일 작업
│   └── Base64ToFileConverter     # Base64 변환
├── config/                        # Spring 설정
│   ├── exception/                # 전역 예외 핸들러
│   ├── jpa/                      # QueryDSL 설정
│   ├── logging/                  # AOP 기반 로깅
│   ├── validation/               # Validation 그룹
│   └── webmvc/                   # Web MVC 설정
└── web/                           # Thymeleaf 컨트롤러
    ├── sp/                       # 음성 처리 UI
    └── HomeController            # 메인 페이지
```

### 계층별 책임

1. **Presentation Layer** (`presentation/`)
   - REST 컨트롤러 (`@RestController`)
   - 요청/응답 처리
   - 예: `SpKoDemoRestController`

2. **Application Layer** (`application/`)
   - 비즈니스 로직을 담은 서비스 클래스
   - DTO (Data Transfer Objects)
   - 트랜잭션 경계 (`@Service`)
   - 예: `SpDemoService`

3. **Domain Layer** (`domain/`)
   - 비즈니스 규칙을 가진 JPA 엔티티
   - Lombok `@Builder`를 사용한 풍부한 도메인 모델
   - 예: `SpKoQuestion`, `SpKoAnswer`

4. **Infrastructure Layer** (`infrastructure/`)
   - `JpaRepository`를 확장한 리포지토리 인터페이스
   - QueryDSL을 사용한 커스텀 리포지토리 구현
   - 패턴: `XxxRepository` + `XxxRepositoryCustom` + `XxxRepositoryCustomImpl`

## 🚀 빌드 및 실행

### Gradle 명령어

```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행 (기본: dev 프로필, 포트 8080)
./gradlew bootRun

# 특정 프로필로 실행
./gradlew bootRun --args='--spring.profiles.active=demo'

# 테스트 실행
./gradlew test

# 클린 빌드
./gradlew clean build
```

### 애플리케이션 프로필

`src/main/resources/application.yml`에 3개의 프로필이 구성되어 있습니다:

| 프로필 | 설명 | DB 서버 | 포트 | 파일 저장 경로 |
|--------|------|---------|------|---------------|
| **dev** (기본) | 로컬 개발 환경 | 192.168.123.181:5432 | 8080 | `C:\data\mzcore\` |
| **prod** | 운영 환경 | 112.220.79.218:18154 | 8080 | `D:\data\mzcore\` |
| **demo** | 데모 환경 | 112.220.79.218:18154 | **8081** | `C:/data/mzcore/` |

### 실행 방법

```bash
# 개발 환경 (dev 프로필)
./gradlew bootRun

# 데모 환경 (포트 8081)
./gradlew bootRun --args='--spring.profiles.active=demo'
```

- 개발/운영: `http://localhost:8080`
- 데모: `http://localhost:8081`

## 🔑 핵심 기능

### 1. AOP 기반 시스템 로깅

`SysLogAspect`가 모든 서비스 작업을 자동으로 로깅합니다:
- 인터셉트 대상: `*Service` 클래스의 `find*`, `create*`, `update*`, `delete*` 메소드
- 추적 정보: 실행 시간, 에러, IP 주소, 메소드명
- `@AfterThrowing`으로 예외 처리

**특정 메소드 로깅 제외:**
```java
@NoLogging  // 이 어노테이션 추가 시 로깅에서 제외
public ApiResponse<?> myMethod() { ... }
```

**로깅 제외 URI:**
- `/api/logout`
- `/api/login/createMail`
- `/api/login/findCodeCheck`
- `/api/login/updatePwd`
- `/speechpro/demo`
- `/api/sp/demo*`

### 2. 표준 API 응답

모든 REST 엔드포인트는 `ApiResponse<T>`를 사용합니다:

```java
return ApiResponse.of(HttpStatus.OK, data);
return ApiResponse.of(HttpStatus.NOT_FOUND, null);
```

상태 코드는 자동으로 메시지에 매핑됩니다 (200→"Operation succeeded", 404→"Resource not found" 등).

### 3. QueryDSL 타입 안전 쿼리

QueryDSL Q-type은 빌드 시 `src/main/generated/`에 자동 생성됩니다.

**커스텀 리포지토리 패턴:**
```java
// 인터페이스
public interface SpKoQuestionRepository extends
    JpaRepository<SpKoQuestion, Long>, SpKoQuestionRepositoryCustom { }

// 커스텀 인터페이스
public interface SpKoQuestionRepositoryCustom {
    SpKoQuestionDTO findOneByDemo(Long id);
}

// QueryDSL 구현체
public class SpKoQuestionRepositoryCustomImpl implements SpKoQuestionRepositoryCustom {
    // QSpKoQuestion 사용
}
```

### 4. 파일 업로드 처리

파일은 프로필별 디렉토리에 저장됩니다:
- **dev**: `C:\data\mzcore\`
- **prod**: `D:\data\mzcore\`
- **demo**: `C:/data/mzcore/`

Multipart 설정: 최대 파일 크기 5MB, 최대 요청 크기 10MB

### 5. 국제화 (i18n)

`src/main/resources/message/`의 메시지 프로퍼티:
- `messages.properties` (기본)
- `messages_ko_kr.properties` (한국어)
- `messages_en_us.properties` (영어)

### 6. 외부 API 통합

**SpeechPro Korean Engine** 통합 (`api.speechpro_kr.url`):

| 엔드포인트 | 기능 |
|-----------|------|
| `/gtp` | G2P (문자-음소 변환) |
| `/model` | 발음 모델 생성 |
| `/scorejson` | Base64 오디오를 통한 발음 평가 |

**서비스**: `SpDemoService`
- `createModel()`: 발음 기호 및 모델 생성
- `createEvaluate()`: Base64 오디오로 발음 평가, DB 및 파일 시스템에 저장

## 💾 데이터베이스

**PostgreSQL** 데이터베이스 `mkaieconcamp`:
- `domain/` 패키지의 JPA 엔티티
- Hibernate dialect: PostgreSQL
- log4jdbc를 통한 SQL 로깅 (driver: `net.sf.log4jdbc.sql.jdbcapi.DriverSpy`)
- `open-in-view: false` (지연 로딩 문제 방지)

연결 정보는 프로필별로 설정됩니다 (application.yml 참조).

## 🧪 테스트

```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests com.mk.AiEconCampApplicationTests

# 상세 출력과 함께 실행
./gradlew test --info
```

현재 테스트 파일: `src/test/java/com/mk/AiEconCampApplicationTests.java`

## 📝 개발 가이드

### 새로운 서비스 메소드 추가

1. 서비스 클래스에 메소드 추가 (예: `SpDemoService`)
2. 메소드명 접두사에 따라 AOP 로깅 자동 적용 (`find*`, `create*`, `update*`, `delete*`)
3. REST 엔드포인트는 `ApiResponse<T>` 반환
4. 로깅을 건너뛰려면 `@NoLogging` 추가

### 새로운 엔티티 추가

1. `domain/` 패키지에 엔티티 생성
2. `@Entity`, `@Getter`, Lombok `@Builder` 추가
3. `./gradlew build` 실행하여 QueryDSL Q-type 생성
4. `JpaRepository`를 확장하는 리포지토리 인터페이스 생성
5. 필요시 커스텀 리포지토리 인터페이스 + 구현체 추가

### 외부 API 작업

외부 HTTP 호출은 `HttpUtil.executeRequest()` 사용:

```java
Map<String, Object> header = new HashMap<>();
header.put("Content-Type", "application/json");

Map<String, Object> body = new HashMap<>();
body.put("key", "value");

String result = HttpUtil.executeRequest("POST", url, header, body);
```

### QueryDSL 소스 재생성

JPA 엔티티 수정 후, Q-type 재생성을 위해 리빌드:

```bash
./gradlew clean build
```

생성된 Q-type 위치: `build/generated/sources/annotationProcessor/java/main/`

## ⚠️ 중요 설정 사항

### 한국어 텍스트 공백 정규화

데이터베이스의 한국어 텍스트는 다양한 공백 문자를 포함할 수 있습니다. SpeechPro API로 전송하기 전에 `SpDemoService.normalizeSpaces()`를 사용하여 NBSP, Em Space, En Space 등을 일반 공백으로 변환하세요.

## 📊 주요 컨트롤러

| 컨트롤러 | 타입 | 설명 |
|---------|------|------|
| `SpKoDemoRestController` | REST | 한국어 발음 평가 API |
| `FileRestController` | REST | 파일 업로드 API |
| `SpKoController` | Web | 한국어 음성 처리 UI |
| `SpEnController` | Web | 영어 음성 처리 UI |
| `HomeController` | Web | 메인 페이지 |
| `CustomErrorController` | Web | 에러 페이지 처리 |

## 🔧 설정 파일

- `build.gradle`: Gradle 빌드 설정 및 의존성
- `application.yml`: Spring Boot 프로필별 설정
- `logback.xml`: 로깅 설정
- `messages*.properties`: 국제화 메시지

## 📦 주요 의존성

```gradle
dependencies {
    // Spring Boot Starters
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    
    // QueryDSL
    implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
    
    // Database
    runtimeOnly 'org.postgresql:postgresql'
    runtimeOnly 'org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16'
    
    // Utilities
    compileOnly 'org.projectlombok:lombok'
    implementation 'org.apache.poi:poi:5.2.1'
    implementation 'org.apache.poi:poi-ooxml:5.2.1'
}
```

## 📄 라이센스

이 프로젝트는 비공개 프로젝트입니다.

## 👥 개발팀

- **Group**: com.mk
- **Artifact**: korean-pro-demo
- **Version**: 0.0.1-SNAPSHOT
