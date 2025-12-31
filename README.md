# DigiBank - Smart City Digital Banking Module

## Overview
DigiBank is a digital banking module that enables fiat and cryptocurrency payments for smart city services such as parking, transportation, and utilities.

## Technology Stack
- **Backend**: Java 17 + Spring Boot 3.2.0
- **Database**: PostgreSQL
- **Authentication**: JWT
- **Deployment**: Render/Railway

## Design Patterns Implemented
1. **Singleton**: CityController - Central system controller
2. **Command**: PaymentCommand - Payment execution encapsulation
3. **Observer**: NotificationObserver - Event notifications
4. **Adapter**: CryptoPaymentAdapter - Multi-blockchain integration
5. **Template Method**: SecurityCheckTemplate - Standardized security checks

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Payments
- `POST /api/pay/fiat` - Process fiat payment
- `POST /api/pay/crypto` - Process crypto payment

### Account
- `GET /api/account/balance` - Get account balance

### Transactions
- `GET /api/transactions` - Get user transactions
- `GET /api/transactions/{id}` - Get specific transaction

### Metrics (Admin Only)
- `GET /api/metrics` - System metrics
- `GET /api/metrics/logs` - Audit logs

## Running Locally

1. Install PostgreSQL and create database:
```sql
CREATE DATABASE digibank;
```

2. Update `application.properties` with database credentials

3. Build and run:
```bash
mvn clean package
java -jar target/digibank-1.0.0.jar
```

## Deployment

### Deployment on Render (Docker + PostgreSQL)

#### Prerequisites
1. Connect your GitHub repository to Render
2. Create a managed PostgreSQL database on Render
3. Note the database connection details

#### Required Environment Variables

Configure these in your Render service settings:

**JDBC_DATABASE_URL** (Required)
- Must be in JDBC format for PostgreSQL driver compatibility
- Render provides `DATABASE_URL` in format: `postgresql://user:pass@host:5432/db`
- Convert to JDBC format: `jdbc:postgresql://host:5432/db`
- Example conversion:
  ```
  Render Internal DB URL: postgresql://user:pass@dpg-xxxxx-a.oregon-postgres.render.com:5432/digibank
  Convert to: jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/digibank
  ```
- **Note**: Extract credentials separately (see below)

**DB_USERNAME**
- Database username from Render PostgreSQL service
- Example: `digibank_user`

**DB_PASSWORD**
- Database password from Render PostgreSQL service

**JWT_SECRET**
- Secret key for JWT token signing (minimum 32 characters)
- Generate with: `openssl rand -base64 32`
- Example: `aB3dEf9gHiJkLmNoPqRsTuVwXyZ1234567890AbCdEfGhIjKlMnOpQrStUvWxYz`

**PORT**
- **Do NOT set manually** - Render provides this automatically
- Application listens on `${PORT:8080}`

#### Deployment Steps

1. **Create Web Service**
   - Select "Docker" environment
   - Point to your repository
   - Set Dockerfile path (if not root): `./Dockerfile`

2. **Create PostgreSQL Database**
   - Add PostgreSQL service
   - Note connection details

3. **Configure Environment Variables**
   - Set `JDBC_DATABASE_URL` (converted from Render's `DATABASE_URL`)
   - Set `DB_USERNAME` and `DB_PASSWORD`
   - Set `JWT_SECRET` (generate strong secret)
   - **Do not set `PORT`** (auto-provided by Render)

4. **Deploy**
   - Render will build from Dockerfile
   - Health check endpoint: `/actuator/health`
   - Should return: `{"status":"UP"}`

#### Important Notes
- `DATABASE_URL` alone is not sufficient - PostgreSQL JDBC driver requires JDBC format
- Always use `JDBC_DATABASE_URL` for reliable connection
- Application falls back to `DATABASE_URL` if `JDBC_DATABASE_URL` is not set
- For local development, defaults to `localhost:5432/digibank`

### Railway
1. Connect repository
2. Add PostgreSQL service
3. Set environment variables
4. Deploy

## Environment Variables
- `JDBC_DATABASE_URL`: PostgreSQL connection string in JDBC format (preferred)
- `DATABASE_URL`: PostgreSQL connection string (fallback, requires JDBC conversion)
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT tokens (minimum 32 characters)
- `PORT`: Server port (auto-set by Render, default: 8080)


