# Student Club Management Platform

## Project Overview

The Student Club Management Platform is designed to help SDU clubs manage meetings, room booking requests, announcements, interview reservations, budgets, and expenses in one place.

The platform aims to prevent scheduling conflicts, simplify club management, and protect financial information through role-based access control.

## Team Winx

- Serik Aliya — 240107066
- Kanatkyzy Aruna — 240107021
- Arystanbek Bibinur — 240107065
- Nuerjuma Ahemeili — 240107120

## Initial Architecture

The proposed architecture is a **Layered Modular Monolith** consisting of:

- **Web App:** user interface for club members, club heads, candidates, room coordinators, and finance staff.
- **Backend API:** handles business rules, reservations, permissions, and financial operations.
- **Database:** stores users, clubs, schedules, bookings, announcements, budgets, and expenses.

## Architecture Decision

ADR-001 defines module boundaries: modules communicate through public service interfaces rather than accessing each other's repositories or tables directly.

See [`docs/adr/ADR-001-module-boundaries.md`](docs/adr/ADR-001-module-boundaries.md).

## Scope

The initial prototype does not integrate with existing university, banking, email, or calendar systems.
