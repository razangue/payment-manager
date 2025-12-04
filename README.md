# Payment Manager - with(Hexagonal architecture)

Modules:
- payment-domain
- payment-application
- payment-infrastructure (Spring Boot app)

Run:
1. docker compose up -d

3 API groups: 
-Client
-Account
-Operation Account

-On Account we can make 3 operations: deposit, withdrawal and payment
-Internal operation generate 1 operation detail which allows to see balance before and after operation and the amount of operation
-Payment operation generate two operations detail (debit and credit)
-After updating account balance, create operation and operation detail, we send a notification for each operation detail

It is not finished I am fixing some bugs (I didn't push branch with unit test because it not stable)


## Run with Docker Compose

Start infrastructure services:

```bash
docker compose up -d
```
