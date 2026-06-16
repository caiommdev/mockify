# Mockify — Music Streaming API

A Domain-Driven Design (DDD) multi-module Spring Boot application simulating the backend of a music streaming platform. All requests go through a single API Gateway on port **8080**.

---

## Architecture

```
                        ┌──────────────────┐
                        │   API Gateway    │  :8080
                        │  (Spring WebFlux)│
                        └────────┬─────────┘
                                 │
          ┌──────────────────────┼──────────────────────┐
          │              ┌───────┴───────┐              │
          ▼              ▼               ▼              ▼
   account-service  transaction-service  favorite-service  ...
      :8081              :8082              :8083
                                        playlist-service  subscription-service
                                           :8084              :8085
```

| Module               | Port | Responsibility                              |
|----------------------|------|---------------------------------------------|
| `api-gateway`        | 8080 | Central entry point — routes all requests   |
| `account-service`    | 8081 | Account and credit card management          |
| `transaction-service`| 8082 | Transaction authorization and fraud rules   |
| `favorite-service`   | 8083 | Songs catalog and favorite management       |
| `playlist-service`   | 8084 | Playlist creation and song ordering         |
| `subscription-service`| 8085| Subscription plan management               |

---

## Running the Application

### Prerequisites

- Docker and Docker Compose

### Start all services

```bash
docker compose up -d
```

All services start in dependency order. The gateway becomes available after all services pass their health checks (approximately 60 seconds on first run).

### Stop all services

```bash
docker compose down
```

### Rebuild a specific service after code changes

```bash
docker compose build <service-name>
docker compose up -d <service-name>

# Example:
docker compose build transaction-service
docker compose up -d transaction-service
```

---

## Manual Testing

All requests below use **port 8080** (the gateway). No need to call individual services directly.

### Suggested flow

1. Create account
2. Register credit card
3. Subscribe to a plan
4. Browse songs and add favorites
5. Create playlists and add songs
6. Authorize transactions

---

### Accounts

#### Create account

##### POST http://localhost:8080/api/v1/
```json
  {
    "email": "user@mockify.com",
    "fullName": "John Doe"
  }
```

**Response `201`**
```json
{
  "id": "a1b2c3d4-...",
  "email": "user@mockify.com",
  "fullName": "John Doe",
  "creditCard": null
}
```

---

#### Get account

##### http://localhost:8080/api/v1/accounts/{accountId}

---

#### Get account status

##### http://localhost:8080/api/v1/accounts/{accountId}/status


**Response `200`**
```json
{
  "accountId": "a1b2c3d4-...",
  "hasActiveCreditCard": true
}
```

---

#### Register credit card
##### POST http://localhost:8080/api/v1/accounts/{accountId}/credit-card
```json
  {
    "cardNumber": "4111111111111111",
    "expiryMonth": 12,
    "expiryYear": 2028,
    "holderName": "John Doe"
  }
```

**Response `200`** — returns the updated account with masked card number.

---

### Subscriptions

Available plans: `FREE`, `PREMIUM`, `FAMILY`

#### Subscribe to a plan
##### POST http://localhost:8080/api/v1/accounts/{accountId}/subscriptions
```json
  { 
    "plan": "PREMIUM" 
  }
```

**Response `201`**
```json
{
  "id": "...",
  "accountId": "...",
  "plan": "PREMIUM",
  "status": "ACTIVE",
  "startDate": "2026-06-15",
  "active": true
}
```

---

#### Get active subscription

##### http://localhost:8080/api/v1/accounts/{accountId}/subscriptions/active


---

#### Get subscription status
##### http://localhost:8080/api/v1/accounts/{accountId}/subscriptions/status


---

#### Cancel subscription

##### DELETE http://localhost:8080/api/v1/accounts/{accountId}/subscriptions/active


**Response `204 No Content`**

---

### Transactions

Transaction authorization applies fraud rules:

| Rule                        | Condition                                                        |
|-----------------------------|------------------------------------------------------------------|
| `card-not-active`           | Account has no active credit card                               |
| `duplicate-transaction`     | More than 2 transactions with same amount + merchant in 2 min   |
| `high-frequency-small-interval` | More than 3 transactions for the same account in 2 min      |

#### Authorize transaction

##### POST http://localhost:8080/api/v1/transactions \

```json
  {
    "accountId": "{accountId}",
    "amount": 49.90,
    "currency": "BRL",
    "merchantId": "SPOT-001",
    "merchantName": "Spotify"
  }
```

