<div align="center">

# 🚀 Crowdfunding Platform — Backend

</div>

## 📖 Descripción

**Crowdfunding Platform** es una API REST robusta desarrollada con **Spring Boot** que permite gestionar campañas de financiamiento colectivo. Los creadores pueden publicar proyectos con metas fijas o flexibles, los patrocinadores pueden apoyar causas a cambio de recompensas exclusivas, y los administradores mantienen la integridad de la plataforma.

##

## ✨ Características

### 🔐 Seguridad
-  Autenticación **stateless con JWT**
-  Contraseñas encriptadas con **BCrypt**
-  Control de acceso por **roles** (ADMIN, CREATOR, SPONSOR)
-  Manejo global de excepciones con respuestas estandarizadas

### 📤 Exportación 
- Exportación en formato **CSV**
- Exportación en formato **XML/Web**
- Exportación en formato **RSS Feed**

### 💳 Pagos 
- Integración con **Stripe** para pagos en línea
- Creación de **PaymentIntents**
- Confirmación de pagos y actualización de pledges

##

## 🛠 Stack Tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Spring Boot | 4.0.x | Framework principal |
| Spring Security | 6.x | Seguridad y autorización |
| Spring Data JPA | 3.x | Persistencia de datos |
| PostgreSQL | 16 | Base de datos |
| JWT (jjwt) | 0.12.5 | Autenticación |
| Stripe Java SDK | 25.3.0 | Pagos en línea |
| OpenCSV | 5.9 | Exportación CSV |
| Lombok | Latest | Reducción de boilerplate |
| Maven | 3.x | Gestión de dependencias |

##

## 🏗 Arquitectura

El proyecto implementa una **Arquitectura N-Capas**:

```
com.example.crowdfundingplatform/
│
├── 🔧 config/           → Configuración global (SecurityConfig)
├── 🎮 controller/       → Capa de Presentación (API REST)
│
├── 📦 domain/
│   ├── dto/
│   │   ├── request/     → DTOs de entrada (CreateCampaignRequest, etc.)
│   │   └── response/    → DTOs de salida (CampaignDetailResponse, etc.)
│   ├── entity/          → Entidades JPA (Campaign, User, Pledge, etc.)
│   └── enums/           → Enumeraciones (Role, CampaignStatus, GoalType)
│
├── ⚠️  exception/        → Manejo global de excepciones
├── 📤 export/           → Patrón Strategy (CSV, Web, RSS)
├── 🔄 mapper/           → Conversión Entity ↔ DTO
├── 🗄️  repository/       → Capa de Acceso a Datos (Spring Data JPA)
├── 🔒 security/         → JWT Filter, JwtUtil, JwtAuth
│
└── ⚙️  service/          → Lógica de Negocio
    └── implService/     → Implementaciones de servicios
```

##

## 🚀 Instalación

### Prerrequisitos
- Java 21+
- Maven 3.x
- PostgreSQL 16+
- IntelliJ IDEA (recomendado)

### Pasos

**1. Clonar el repositorio:**
```bash
git clone https://github.com/AstridVides-cyber/Backend-CrowdfundingPlatform.git
cd Backend-CrowdfundingPlatform/CrowdfundingPlatform
```

**2. Crear la base de datos:**
```sql
CREATE DATABASE crowdfunding_db;
```

**3. Configurar variables de entorno en IntelliJ:**
```
Run → Edit Configurations → Environment Variables
```

La API arranca en HTTPS por defecto. Si el keystore no existe, la app lo genera sola al arrancar en `ssl/crowdfunding-platform.jks`.

Variables opcionales:
- `SSL_KEY_STORE=/ruta/al/crowdfunding-platform.jks`
- `SSL_KEY_STORE_PASSWORD=tu_password`
- `SSL_KEY_PASSWORD=tu_password` (si es distinto)
- `SSL_KEY_STORE_TYPE=JKS`
- `SSL_KEY_ALIAS=crowdfunding-platform`
- `SERVER_PORT=8443` (por defecto)

**4. Correr el proyecto:**
```bash
./mvnw spring-boot:run
```

La API estará disponible en: `https://localhost:8443`

##

## 🔑 Variables de Entorno

