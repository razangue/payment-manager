# Payment Manager - with(Hexagonal architecture)

I designed this application with hexagonal architecture. I have 3 modules

Modules:
- payment-domain
- payment-application
- payment-infrastructure (Spring Boot app)


3 API groups: 
- Client
- Account
- Operation Account

- On Account we can make 3 operations: deposit, withdrawal and payment
- Internal operation generate 1 operation detail which allows to see balance before and after operation and the amount of operation
- Payment operation generate two operations detail (debit and credit)
- After updating account balance, create operation and operation detail, we send a notification for each operation detail

For tests use POSTMAN
=>Start infrastructure services: (Run with Docker Compose) => for starting postgresql and kafka. Use the command below on terminal:

docker compose up -d

=>start create client : 
- url API:  http://localhost:8080/api/clients/create
- body:
            {
              "lastName":"Jean ",
              "firstName":"Pierre",
              "birthDate":"1988-11-05",
              "gender":"M",
              "nationality":"France"
            }
  
=>create 2 accounts: 
- url API:  http://localhost:8080/api/account/create
- bodies: replace "xxxxxx" by the generated previous client uuid ( "6297aaff-2456-420d-894e-13ac3e4fe27d" for example)
            {
                "accountNumber": "12342",
                "owners":[{
                "birthDate": "1988-11-05",
                "firstName": "Pierre",
                "gender": "M",
                "id": "xxxxxx",
                "lastName": "Jean ",
                "nationality": "France"
            }]
            }
            
  {
                "accountNumber": "12343",
                "owners":[{
                "birthDate": "1988-11-05",
                "firstName": "Pierre",
                "gender": "M",
                "id": "xxxxxx",
                "lastName": "Jean ",
                "nationality": "France"
            }]
            }
  
=> make a deposit operation to get fund:
- url API:  http://localhost:8080/api/operations/deposit
- body :
  
          {
           "accountNumber": "12341",
           "amount": "2500.0"
          }
  
=> make a payment:
- url API:  http://localhost:8080/api/operations/payment
- body:
  
     {
      "sourceAccountNumber": "12341",
       "amount": "2500.0",
       "receivingAccountNumber":"12342"
      }

Use a db client to visualize all data in database

