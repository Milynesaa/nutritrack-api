# NutriTrack Backend

Sistema de seguimiento nutricional con IA, autenticación JWT y gestión de pacientes y nutricionistas.

## 🚀 Tecnologías

- Java 21
- Spring Boot 4.0.6
- Spring Security + JWT (con rotación de Refresh Tokens)
- PostgreSQL
- Spring AI (Anthropic, DeepSeek, OpenAI, HuggingFace)
- Maven
- Lombok (@Slf4j para logging)
- Swagger/OpenAPI

## 📋 Requisitos Previos

- Java 21 o superior
- Maven 3.8+
- PostgreSQL 14+
- Cuenta de Gmail con App Password (para recuperación de contraseña)

## 🔧 Configuración Local

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd nutritrack-backend
```

### 2. Configurar base de datos PostgreSQL

```sql
CREATE DATABASE nutritrack;
```

### 3. Configurar variables de entorno

Crea un archivo `.env` o configura las siguientes variables en tu IDE:

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/nutritrack
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=tu_password
JWT_SECRET=tu_secret_key_muy_larga_para_hs256
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=tu_email@gmail.com
SMTP_PASSWORD=tu_app_password
FRONTEND_RESET_PASSWORD_URL=http://localhost:3000/reset-password
DDL_AUTO=update
```

### 4. Configurar Gmail SMTP

1. Ve a tu cuenta de Google
2. Activa la autenticación de dos pasos
3. Genera una "App Password" en: https://myaccount.google.com/apppasswords
4. Usa esa contraseña en la variable `SMTP_PASSWORD`

### 5. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## 📚 Documentación API

Swagger UI disponible en: `http://localhost:8080/swagger-ui.html`

### Endpoints Principales

#### Autenticación
- `POST /api/auth/register` - Registro de usuario
- `POST /api/auth/login` - Login con JWT
- `POST /api/auth/refresh-token` - Renovar access token
- `POST /api/auth/forgot-password` - Solicitar recuperación de contraseña
- `POST /api/auth/reset-password` - Restablecer contraseña

#### Dashboard
- `GET /api/dashboard` - Obtener resumen del usuario (requiere autenticación)

#### Meals
- `POST /api/meals` - Crear registro de comida
- `GET /api/meals` - Listar comidas del usuario
- `PUT /api/meals/{id}` - Actualizar comida
- `DELETE /api/meals/{id}` - Eliminar comida

#### Habits
- `POST /api/habits` - Crear hábito
- `GET /api/habits` - Listar hábitos del usuario
- `PUT /api/habits/{id}` - Actualizar hábito
- `DELETE /api/habits/{id}` - Eliminar hábito

#### AI
- `POST /api/ai/chat` - Chat con asistente de IA

## 🚀 Deployment en Producción

### Railway

1. Crear nuevo proyecto en Railway
2. Conectar repositorio GitHub
3. Configurar variables de entorno en Railway:

```env
DATABASE_URL=postgresql://user:password@host:port/database
DATABASE_USERNAME=user
DATABASE_PASSWORD=password
JWT_SECRET=tu_secret_key_muy_larga_para_hs256
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=tu_email@gmail.com
SMTP_PASSWORD=tu_app_password
DDL_AUTO=validate
```

4. Railway detectará automáticamente que es una aplicación Spring Boot
5. Deploy automático al hacer push a main

### Render

1. Crear nuevo Web Service en Render
2. Conectar repositorio GitHub
3. Configurar build command: `mvn clean package`
4. Configurar start command: `java -jar target/nutritrack-backend-0.0.1-SNAPSHOT.jar`
5. Configurar variables de entorno
6. Deploy

