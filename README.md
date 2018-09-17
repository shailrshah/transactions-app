# Transactions-App

This repository contains a Spring Boot application that can be used to determine the transactions to perform in order to get a model Portfolio, from existing holdings.


## Getting Started
- Clone the repository: `git clone https://github.com/shailrshah/transactions-app.git`
- CD into the app: `cd transactions-app`
- Run the app: `gradlew.bat clean bootrun` (Windows) or `./gradlew clean bootrun` (Mac/Linux)
- Send a `POST` request to `localhost:8080/api/transactions` with the following JSON body:
```
{
  "holdingStock" : [ {
    "name" : "GOOGL",
    "quantity" : 100
  }, {
    "name" : "AAPL",
    "quantity" : 50.0
  }, {
    "name" : "MSFT",
    "quantity" : 50.0
  }],
  "modelStock" : [ {
    "name" : "AAPL",
    "percentage" : 40.0
  }, {
    "name" : "GOOGL",
    "percentage" : 60.0
  } ]
}
```
- Expect the following response: 
```
{
    "transactions": [
        {
            "name": "GOOGL",
            "quantity": 20,
            "type": "BUY"
        },
        {
            "name": "MSFT",
            "quantity": 50,
            "type": "SELL"
        },
        {
            "name": "AAPL",
            "quantity": 30,
            "type": "BUY"
        }
    ]
}
```