| Variable | Valor | Descripción |
|---|---|---|
| `DB_URL` | jdbc:postgresql://localhost:5432/crowdfunding_db |URL de conexión a PostgreSQL |
| `DB_USER` | tu_usuario |Usuario de la base de datos |
| `DB_PASSWORD` | tu_contraseña |Contraseña de la base de datos |
| `SK_TEST` | sk_test_tu_clave_stripe |Clave secreta de Stripe (modo test) |
| `PK_TEST` | pk_test_tu_clave_stripe |Clave pública de Stripe (modo test) |
| `JWT_SECRET` | jwt_secret_test_tu_clave |Clave secreta de JWT |
| `JWT_EXPIRATION` | 3600000  |Tiempo de expiración |
| `SSL_ENABLED` | true/false | Activa HTTPS con keystore |
| `SSL_KEY_STORE` | ssl/crowdfunding-platform.jks | Ruta al keystore |
| `SSL_KEY_STORE_PASSWORD` | changeit | Contraseña del keystore |
| `SSL_KEY_PASSWORD` | changeit | Contraseña de la clave privada |
| `SSL_KEY_STORE_TYPE` | JKS | Tipo de keystore |
| `SSL_KEY_ALIAS` | crowdfunding-platform | Alias del certificado |
| `SERVER_PORT` | 8443 | Puerto HTTPS/HTTP |

##

## 📡 Endpoints

## 🔓 Auth 
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registro de usuario |
| POST | `/api/auth/login` | Inicio de sesión |

## 🏕️ Campaigns
| Método | Ruta | Rol | Descripción |
|--------|------|-----|-------------|
| POST | `/api/campaigns/create` | CREATOR | Crear campaña |
| GET | `/api/campaigns/allCampaigns` | Autenticado | Listar campañas |
| GET | `/api/campaigns/{id}` | Autenticado | Detalle de campaña |
| PUT | `/api/campaigns/{id}` | CREATOR | Actualizar campaña |
| DELETE | `/api/campaigns/{id}` | CREATOR/ADMIN | Eliminar campaña |
| GET | `/api/campaigns/featured` | Autenticado | Campañas destacadas |
| GET | `/api/campaigns/category/{id}` | Autenticado | Filtrar por categoría |
| GET | `/api/campaigns/location/{id}` | Autenticado | Filtrar por ubicación |
| GET | `/api/campaigns/status/{status}` | Autenticado | Filtrar por estado |
| PATCH | `/api/campaigns/{id}/approve` | ADMIN | Aprobar campaña |
| PATCH | `/api/campaigns/{id}/reject` | ADMIN | Rechazar campaña |

## 💰 Pledges
| Método | Ruta | Rol | Descripción |
|--------|------|-----|-------------|
| POST | `/api/pledges/create` | SPONSOR | Crear pledge |
| GET | `/api/pledges/my` | SPONSOR | Mis pledges |
| GET | `/api/pledges?campaignId={id}` | CREATOR/ADMIN | Pledges de campaña |
| PATCH | `/api/pledges/{id}/refund` | ADMIN | Reembolsar pledge |

## 🎁 Rewards
| Método | Ruta | Rol | Descripción |
|--------|------|-----|-------------|
| POST | `/api/rewards` | CREATOR | Crear recompensa |
| GET | `/api/rewards/{id}` | Autenticado | Detalle de recompensa |
| GET | `/api/rewards/campaign/{id}` | Autenticado | Recompensas de campaña |
| PUT | `/api/rewards/{id}` | CREATOR | Actualizar recompensa |
| DELETE | `/api/rewards/{id}` | CREATOR | Eliminar recompensa |

## 🚨 Fraud Reports
| Método | Ruta | Rol | Descripción |
|--------|------|-----|-------------|
| POST | `/api/fraud-reports` | Autenticado | Reportar campaña |
| GET | `/api/fraud-reports` | ADMIN | Listar reportes sin resolver |
| PATCH | `/api/fraud-reports/{id}/resolve` | ADMIN | Resolver reporte |

## 👮 Admin
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/campaigns/pending` | Campañas pendientes |
| PATCH | `/api/admin/campaigns/{id}/approve` | Aprobar campaña |
| PATCH | `/api/admin/campaigns/{id}/reject` | Rechazar campaña |
| GET | `/api/admin/fraud-reports` | Reportes sin resolver |
| PATCH | `/api/admin/fraud-reports/{id}/resolve` | Resolver reporte |

## 📤 Export
| Método | Ruta | Rol | Descripción |
|--------|------|-----|-------------|
| GET | `/api/export/CSV` | ADMIN/CREATOR | Exportar en CSV |
| GET | `/api/export/WEB` | ADMIN/CREATOR | Exportar en XML |
| GET | `/api/export/RSS` | ADMIN/CREATOR | Exportar en RSS |

## 💳 Payments
| Método | Ruta | Rol | Descripción |
|--------|------|-----|-------------|
| POST | `/api/payments/create-intent` | SPONSOR | Crear PaymentIntent |
| POST | `/api/payments/confirm/{id}` | SPONSOR | Confirmar pago |
<div align="center">
    
##

**Desarrollado con Spring Boot :D**


</div>
