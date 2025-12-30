# DigiBank Deployment Checklist

## Pre-Deployment

- [ ] Review all code files
- [ ] Verify environment variables are configured
- [ ] Test locally with PostgreSQL
- [ ] Generate UML diagrams (PNG/PDF)
- [ ] Take screenshots of GUI mockups
- [ ] Review IEEE report for completeness

## Database Setup

- [ ] Create PostgreSQL database on Render/Railway
- [ ] Note database connection string
- [ ] Verify database credentials
- [ ] Test database connection

## Environment Variables

Configure these in your cloud platform:

- [ ] `DATABASE_URL` - PostgreSQL connection string
- [ ] `DB_USERNAME` - Database username (if separate)
- [ ] `DB_PASSWORD` - Database password (if separate)
- [ ] `JWT_SECRET` - Strong secret key (min 256 bits)
  - Generate with: `openssl rand -base64 32`
- [ ] `PORT` - Usually auto-set by platform (8080)

## Deployment Steps

### Render

- [ ] Connect GitHub repository
- [ ] Create new Web Service
- [ ] Select "Java" environment
- [ ] Set build command: `mvn clean package -DskipTests`
- [ ] Set start command: `java -jar target/digibank-1.0.0.jar`
- [ ] Add PostgreSQL database
- [ ] Configure environment variables
- [ ] Deploy service
- [ ] Wait for build to complete

### Railway

- [ ] Connect GitHub repository
- [ ] Add PostgreSQL service
- [ ] Add Java service
- [ ] Configure environment variables
- [ ] Deploy

## Post-Deployment Verification

### 1. Health Check

- [ ] Visit: `https://your-app.onrender.com/actuator/health`
- [ ] Should return: `{"status":"UP"}`
- [ ] **Screenshot**: Health check response

### 2. API Testing

#### Registration

- [ ] `POST /api/auth/register`
  ```json
  {
    "username": "test_user",
    "email": "test@example.com",
    "password": "test123",
    "role": "RESIDENT"
  }
  ```
- [ ] **Screenshot**: Registration response

#### Login

- [ ] `POST /api/auth/login`
  ```json
  {
    "username": "test_user",
    "password": "test123"
  }
  ```
- [ ] Save JWT token
- [ ] **Screenshot**: Login response with token

#### Get Balance

- [ ] `GET /api/account/balance`
- [ ] Header: `Authorization: Bearer <token>`
- [ ] **Screenshot**: Balance response

#### Fiat Payment

- [ ] `POST /api/pay/fiat`
- [ ] Header: `Authorization: Bearer <token>`
- [ ] Body:
  ```json
  {
    "paymentType": "FIAT",
    "amount": 10.0,
    "serviceType": "PARKING",
    "description": "Test payment"
  }
  ```
- [ ] **Screenshot**: Payment response

#### Crypto Payment

- [ ] `POST /api/pay/crypto`
- [ ] Header: `Authorization: Bearer <token>`
- [ ] Body:
  ```json
  {
    "paymentType": "CRYPTO",
    "amount": 0.01,
    "cryptoNetwork": "ETH",
    "serviceType": "PARKING",
    "description": "Test crypto payment"
  }
  ```
- [ ] **Screenshot**: Crypto payment response

#### Get Transactions

- [ ] `GET /api/transactions`
- [ ] Header: `Authorization: Bearer <token>`
- [ ] **Screenshot**: Transactions list

#### Admin Metrics (if admin user created)

- [ ] `GET /api/metrics`
- [ ] Header: `Authorization: Bearer <admin_token>`
- [ ] **Screenshot**: Metrics response

### 3. Logs Verification

- [ ] Access cloud platform logs
- [ ] Verify application started successfully
- [ ] Check for any errors
- [ ] **Screenshot**: Application logs

### 4. Database Verification

- [ ] Connect to database (if possible)
- [ ] Verify tables created: users, accounts, transactions, audit_logs
- [ ] Check data inserted from test requests
- [ ] **Screenshot**: Database schema (no secrets visible)

## Screenshots Required for Submission

1. ✅ Deployed service URL (browser showing health check)
2. ✅ API responses (Postman or browser dev tools)
3. ✅ Application logs (cloud console)
4. ✅ Database configuration (no secrets)
5. ✅ GUI mockups (HTML files opened in browser)

## Troubleshooting

### Build Fails

- Check Maven dependencies in `pom.xml`
- Verify Java version (17 required)
- Check build logs for errors

### Database Connection Fails

- Verify `DATABASE_URL` format
- Check database credentials
- Ensure database is accessible from platform

### Application Won't Start

- Check `PORT` environment variable
- Review application logs
- Verify JWT_SECRET is set

### API Returns 401

- Verify JWT token is valid
- Check token expiration
- Ensure Authorization header format: `Bearer <token>`

## Final Checklist

- [ ] All API endpoints tested
- [ ] All screenshots captured
- [ ] UML diagrams generated
- [ ] GUI mockups screenshotted
- [ ] IEEE report complete
- [ ] Deployment documentation updated
- [ ] Project ready for submission

---

**Note**: Keep all screenshots organized in a folder for easy inclusion in submission package.