**Response `200` — Approved**
```json
{
  "id": "...",
  "accountId": "...",
  "amount": 49.90,
  "currency": "BRL",
  "merchantId": "SPOT-001",
  "merchantName": "Spotify",
  "status": "APPROVED",
  "violations": [],
  "occurredAt": "2026-06-15T11:00:00Z"
}
```

**Response `200` — Denied**
```json
{
  "status": "DENIED",
  "violations": ["duplicate-transaction"]
}
```

---

#### Get transaction by ID

##### GET http://localhost:8080/api/v1/transactions/{transactionId}


---

#### List transactions by account

##### GET http://localhost:8080/api/v1/transactions?accountId={accountId}


---

### Songs

#### List all songs

##### GET http://localhost:8080/api/v1/songs

**Response `200`**
```json
[
  {
    "id": "2946a362-...",
    "title": "Bohemian Rhapsody",
    "artist": "Queen",
    "durationMs": 354000,
    "genre": "ROCK"
  }
]
```

---

#### Get song by ID

##### GET http://localhost:8080/api/v1/songs/{songId}

---

### Favorites

#### List favorites

##### GET http://localhost:8080/api/v1/accounts/{accountId}/favorites


---

#### Add song to favorites

##### POST http://localhost:8080/api/v1/accounts/{accountId}/favorites
```json
  { 
    "songId": "{songId}" 
  }
```

**Response `201 No Content`**

---

#### Remove song from favorites

##### DELETE http://localhost:8080/api/v1/accounts/{accountId}/favorites/{songId}


**Response `204 No Content`**

---

### Playlists

#### List playlists

##### GET http://localhost:8080/api/v1/accounts/{accountId}/playlists


---

#### Create playlist

##### POST http://localhost:8080/api/v1/accounts/{accountId}/playlists
```json 
  { 
    "name": "My Workout Playlist" 
  } 
```

**Response `201`** — Location header contains the new playlist URI.

---

#### Add song to playlist

##### POST http://localhost:8080/api/v1/accounts/{accountId}/playlists/{playlistId}/songs
```json
  {
    "songId": "{songId}",
    "position": 1
  }
```

**Response `200`**

---

#### Remove song from playlist

#####  DELETE http://localhost:8080/api/v1/accounts/{accountId}/playlists/{playlistId}/songs/{songId}


**Response `204 No Content`**

---

#### Delete playlist

#####  DELETE http://localhost:8080/api/v1/accounts/{accountId}/playlists/{playlistId}


**Response `204 No Content`**

---

## End-to-End Example

Complete walkthrough creating an account and making a transaction:

##### POST http://localhost:8080/api/v1/accounts
```json
    # 1 — Create account
  {
    "email":"demo@mockify.com","fullName":"Demo  User"
  }
```

##### POST http://localhost:8080/api/v1/accounts/$ACCOUNT_ID/credit-card
```json
  # 2 — Register credit card
  {
    "cardNumber":"4111111111111111",
    "expiryMonth":12,
    "expiryYear":2028,
    "holderName":"Demo User"
  }
```

##### POST http://localhost:8080/api/v1/accounts/$ACCOUNT_ID/subscriptions
```json
  # 3 — Subscribe to Premium
  {
    "plan": "PREMIUM"
  }
```

##### GET http://localhost:8080/api/v1/songs
```json
  # 4 — List songs and pick one
```

##### POST http://localhost:8080/api/v1/accounts/$ACCOUNT_ID/favorites
```json
  # 5 — Add to favorites
  {
    "songId": "{songId}"
  }
```

##### POST http://localhost:8080/api/v1/transactions
```json
  # 6 — Authorize a transaction
  {
    "accountId": "{accountId}",
    "amount": 29.90,
    "currency": "BRL",
    "merchantId": "SPOT-001",
    "merchantName": "Spotify"
  }
```

---

## Project Structure

```
mockify/
├── api-gateway/          # Central entry point (port 8080)
├── account-service/      # Accounts and credit cards (port 8081)
├── transaction-service/  # Transactions and fraud rules (port 8082)
├── favorite-service/     # Songs and favorites (port 8083)
├── playlist-service/     # Playlists (port 8084)
├── subscription-service/ # Subscription plans (port 8085)
├── shared/               # Domain primitives shared across modules
├── compose.yaml          # Docker Compose — starts all services
├── Dockerfile            # Multi-service image build (ARG SERVICE=...)
└── pom.xml               # Maven multi-module aggregator
```
