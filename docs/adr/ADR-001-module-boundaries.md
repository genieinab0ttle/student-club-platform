# ADR-001: Feature Module Boundaries

**Status:** Accepted

## Context
The Student Club Management Platform contains several modules that need to share information. Direct access to other modules' database tables could create tight coupling and bypass security rules.

## Decision
Each module exposes a public service interface. Other modules access its functionality only through that interface. Inside each module, dependencies follow the direction: Controller → Service → Repository.

## Alternatives Considered
- **Shared data access:** Rejected because it creates tight coupling between modules.
- **Separate database per module:** Rejected because it complicates data synchronization and transactions.

## Consequences

**Positive:**
- Modules are easier to maintain and test independently.
- Financial access checks remain inside the Budget module.
- Modules have clear ownership boundaries.

**Negative:**
- More interfaces and additional code are required.
- Cross-module operations need service calls instead of direct database queries.
- Developers must enforce these boundaries through code review.
