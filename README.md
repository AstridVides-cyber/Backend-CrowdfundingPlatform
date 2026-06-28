# Backend-CrowdfundingPlatform

> Resumen basado en la rama `develop` (donde está la versión más reciente).

## ¿De qué trata este repositorio?
Backend de una plataforma de **crowdfunding** con autenticación JWT, manejo de campañas, recompensas, promesas de donación (pledges), reportes de fraude, pagos con Stripe y exportación de campañas.

Arquitectura general:
- API REST con Spring Boot.
- Capas de `controller`, `service`, `repository` y entidades JPA.
- Base de datos PostgreSQL.
- Seguridad con Spring Security + JWT.

## Roles del sistema
Actualmente hay **3 roles**:
- `ADMIN`
- `CREATOR`
- `SPONSOR`

Reglas globales de seguridad:
- `/api/auth/**` es público.
- `/api/admin/**` solo `ADMIN`.
- El resto requiere autenticación JWT.

## ¿Cómo funciona (flujo general)?
1. El usuario se registra/inicia sesión y recibe token JWT.
2. Un `CREATOR` crea campañas y recompensas.
3. Un `ADMIN` aprueba/rechaza campañas y modera reportes.
4. Un `SPONSOR` hace pledges y procesa pago.
5. Se pueden reportar campañas por fraude.
6. `ADMIN`/`CREATOR` pueden exportar campañas (CSV, RSS, WEB).

## Endpoints identificados
En `develop` hay **34 endpoints** en total.

### 1) Auth (`/api/auth`) — 2 endpoints
- `POST /register`
- `POST /login`

### 2) Campaigns (`/api/campaigns`) — 12 endpoints
- `POST /` *(CREATOR)*
- `GET /`
- `GET /{id}`
- `GET /status/{status}`
- `GET /category/{category}`
- `GET /location/{location}`
- `GET /featured`
- `GET /creator/{creatorId}`
- `PUT /{id}` *(CREATOR)*
- `DELETE /{id}` *(CREATOR o ADMIN)*
- `PATCH /{id}/approve` *(ADMIN)*
- `PATCH /{id}/reject` *(ADMIN)*

### 3) Rewards (`/api/rewards`) — 5 endpoints
- `POST /` *(CREATOR)*
- `GET /?campaignId=...`
- `GET /{id}`
- `PUT /{id}` *(CREATOR)*
- `DELETE /{id}` *(CREATOR)*

### 4) Pledges (`/api/pledges`) — 4 endpoints
- `POST /` *(SPONSOR)*
- `GET /my` *(SPONSOR)*
- `GET /?campaignId=...` *(CREATOR o ADMIN)*
- `PATCH /{id}/refund` *(ADMIN)*

### 5) Payments (`/api/payments`) — 2 endpoints
- `POST /create-intent` *(SPONSOR)*
- `POST /confirm/{pledgeId}?paymentIntentId=...` *(SPONSOR)*

### 6) Fraud Reports (`/api/fraud-reports`) — 3 endpoints
- `POST /` *(usuario autenticado)*
- `GET /` *(ADMIN)*
- `PATCH /{id}/resolve` *(ADMIN)*

### 7) Admin (`/api/admin`) — 5 endpoints
- `GET /campaigns/pending`
- `PATCH /campaigns/{id}/approve`
- `PATCH /campaigns/{id}/reject`
- `GET /fraud-reports`
- `PATCH /fraud-reports/{id}/resolve`

### 8) Export (`/api/export`) — 1 endpoint
- `GET /{format}` *(ADMIN o CREATOR)*

## Stack principal
- Java 21
- Spring Boot 3.3
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Stripe SDK
- OpenCSV
