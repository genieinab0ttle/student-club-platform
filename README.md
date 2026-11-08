# Student Club Management Platform

## Team Winx

| Member | Student ID |
|---|---|
| Serik Aliya | 240107066 |
| Kanatkyzy Aruna | 240107021 |
| Arystanbek Bibinur | 240107065 |
| Nuerjuma Ahemeili | 240107120 |

## Project overview

The Student Club Management Platform is intended to help SDU clubs manage meetings, room requests, announcements, interview reservations, budgets, and expenses.

Milestone 3 implements one coherent flow: creating and viewing club meetings.

## Implemented user flow

1. The user enters a meeting title, date, time, and location.
2. React sends the details to POST /api/meetings.
3. The backend validates required fields and rejects dates before the current date.
4. MeetingService saves valid meetings through MeetingRepository.
5. PostgreSQL stores the meeting and generates its ID.
6. The backend returns the saved meeting with HTTP 201.
7. The frontend displays the meeting.
8. After a page refresh, GET /api/meetings retrieves the stored meetings.

A meeting location is descriptive information. It does not create an approved room booking.

## Current limitations

The prototype does not yet implement:

- Authentication or role-based access control.
- Club-specific meeting filtering.
- Meeting end times, editing, or cancellation.
- Room requests, approvals, or conflict detection.
- Announcements, interview reservations, budgets, or expenses.

Rejected meeting creation currently produces a generic frontend error message.

## Technologies

- Frontend: JavaScript, React, Vite.
- Backend: Java, Spring Boot, Jakarta Validation.
- Persistence: Spring Data JPA and PostgreSQL.
- Tests: JUnit, Spring Boot Test, and MockMvc.
- Build tools: Maven Wrapper and npm.

The Maven project targets Java 17. The verified team environment used Java 26.0.2.1, Node.js 26.13.0, npm 11.20.0, and PostgreSQL 16.14 on macOS.

Other environments have not yet been verified.

## Requirements

Install:

- A JDK compatible with the Spring Boot version in backend/pom.xml.
- Node.js and npm compatible with frontend/package.json.
- PostgreSQL.
- Git.

## Get the project

```bash
git clone https://github.com/genieinab0ttle/student-club-platform.git
cd student-club-platform
```

The implemented prototype is available in main.

## Database setup

Start PostgreSQL and create a database named student_club.

For example, replace YOUR_DB_USER and use the port of your PostgreSQL instance:

```bash
createdb -h localhost -p 5433 -U YOUR_DB_USER student_club
```

The backend reads its connection settings from environment variables:

| Variable | Purpose | Default |
|---|---|---|
| DB_USERNAME | PostgreSQL username | Required |
| DB_PASSWORD | PostgreSQL password | Empty |
| DB_URL | JDBC connection URL | jdbc:postgresql://localhost:5433/student_club |

An empty password works only if the local PostgreSQL authentication configuration permits it.

Set credentials locally. Do not commit passwords.

This project does not automatically load .env files.

Hibernate uses ddl-auto=update to create or update the prototype schema. Production database migrations are outside this milestone.

## Run the backend

In a terminal, from the project root:

```bash
cd backend
export DB_USERNAME=YOUR_DB_USER
```

If your PostgreSQL instance uses another port, set DB_URL:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/student_club
```

If required, set DB_PASSWORD in your local environment.

Then run:

```bash
bash mvnw spring-boot:run
```

The backend runs at:

http://localhost:8080

Keep this terminal running.

On Windows PowerShell, set variables using:

```powershell
$env:DB_USERNAME="YOUR_DB_USER"
```

Then run:

```powershell
.\mvnw.cmd spring-boot:run
```

## Run the frontend

Open a second terminal, starting from the project root:

```bash
cd frontend
npm ci
npm run dev
```

Open:

http://localhost:5173/

Keep both frontend and backend terminals running.

The backend currently permits this frontend origin through CORS. Use localhost:5173 rather than another hostname or port.

## API

| Method | Endpoint | Behaviour |
|---|---|---|
| POST | /api/meetings | Validates and saves a meeting |
| GET | /api/meetings | Returns saved meetings |

A valid creation returns HTTP 201.

Invalid required fields or a past meeting date return HTTP 400.

Meeting fields:

- id: generated identifier.
- title: required nonblank text.
- date: required date.
- time: required time.
- location: required nonblank text.

## Run automated tests

Start PostgreSQL first.

Open a terminal in backend and set the same database environment variables used for the application.

Run:

```bash
bash mvnw clean test
```

On Windows:

```powershell
.\mvnw.cmd clean test
```

The current suite contains:

1. Successful meeting creation.
2. Rejection of a blank title.
3. Rejection of a past meeting date.
4. Application context startup.

The meeting tests use MockMvc and local PostgreSQL with transactional rollback. They are integration tests rather than isolated unit tests.

No payment, messaging, or other external service is called.

Verified result on 8 November 2026:

Tests run: 4, Failures: 0, Errors: 0, Skipped: 0.
BUILD SUCCESS.

The successful creation test currently uses the fixed date 2026-11-27. Update it before running the suite after that date.

## Manual verification

The following checks passed on the team Mac:

- A future meeting can be created.
- The meeting remains after refreshing the page.
- Previously stored meetings are displayed.
- A past meeting date is rejected.
- The same location can be used on different dates.

Room booking conflict detection is not implemented.

## Architecture

The system follows a layered modular monolith design.

The implemented Meetings feature uses:

MeetingController → MeetingService → MeetingRepository → PostgreSQL

- MeetingController handles HTTP requests and input validation.
- MeetingService applies the date rule.
- MeetingRepository provides persistence operations through Spring Data JPA.
- Meeting is the persisted entity.

Only the Meetings module is implemented. Other modules remain planned.

See:

- [Architecture diagrams](docs/architecture/README.md)
- [ADR-001: Feature module boundaries](docs/adr/ADR-001-module-boundaries.md)
- [ADR-002: Meeting repository](docs/adr/ADR-002-meeting-repository.md)

## Troubleshooting

### Port 8080 is already in use

A previous backend instance may still be running. Stop that instance with Control+C in its terminal before restarting.

### Database connection refused

Check that PostgreSQL is running and DB_URL uses its actual port.

### Database authentication failed

Check DB_USERNAME, DB_PASSWORD, and the local PostgreSQL authentication settings.

### Frontend launcher is blocked after copying the project

Do not reuse node_modules from another computer. Remove only frontend/node_modules, then run npm ci again.

### Unexpected old test classes

Run bash mvnw clean test to rebuild generated classes.

## AI disclosure

ChatGPT assisted with local setup troubleshooting, implementation and test-evidence review, and drafting documentation, diagrams, and report text.

The team is responsible for verifying the material, making final decisions, and understanding and modifying every submitted part.
