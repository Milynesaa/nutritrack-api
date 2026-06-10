# NutriTrack Backend - Documentación de API

## Descripción General
NutriTrack es un backend para una aplicación de nutrición que permite a pacientes, nutricionistas y administradores gestionar planes de alimentación, hábitos, objetivos y recibir asesoramiento de IA.

**Tecnologías:**
- Spring Boot 4.0.6
- Java 21
- PostgreSQL
- Spring Security con JWT
- Spring AI (HuggingFace)

---

## Roles de Usuario
El sistema tiene 3 roles definidos en el enum `Role`:
- `ROLE_PATIENT` - Paciente (usuario final)
- `ROLE_NUTRITIONIST` - Nutricionista (requiere aprobación del admin)
- `ROLE_ADMIN` - Administrador

**Estados de Usuario:**
- `ACTIVE` - Usuario activo
- `PENDING` - Nutricionista esperando aprobación
- `REJECTED` - Nutricionista rechazado

---

## Formato de Respuestas
Todas las respuestas siguen este formato:

```json
{
  "success": true,
  "message": "Mensaje descriptivo",
  "data": { ... }
}
```

---

## Autenticación
**Base URL:** `/api/auth`

### 1. Registrar Usuario
- **POST** `/api/auth/register`
- **Sin autenticación**
- **Body:**
```json
{
  "name": "string (requerido)",
  "email": "string (requerido, formato email)",
  "password": "string (requerido, mínimo 6 caracteres)",
  "role": "ROLE_PATIENT | ROLE_NUTRITIONIST | ROLE_ADMIN"
}
```
- **Respuesta:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "jwt_token",
    "refreshToken": "refresh_token",
    "role": "ROLE_PATIENT",
    "email": "user@email.com",
    "name": "User Name"
  }
}
```

### 2. Iniciar Sesión
- **POST** `/api/auth/login`
- **Sin autenticación**
- **Body:**
```json
{
  "email": "string (requerido, formato email)",
  "password": "string (requerido)"
}
```
- **Respuesta:** Igual que register

### 3. Olvidé Contraseña
- **POST** `/api/auth/forgot-password`
- **Sin autenticación**
- **Body:**
```json
{
  "email": "string"
}
```
- **Respuesta:** Envía email con token de recuperación

### 4. Restablecer Contraseña
- **POST** `/api/auth/reset-password`
- **Sin autenticación**
- **Body:**
```json
{
  "token": "string",
  "newPassword": "string"
}
```

### 5. Refresh Token
- **POST** `/api/auth/refresh-token`
- **Sin autenticación**
- **Body:**
```json
{
  "refreshToken": "string"
}
```

---

## Usuario Actual
**Base URL:** `/api/user`

### Obtener Usuario Actual
- **GET** `/api/user/me`
- **Requiere autenticación**
- **Respuesta:**
```json
{
  "success": true,
  "message": "User profile fetched successfully",
  "data": {
    "id": 1,
    "name": "string",
    "email": "string",
    "role": "ROLE_PATIENT",
    "status": "ACTIVE",
    "createdAt": "2024-01-01T00:00:00"
  }
}
```

---

## Dashboard
**Base URL:** `/api/dashboard`

### Obtener Dashboard
- **GET** `/api/dashboard`
- **Requiere autenticación**
- **Respuesta:** Datos personalizados según el rol del usuario

---

## Pacientes
**Base URL:** `/api/patient`

### 1. Obtener Perfil
- **GET** `/api/patient/profile`
- **Requiere autenticación (ROLE_PATIENT)**
- **Respuesta:**
```json
{
  "success": true,
  "message": "Patient profile fetched successfully",
  "data": {
    "id": 1,
    "name": "string",
    "email": "string",
    "age": 25,
    "weight": 70.5,
    "height": 175,
    "gender": "MALE | FEMALE",
    "activityLevel": "SEDENTARY | LIGHT | MODERATE | ACTIVE"
  }
}
```

### 2. Actualizar Perfil
- **PUT** `/api/patient/profile`
- **Requiere autenticación (ROLE_PATIENT)**
- **Body:** Igual que la respuesta de getProfile

---

## Comidas (Meals)
**Base URL:** `/api/patient/meals`

### 1. Crear Comida
- **POST** `/api/patient/meals`
- **Requiere autenticación (ROLE_PATIENT)**
- **Body:**
```json
{
  "title": "string (requerido)",
  "description": "string (opcional)",
  "calories": 500 (requerido),
  "type": "BREAKFAST | LUNCH | DINNER | SNACK (requerido)"
}
```
- **Respuesta:**
```json
{
  "success": true,
  "message": "Meal created successfully",
  "data": {
    "id": 1,
    "title": "string",
    "description": "string",
    "calories": 500,
    "type": "BREAKFAST",
    "createdAt": "2024-01-01T00:00:00"
  }
}
```

### 2. Obtener Comidas
- **GET** `/api/patient/meals`
- **Requiere autenticación (ROLE_PATIENT)**
- **Respuesta:** Array de MealResponse

### 3. Actualizar Comida
- **PUT** `/api/patient/meals/{id}`
- **Requiere autenticación (ROLE_PATIENT)**
- **Body:** Igual que create
- **Respuesta:** MealResponse actualizado

### 4. Eliminar Comida
- **DELETE** `/api/patient/meals/{id}`
- **Requiere autenticación (ROLE_PATIENT)**
- **Respuesta:** `{ "success": true, "message": "Meal deleted successfully", "data": "Deleted" }`

---

## Hábitos (Habits)
**Base URL:** `/api/patient/habits`

### 1. Crear Hábito
- **POST** `/api/patient/habits`
- **Requiere autenticación (ROLE_PATIENT)**
- **Body:**
```json
{
  "waterMl": 2000 (requerido),
  "sleepHours": 8 (requerido),
  "exerciseMinutes": 30 (requerido)
}
```
- **Respuesta:**
```json
{
  "success": true,
  "message": "Habit registered successfully",
  "data": {
    "id": 1,
    "waterMl": 2000,
    "sleepHours": 8,
    "exerciseMinutes": 30,
    "date": "2024-01-01"
  }
}
```

### 2. Obtener Hábitos
- **GET** `/api/patient/habits`
- **Requiere autenticación (ROLE_PATIENT)**
- **Respuesta:** Array de HabitResponse

### 3. Actualizar Hábito
- **PUT** `/api/patient/habits/{id}`
- **Requiere autenticación (ROLE_PATIENT)**
- **Body:** Igual que create
- **Respuesta:** HabitResponse actualizado

### 4. Eliminar Hábito
- **DELETE** `/api/patient/habits/{id}`
- **Requiere autenticación (ROLE_PATIENT)**
- **Respuesta:** `{ "success": true, "message": "Habit deleted successfully", "data": "Deleted" }`

---

## Objetivos (Goals)
**Base URL:** `/api/patient/goals`

### 1. Crear Objetivo
- **POST** `/api/patient/goals`
- **Requiere autenticación (ROLE_PATIENT)**
- **Body:**
```json
{
  "title": "string (requerido)",
  "description": "string (opcional)",
  "completed": false (requerido)
}
```
- **Respuesta:**
```json
{
  "success": true,
  "message": "Goal created successfully",
  "data": {
    "id": 1,
    "title": "string",
    "description": "string",
    "completed": false,
    "createdAt": "2024-01-01T00:00:00"
  }
}
```

### 2. Obtener Objetivos
- **GET** `/api/patient/goals`
- **Requiere autenticación (ROLE_PATIENT)**
- **Respuesta:** Array de GoalResponse

### 3. Actualizar Objetivo
- **PUT** `/api/patient/goals/{id}`
- **Requiere autenticación (ROLE_PATIENT)**
- **Body:** Igual que create
- **Respuesta:** GoalResponse actualizado

### 4. Eliminar Objetivo
- **DELETE** `/api/patient/goals/{id}`
- **Requiere autenticación (ROLE_PATIENT)**
- **Respuesta:** `{ "success": true, "message": "Goal deleted successfully", "data": "Deleted" }`

### 5. Toggle Completado
- **PATCH** `/api/patient/goals/{id}/toggle`
- **Requiere autenticación (ROLE_PATIENT)**
- **Respuesta:** GoalResponse con completed invertido

---

## Nutricionistas
**Base URL:** `/api/nutritionist`

### 1. Obtener Perfil
- **GET** `/api/nutritionist/profile`
- **Requiere autenticación (ROLE_NUTRITIONIST)**
- **Respuesta:**
```json
{
  "success": true,
  "message": "Nutritionist profile fetched successfully",
  "data": {
    "id": 1,
    "name": "string",
    "email": "string",
    "specialization": "string",
    "experience": 5,
    "licenseNumber": "string"
  }
}
```

### 2. Actualizar Perfil
- **PUT** `/api/nutritionist/profile`
- **Requiere autenticación (ROLE_NUTRITIONIST)**
- **Body:** Igual que la respuesta de getProfile

### 3. Obtener Pacientes
- **GET** `/api/nutritionist/patients`
- **Requiere autenticación (ROLE_NUTRITIONIST)**
- **Respuesta:** Array de UserResponse (pacientes asignados)

### 4. Obtener Comidas de Paciente
- **GET** `/api/nutritionist/patients/{patientId}/meals`
- **Requiere autenticación (ROLE_NUTRITIONIST)**
- **Respuesta:** Array de MealResponse del paciente

### 5. Obtener Hábitos de Paciente
- **GET** `/api/nutritionist/patients/{patientId}/habits`
- **Requiere autenticación (ROLE_NUTRITIONIST)**
- **Respuesta:** Array de HabitResponse del paciente

### 6. Obtener Objetivos de Paciente
- **GET** `/api/nutritionist/patients/{patientId}/goals`
- **Requiere autenticación (ROLE_NUTRITIONIST)**
- **Respuesta:** Array de GoalResponse del paciente

---

## Administradores
**Base URL:** `/api/admin`
**Todos los endpoints requieren ROLE_ADMIN**

### 1. Obtener Estadísticas
- **GET** `/api/admin/stats`
- **Respuesta:**
```json
{
  "success": true,
  "message": "Admin stats retrieved successfully",
  "data": {
    "totalPatients": 10,
    "totalNutritionists": 5,
    "pendingNutritionists": 2,
    "patientNames": ["Patient1", "Patient2"],
    "nutritionistStats": [
      {
        "id": 1,
        "name": "Nutritionist1",
        "email": "nutri1@email.com",
        "patientCount": 3,
        "efficiencyPercentage": 85.5,
        "status": "ACTIVE"
      }
    ]
  }
}
```

### 2. Obtener Nutricionistas Pendientes
- **GET** `/api/admin/nutritionists/pending`
- **Respuesta:** Array de UserResponse (nutricionistas con status PENDING)

### 3. Aprobar/Rechazar Nutricionista
- **POST** `/api/admin/nutritionists/approve`
- **Body:**
```json
{
  "nutritionistId": 1,
  "approved": true
}
```
- **Respuesta:** UserResponse actualizado

### 4. Obtener Todos los Nutricionistas
- **GET** `/api/admin/nutritionists`
- **Respuesta:** Array de UserResponse

### 5. Actualizar Nutricionista
- **PUT** `/api/admin/nutritionists`
- **Body:**
```json
{
  "nutritionistId": 1,
  "name": "Nuevo Nombre"
}
```
- **Respuesta:** UserResponse actualizado

### 6. Eliminar Nutricionista
- **DELETE** `/api/admin/nutritionists/{id}`
- **Respuesta:** `{ "success": true, "message": "Nutritionist deleted successfully", "data": "Deleted" }`

---

## Inteligencia Artificial
**Base URL:** `/api/ai`

### 1. Chat con IA
- **POST** `/api/ai/chat`
- **Requiere autenticación**
- **Body:**
```json
{
  "message": "string (requerido)"
}
```
- **Respuesta:**
```json
{
  "success": true,
  "message": "AI response generated",
  "data": {
    "response": "string (respuesta de la IA)",
    "timestamp": "2024-01-01T00:00:00"
  }
}
```

### 2. Obtener Historial de Chat
- **GET** `/api/ai/history`
- **Requiere autenticación**
- **Respuesta:**
```json
{
  "success": true,
  "message": "Chat history retrieved successfully",
  "data": [
    {
      "id": 1,
      "message": "string",
      "response": "string",
      "timestamp": "2024-01-01T00:00:00"
    }
  ]
}
```

---

## Headers de Autenticación
Para endpoints que requieren autenticación, incluye el header:

```
Authorization: Bearer {jwt_token}
```

---

## Errores Comunes
- **401 Unauthorized:** Token inválido o expirado
- **403 Forbidden:** No tienes permiso para acceder al recurso
- **404 Not Found:** Recurso no encontrado
- **400 Bad Request:** Datos inválidos en el request

---

## Notas Importantes
1. Los nutricionistas se registran con status `PENDING` y deben ser aprobados por un admin
2. Los pacientes con status `PENDING` o `REJECTED` no pueden iniciar sesión
3. Todos los timestamps están en formato ISO 8601
4. Los roles deben usar el prefijo `ROLE_` (ej: `ROLE_PATIENT`, no `PATIENT`)
5. La API usa JWT para autenticación con refresh tokens
