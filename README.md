# Birth Certificate Application System (BCAS)

A console-based Birth Certificate Application System built with **Core Java** and **Oracle Database (JDBC)**. Citizens register, submit applications, and receive digitally issued certificates; administrators review, approve, or reject applications.

## Tech Stack
- **Java** (JDK 17+)
- **Oracle Database** (XE 18c / 21c) via **JDBC** (`ojdbc8.jar`)
- Layered architecture: `model` → `dao` → `service` → `Main` (UI)

## OOP Concepts Demonstrated
| Concept | Where |
|---|---|
| **Encapsulation** | Private fields + getters/setters in every `model` class |
| **Abstraction** | Abstract class `User` with abstract methods `getRole()`, `showMenu()` |
| **Inheritance** | `Citizen extends User`, `Admin extends User` |
| **Polymorphism** | Runtime dispatch of `showMenu()`, `getRole()`; DAO returns `User` |

## Project Structure
```
BirthCertificateSystem/
├── sql/schema.sql              # Oracle schema + seed admin
├── lib/ojdbc8.jar              # (you provide)
└── src/
    ├── Main.java               # Console entry point
    ├── db/DatabaseConnection.java
    ├── model/
    │   ├── User.java           # abstract
    │   ├── Citizen.java
    │   ├── Admin.java
    │   ├── Application.java
    │   └── Certificate.java
    ├── dao/
    │   ├── UserDAO.java
    │   ├── ApplicationDAO.java
    │   └── CertificateDAO.java
    ├── service/
    │   ├── AuthService.java
    │   ├── ApplicationService.java
    │   └── CertificateService.java
    └── util/InputHelper.java
```

## Setup

1. **Install Oracle DB** (XE is fine) and create a schema/user.
2. **Run the schema**:
   ```sql
   sqlplus system/oracle@//localhost:1521/XE @sql/schema.sql
   ```
3. **Download `ojdbc8.jar`** from Oracle and drop it into `lib/`.
4. **Update credentials** in `src/db/DatabaseConnection.java`:
   ```java
   URL      = "jdbc:oracle:thin:@localhost:1521:XE";
   USER     = "system";
   PASSWORD = "your_password";
   ```

## Compile & Run

**Linux / macOS:**
```bash
cd BirthCertificateSystem
javac -d out -cp "lib/*" $(find src -name "*.java")
java -cp "out:lib/*" Main
```

**Windows:**
```cmd
cd BirthCertificateSystem
javac -d out -cp "lib/*" src\Main.java src\db\*.java src\model\*.java src\dao\*.java src\service\*.java src\util\*.java
java -cp "out;lib/*" Main
```

## Default Credentials
- **Admin** → `admin` / `admin123`
- **Citizen** → register from the main menu.

## Sample Workflow
1. Register as a citizen → login.
2. Submit a birth certificate application (child details, parents, DOB, etc.).
3. Logout, login as admin (`admin` / `admin123`).
4. View pending applications, approve one — system auto-generates a certificate number `BC-<timestamp>`.
5. Login again as the citizen → view the issued certificate.

## Database Schema
- `users` — citizens and admins (role-based)
- `applications` — submitted applications with status (`PENDING` / `APPROVED` / `REJECTED`)
- `certificates` — issued certificates linked to approved applications

Foreign keys and check constraints enforce referential and data integrity.

## Resume Bullet (updated)
> Developed a Birth Certificate Application System using Core Java and Oracle DB (JDBC) with a layered architecture (model–DAO–service–UI) supporting citizen registration, application submission, admin approval workflow, and automated certificate generation. Applied all four OOP pillars — encapsulation, abstraction, inheritance, and polymorphism — through an abstract `User` hierarchy and role-based session dispatch.
