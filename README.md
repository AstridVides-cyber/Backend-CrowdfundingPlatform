# Backend CrowdfundingPlatform

## Resumen del proyecto
Backend de una plataforma de crowdfunding para gestionar campañas de recaudación, recompensas, aportes financieros y moderación administrativa con estas capacidades principales:

- Gestión completa de campañas y recompensas.
- Procesamiento seguro de pagos con Stripe.
- Autenticación y autorización por roles con JWT.
- Moderación de campañas y manejo de reportes de fraude.
- Exportación de datos del negocio en CSV, RSS y WEB.

## Arquitectura
El proyecto sigue una arquitectura monolítica limpia, organizada por capas para separar responsabilidades y mantener el código escalable:

- `config`: configuraciones globales, seguridad y CORS.
- `controller`: endpoints REST y mapeo de entrada y salida.
- `domain`: entidades, enums y DTOs.
- `repository`: acceso a datos con Spring Data JPA.
- `security`: filtros JWT, autenticación y utilidades de seguridad.
- `service`: reglas de negocio e integraciones externas.
- `export`: estrategias de exportación de datos.

## Roles y permisos
La plataforma maneja tres roles principales:

- `ADMIN`: administra la plataforma, modera campañas, resuelve fraudes y gestiona reembolsos.
- `CREATOR`: crea y administra campañas y recompensas propias, además de acceder a exportaciones.
- `SPONSOR`: descubre campañas, crea pledges y completa pagos de forma segura.

Reglas generales de acceso:

- `/api/auth/**` es público para registro e inicio de sesión.
- Las rutas protegidas requieren token JWT.
- Las acciones administrativas están restringidas a `ADMIN`.

## Módulos funcionales

- Campañas: CRUD completo, filtros por estado y visibilidad destacada.
- Recompensas: creación, edición y eliminación asociadas a campañas.
- Pledges: registro de aportes y seguimiento del estado de financiamiento.
- Pagos: creación y confirmación de Payment Intents con Stripe.
- Fraude: reportes de campañas sospechosas y resolución administrativa.
- Exportación: generación de salidas en CSV, RSS y WEB.

## Stack principal

- Java 21
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Stripe SDK
- OpenCSV
