# UserAuthSystem

A full-stack user authentication system built with **Java Servlets**, **MySQL**, and **BCrypt** — following a clean layered MVC architecture.

---

## Screenshots

### Login Page
<img src="https://github.com/user-attachments/assets/1d76649f-86c1-4ffa-bc4a-a12c62f976bd" width="700" alt="Login"/>

### Register Page
<img src="https://github.com/user-attachments/assets/b782112e-4d1d-418f-95a6-7f4df8e2b628" width="700" alt="Register"/>

### Welcome Page
<img src="https://github.com/user-attachments/assets/0def6791-6141-4a46-bad6-71432b9449e9" width="700" alt="Welcome"/>

---

## Features

- User registration with full input validation
- Secure login with BCrypt password hashing (cost factor 12)
- Session management with session fixation protection
- AuthFilter protecting all routes — unauthenticated users redirected to login
- Welcome page showing name and email after successful login
- Clean logout with full session invalidation
- Responsive UI with password show/hide toggle on all password fields
- Anti email-enumeration — same error for wrong email and wrong password

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Jakarta Servlet 6.0 |
| Server | Apache Tomcat 10.1 |
| Database | MySQL 8+ |
| Password Hashing | BCrypt (jBCrypt) |
| Connection Pool | Tomcat JNDI / DBCP |
| Logging | SLF4J |
| Testing | JUnit 5, Mockito, AssertJ, H2 |
| Build | Maven |

---

## Project Structure

```
src/
├── main/
│   ├── java/com/auth/
│   │   ├── config/
│   │   │   ├── DatabaseConfig.java          # JNDI DataSource resolution
│   │   │   └── AppLifecycleListener.java    # Pool init and shutdown
│   │   ├── model/
│   │   │   └── User.java                    # Domain entity
│   │   ├── dto/
│   │   │   ├── LoginRequest.java            # Login form data
│   │   │   ├── RegisterRequest.java         # Register form data
│   │   │   └── UserSessionDto.java          # Safe session object (no password hash)
│   │   ├── exception/
│   │   │   └── AuthException.java           # Typed exception with ErrorCode enum
│   │   ├── validator/
│   │   │   └── AuthValidator.java           # Input validation rules
│   │   ├── dao/
│   │   │   └── UserDAO.java                 # SQL queries only
│   │   ├── service/
│   │   │   ├── AuthService.java             # Interface
│   │   │   └── AuthServiceImpl.java         # Business logic, BCrypt hashing
│   │   ├── filter/
│   │   │   └── AuthFilter.java              # Route protection
│   │   └── servlet/
│   │       ├── LoginServlet.java            # POST /login
│   │       ├── RegisterServlet.java         # POST /register
│   │       └── LogoutServlet.java           # GET  /logout
│   └── webapp/
│       ├── login.html
│       ├── register.html
│       ├── welcome.html
│       ├── index.html
│       ├── css/style.css
│       ├── WEB-INF/web.xml
│       └── META-INF/context.xml             # NOT committed — contains credentials
└── test/
    └── java/com/auth/
        ├── validator/AuthValidatorTest.java
        ├── service/AuthServiceImplTest.java
        └── dao/UserDAOTest.java
```

---

## Architecture

```
HTTP Request
     │
     ▼
┌──────────────────────────────┐
│         AuthFilter           │  ← Checks session on every request
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│     Servlet (Controller)     │  ← Reads HTTP params, redirects
│  LoginServlet                │
│  RegisterServlet             │
│  LogoutServlet               │
└──────────────┬───────────────┘
               │  DTO
               ▼
┌──────────────────────────────┐
│       Service Layer          │  ← Business logic, BCrypt, validation
│       AuthServiceImpl        │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│         DAO Layer            │  ← SQL only
│         UserDAO              │
└──────────────┬───────────────┘
               │  JNDI
               ▼
            MySQL
```

---

## Setup

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/UserAuthSystem.git
cd UserAuthSystem
```

### 2. Create the database
```bash
mysql -u root -p < database.sql
```

### 3. Configure credentials
Create `src/main/webapp/META-INF/context.xml` — this file is in `.gitignore` and must **never** be committed:

```xml
<Context>
    <Resource
        name="jdbc/authDB"
        auth="Container"
        type="javax.sql.DataSource"
        driverClassName="com.mysql.cj.jdbc.Driver"
        url="jdbc:mysql://localhost:3306/auth_db?useSSL=false&amp;serverTimezone=UTC"
        username="YOUR_DB_USER"
        password="YOUR_DB_PASSWORD"
        maxTotal="10"
        maxIdle="5"
        minIdle="2"
        maxWaitMillis="20000"
    />
</Context>
```

### 4. Build
```bash
mvn clean package
```

### 5. Deploy
Right-click project in Eclipse → **Run As** → **Run on Server** → select Tomcat 10.1

### 6. Open in browser
```
http://localhost:8081/UserAuthSystem/
```

---

## Requirements

- Java 21+
- Apache Tomcat 10.1
- MySQL 8+
- Maven 3.8+

---

## App Flow

```
/register  →  fill form  →  account created  →  welcome page
/login     →  fill form  →  logged in        →  welcome page
/welcome   →  shows name + email + sign out button
/logout    →  session cleared  →  back to login
```

---

## Security Measures

| Measure | Implementation |
|---|---|
| Password hashing | BCrypt cost factor 12 |
| Session fixation | Session regenerated on every login |
| Email enumeration | Same error message for wrong email and wrong password |
| XSS via cookies | HttpOnly flag on session cookie |
| Route protection | AuthFilter intercepts every request |
| Credential safety | context.xml gitignored — never in source code |

---

## Running Tests

```bash
mvn test
```

No MySQL required — DAO tests use H2 in-memory database.

| Test Class | What It Tests | Database |
|---|---|---|
| `AuthValidatorTest` | All validation rules | None |
| `AuthServiceImplTest` | Business logic with mocked DAO | None |
| `UserDAOTest` | SQL queries and constraints | H2 in-memory |