### Variables de Entorno Requeridas

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| DATABASE_URL | URL de conexión PostgreSQL | `jdbc:postgresql://...` |
| DATABASE_USERNAME | Usuario de BD | `postgres` |
| DATABASE_PASSWORD | Contraseña de BD | `password123` |
| JWT_SECRET | Secret key para JWT (mínimo 256 bits) | `my-very-long-secret-key...` |
| JWT_EXPIRATION | Expiración access token (ms) | `86400000` (24h) |
| JWT_REFRESH_EXPIRATION | Expiración refresh token (ms) | `604800000` (7d) |
| SMTP_HOST | Host SMTP | `smtp.gmail.com` |
| SMTP_PORT | Puerto SMTP | `587` |
| SMTP_USERNAME | Email para envío | `email@gmail.com` |
| SMTP_PASSWORD | App Password de Gmail | `abcd efgh ijkl mnop` |
| FRONTEND_RESET_PASSWORD_URL | URL del frontend para resetear password | `http://localhost:3000/reset-password` |
| DDL_AUTO | Estrategia de DDL | `update` (dev) / `validate` (prod) |

## 🔐 Seguridad

- JWT con expiración configurable
- **Refresh tokens con rotación segura** - Tokens antiguos se invalidan al crear uno nuevo
- **Generación de tokens mejorada** - Usa SecureRandom con 256 bits de entropía
- Password encoding con BCrypt
- Protección de endpoints por roles (PATIENT, NUTRITIONIST, ADMIN)
- CORS configurado
- Validación de inputs
- Manejo de excepciones específicas (ResourceNotFoundException, UnauthorizedException, EmailServiceException)

## 📊 Logs

Los logs están configurados para:
- Console logging en desarrollo
- File logging en producción
- Niveles configurables por paquete
- Formato estructurado
- **Usando @Slf4j de Lombok** (reemplazó LogUtil)

## 🧪 Testing

```bash
mvn test
```

## 📁 Estructura del Proyecto

```
nutritrack-backend/
├── src/main/java/com/nutritrack/
│   ├── config/           # Configuraciones (Security, JWT, Mail, Swagger)
│   ├── controller/       # Controladores REST
│   ├── dto/              # Data Transfer Objects
│   ├── entity/           # Entidades JPA
│   ├── exception/        # Manejo de excepciones
│   ├── repository/       # Repositorios JPA
│   ├── service/          # Lógica de negocio
│   └── util/             # Utilidades (LogUtil, TokenGenerator)
├── src/main/resources/
│   ├── application.yaml          # Configuración desarrollo
│   └── application-prod.yml     # Configuración producción
└── pom.xml
```

## 🎯 Características Implementadas

### ✅ Auth & Security
- Login con JWT
- Registro de usuario
- Password encoder (BCrypt)
- UserDetailsService conectado a BD
- JWT Filter funcional
- Roles (PATIENT / NUTRITIONIST)
- Protección de endpoints por roles

### ✅ Refresh Tokens
- Entity refresh token
- Repository
- Endpoint refresh
- Expiración access token (24h)
- Expiración refresh token (7d)
- Rotación segura

### ✅ Swagger
- Swagger UI activo
- Documentación completa
- JWT en Swagger
- Organización de endpoints

### ✅ Logs
- Logs en servicios
- Logs de errores
- Logs de auth
- SLF4J configurado

### ✅ Email Recovery
- Forgot password
- Reset password
- Token de recuperación (15 min)
- Expiración de token
- Email Gmail SMTP funcionando

### ✅ Arquitectura
- Controller / Service / Repository correcto
- DTOs usados correctamente
- No lógica en controllers
- Código escalable
- GlobalExceptionHandler

### ✅ Funcionalidades Core
- CRUD Meals
- CRUD Habits
- Perfil usuario
- Dashboard básico
- IA (Hugging Face, Anthropic, DeepSeek, OpenAI)

### ✅ Deployment Ready
- Variables de entorno
- Profile prod configurado
- PostgreSQL externo preparado
- Listo para Railway / Render

## 🔧 Troubleshooting

### Error de conexión a BD
Verifica que PostgreSQL esté corriendo y que las credenciales sean correctas.

### Error de JWT
Asegúrate de que `JWT_SECRET` tenga al menos 256 bits de longitud.

### Error de email
Verifica que hayas generado una App Password válida en tu cuenta de Google.

## 📝 Licencia

MIT License

## 👥 Equipo

NutriTrack Team
