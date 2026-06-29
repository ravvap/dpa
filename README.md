# TIP Data Processing Admin (DPA) Backend Microservice

This microservice provides the enterprise-grade backend infrastructure for managing, filtering, tracking, and auditing processing control parameters within the Data Processing Admin console. It handles dynamic lookup dropdown assemblies, compound multi-mode grid searches, strict role-based access controls (RBAC), automatic history trail serialization, and binary Excel generation optimized for Angular Enterprise Grids.

## 👤 Project Ownership
* **Primary Developer:** Prasad Ravva
* **Creation Date:** June 29, 2026
* **Target Package Namespace:** `gov.fdic.tip.dpa.*`

---

## 🚀 Key Features
* **Advanced Compound Filters:** Main screen views can filter by Description (`field_name`), Source System, or both concurrently.
* **Smart Period Resolution:** Spits out active rows matching `display = true` while dynamically computing processing periods directly from `dm_assessment_dates` using `date_type IN ('Period Begin Date', 'Period End Date')`.
* **Automated History Logging:** Updates automatically record into the audit tables with a standardized `MANUAL_EDIT` change flag tracking modification histories.
* **Interactive OpenAPI Integration:** Embedded Swagger config allowing direct Bearer token authenticated execution profiles.
* **High-Fidelity Document Streams:** Out-of-the-box support for binary streaming formatting cell properties, font weights, and tracking banners matching the Angular UI layout.

---

## 🗄️ Core Database Architecture
The sub-system relies on the following relational table matrices:
1. `dpa_operation_rules`: The primary engine table tracking data configurations, filtering logic, and active grid rendering visibility (`display = true`).
2. `dpa_source_system`: Standard corporate registry populated by explicit system records (`CALL`, `RRPS`, `SIMS`).
3. `dm_assessment_dates`: Evaluates active operational reporting windows contextually using business date rules.
4. `dpa_processing_control_history`: Secure immutable storage repository holding state modifications.

---

## 📡 API Endpoints Map (`DpaConstants`)

All endpoints are configured and accessed using properties registered within `DpaConstants.Routes`:

| HTTP Method | Route Endpoint | Required Authority SpEL | Purpose |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/v1/dpa/lookup/descriptions` | `hasAuthority('DPA_LINE_ITEM_VIEW')` | Pulls unique `field_name` choices for the UI filter. |
| **GET** | `/api/v1/dpa/lookup/source-systems` | `hasAuthority('DPA_LINE_ITEM_VIEW')` | Pulls names alphabetically from `dpa_source_system`. |
| **GET** | `/api/v1/dpa/grid-search` | `hasAuthority('DPA_LINE_ITEM_VIEW')` | Performs compound grid evaluations. |
| **GET** | `/api/v1/dpa/history` | `hasAuthority('DPA_LINE_ITEM_VIEW')` | Loads history logs for selected row tracking elements. |
| **PUT** | `/api/v1/dpa/update` | `hasAuthority('DPA_LINE_ITEM_EDIT_MODE')` | Commits modifications and logs a `MANUAL_EDIT` audit row. |
| **GET** | `/api/v1/dpa/export` | `hasAuthority('DPA_LINE_ITEM_VIEW')` | Generates a formatted Apache POI `.xlsx` binary download. |

---

## 🛠️ Compilation, Testing & Interactive Documentation

### 1. Build Compilation
Compile the microservice package and run internal integration tests using the Maven lifecycle wrapper:
```bash
mvn clean package