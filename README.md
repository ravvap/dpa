# TIP Data Processing Admin (DPA) Backend Microservice

This microservice provides the complete backend system for managing, tracking, and auditing processing control parameters inside the Data Processing Admin console. It supports multi-mode history searches, lookup metadata assembly for client dropdown workflows, role-based access rules (RBAC), and high-fidelity binary streams to support Angular Enterprise Grid Excel exports.

## 🚀 Key Features
* **Package Structure:** Standardized under `gov.fdic.tip.dpa.*`.
* **Dynamic Lookups:** Centralized orchestration of lookup engines filtering by active `source_system` and `dm_assessment_dates`.
* **Dual-Mode Mutual Exclusivity Guard:** Strict enforcement of parameter tracking rules preventing client-side filter collisions (400 Bad Request if Mode 1 and Mode 2 parameters are concurrently provided).
* **High-Fidelity Grid Exports:** Streamlined integration with Angular Enterprise Grid frameworks using Apache POI (`.xlsx`) featuring proper alignment, font weight hierarchies, custom metadata banners, and safe code translations.
* **Granular RBAC Enforcements:** Strict method-level access controls via custom Spring Security authorizations.

---

## 🛠️ Technology Stack & Dependencies

The system builds cleanly using **Java 17+** and **Spring Boot 3.x**. Core dependency nodes defined within the `pom.xml` build matrix include:
* `spring-boot-starter-web` — High-performance REST service delivery engine.
* `spring-boot-starter-data-jpa` — Persistence abstraction layer driving data mapping bindings.
* `spring-boot-starter-security` — Security adapter intercepting execution requests against role credentials.
* `postgresql` — Native engine driver optimizing execution across tables.
* `org.projectlombok:lombok` — Boilerplate removal processor.
* `org.apache.poi:poi-ooxml (5.2.5)` — Native system processing memory pipelines into high-performance spreadsheet structures.

---

## 🗄️ Database Architecture & Lookup Registry

The system maps explicitly across your designated storage tables:

1. **`source_system`**: Drives target domain routing filters (`CALL`, `RRPS`, `SIMS`).
2. **`dm_assessment_dates`**: Drives assessment window contexts (`2026Q1`, `2026Q2`, etc.).
3. **`dpa_processing_controls`**: Primary transactional processing records.
4. **`dpa_processing_control_history`**: Audit log tracking mutations (`MANUAL_EDIT`, `QUARTER_INIT`, `MIGRATION`).

### Security Matrix Mapping (RBAC)
Database mappings inside the security provider translate permission codes into functional method-level policies:
* **`DPA_LINE_ITEM_VIEW`**: Assigned to `Analyst`, `Admin`, and `Manager` roles. Grants operational authority to lookup grids, metadata structures, and standalone historical records.
* **`DPA_LINE_ITEM_EDIT_MODE`**: Assigned to `Admin` and `Manager` roles exclusively. Protects transactional mutation routes.

---

## 📡 API Contract Specification

### 1. Unified Dropdown Metadata
Assembles metadata matrices required to instantiate filtering dropdown selectors within your user interface.
* **Endpoint:** `GET /api/v1/dpa/metadata`
* **Authorization Header:** `Bearer <JWT>` (Requires `DPA_LINE_ITEM_VIEW` permission)
* **Response Payload Structure (`200 OK`):**
```json
{
  "sourceSystems": ["CALL", "RRPS", "SIMS"],
  "processingPeriods": [
    { "periodCode": "2026Q1", "displayLabel": "2026 Quarter 1" },
    { "periodCode": "2026Q2", "displayLabel": "2026 Quarter 2" }
  ],
  "dataElements": [
    { "lineItemNumber": 101, "description": "Total Domestic Deposits" }
  ]
}