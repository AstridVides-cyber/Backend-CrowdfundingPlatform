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

🔓 Auth (Público)
MétodoRutaDescripciónPOST/api/auth/registerRegistro de usuarioPOST/api/auth/loginInicio de sesión
🏕️ Campaigns
MétodoRutaRolDescripciónPOST/api/campaigns/createCREATORCrear campañaGET/api/campaigns/allCampaignsAutenticadoListar campañasGET/api/campaigns/{id}AutenticadoDetalle de campañaPUT/api/campaigns/{id}CREATORActualizar campañaDELETE/api/campaigns/{id}CREATOR/ADMINEliminar campañaGET/api/campaigns/featuredAutenticadoCampañas destacadasGET/api/campaigns/category/{cat}AutenticadoFiltrar por categoríaGET/api/campaigns/location/{loc}AutenticadoFiltrar por ubicaciónGET/api/campaigns/status/{status}AutenticadoFiltrar por estadoPATCH/api/campaigns/{id}/approveADMINAprobar campañaPATCH/api/campaigns/{id}/rejectADMINRechazar campaña
💰 Pledges
MétodoRutaRolDescripciónPOST/api/pledges/createSPONSORCrear pledgeGET/api/pledges/mySPONSORMis pledgesGET/api/pledges?campaignId={id}CREATOR/ADMINPledges de campañaPATCH/api/pledges/{id}/refundADMINReembolsar pledge
🎁 Rewards
MétodoRutaRolDescripciónPOST/api/rewardsCREATORCrear recompensaGET/api/rewards/{id}AutenticadoDetalle de recompensaGET/api/rewards/campaign/{id}AutenticadoRecompensas de campañaPUT/api/rewards/{id}CREATORActualizar recompensaDELETE/api/rewards/{id}CREATOREliminar recompensa
🚨 Fraud Reports
MétodoRutaRolDescripciónPOST/api/fraud-reportsAutenticadoReportar campañaGET/api/fraud-reportsADMINListar reportes sin resolverPATCH/api/fraud-reports/{id}/resolveADMINResolver reporte
👮 Admin
MétodoRutaDescripciónGET/api/admin/campaigns/pendingCampañas pendientesPATCH/api/admin/campaigns/{id}/approveAprobar campañaPATCH/api/admin/campaigns/{id}/rejectRechazar campañaGET/api/admin/fraud-reportsReportes sin resolverPATCH/api/admin/fraud-reports/{id}/resolveResolver reporte
📤 Export
MétodoRutaRolDescripciónGET/api/export/CSVADMIN/CREATORExportar en CSVGET/api/export/WEBADMIN/CREATORExportar en XMLGET/api/export/RSSADMIN/CREATORExportar en RSS
💳 Payments
MétodoRutaRolDescripciónPOST/api/payments/create-intentSPONSORCrear PaymentIntentPOST/api/payments/confirm/{pledgeId}?paymentIntentId={id}SPONSORConfirmar pago

<div align="center">

**Desarrollado con Spring Boot :D**


</div>
