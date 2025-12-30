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

### Render
1. Connect GitHub repository
2. Create PostgreSQL database
3. Set environment variables:
   - `DATABASE_URL`
   - `JWT_SECRET`
   - `PORT` (auto-set by Render)

### Railway
1. Connect repository
2. Add PostgreSQL service
3. Set environment variables
4. Deploy

## Environment Variables
- `DATABASE_URL`: PostgreSQL connection string
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT tokens (min 256 bits)
- `PORT`: Server port (default: 8080)


