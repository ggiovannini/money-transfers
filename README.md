# Money Transfers


## How to run


#### Build
To build the project execute from the root folder:

gradle build

#### Run
To run the app execute from the root folder:

gradle run

or from the jar:

java -jar money-transfers-1.0.0.jar


The application will start on the `localhost` and will be listening to the port `8080`


#### Tests
To run the tests execute from the root folder:

gradle test

The functional tests will run the app on the `localhost` and the port `8888`

To see the test report open:
../build/reports/tests/test/index.html



## API Definition

### Account

#### Account Structure
{
"id": <string>,
"balance": <MoneyBalance>
}

#### MoneyBalance Structure
{
"id": <string>,
"amount": <number>
}

#### Create Account

The following creates an account and returns the created entity with its `ID` 

Request:

POST http://localhost:8080/api/1.0/account
{
"balance": 7000.60
}

Response:

HTTP 201 OK
{
"id": "1",
"balance": {
"amount": 7000.60
}
}


#### Get Account

The following gets an account

Request:

GET http://localhost:8080/api/1.0/account/<id>

Response:

HTTP 200 OK
{
"id": "1",
"balance": {
"amount": 7000.60
}
}


### Transaction

#### Structure
{
"id": <string>,
"sourceAccount": <string>,
"targetAccount": <string>,
"transactionStatus": <TransactionStatus>,
"transactionAmount": <number>
}


#### Create a transference

Request:

POST http://localhost:8080/api/1.0/transaction/transfer
{
"transaction_amount": 6000.50,
"source_account": "413472",
"target_account": "347347",
}

Response:

HTTP 200 OK
{
"id": "1",
"source_account": "413472",
"target_account": "347347",
"status": "DONE",
"transaction_amount": 6000.50
}

HTTP 500 Internal Error
{
"status": "500",
"message": "Insufficient account money to make the transaction"
}  


### Exception Handing

Example response:

{
"status": "500",
"message": "Insufficient account money to make the transaction"
}    
