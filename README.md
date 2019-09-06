# Money Transfers

## Used technology

#### General:

- Java 11.0.3
- Spark (http://sparkjava.com/)
- Gson (https://github.com/google/gson)
- Guice Injector (https://github.com/google/guice)
- SLF4J (https://www.slf4j.org/)
  
  #### For test:  
- Junit
- Rest assured (http://rest-assured.io/)
- Mockito (https://site.mockito.org/)
    


## How to run

#### Run from the jar

Execute from the root folder:

    java -jar money-transfers-1.0.0.jar

The application will start on the `localhost` and will be listening to the port `8080`


#### Build
To build the project execute from the root folder:

    ./gradlew build

#### Run
To run the app execute from the root folder:

    ./gradlew run


The application will start on the `localhost` and will be listening to the port `8080`


#### Tests
To run the tests execute from the root folder:

    ./gradlew test

The functional tests will run the app on the `localhost` and the port `8888`

To see the test report open:
    `../build/reports/tests/test/index.html`



## API Definition

### Account

#### Account Structure
    {
        "id": <string>,
        "balance": <number>
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
        "balance": 7000.60
    }


#### Get Account

The following gets an account

Request:

    GET http://localhost:8080/api/1.0/account/<id>

Response:

    HTTP 200 OK
    {
        "id": "1",
        "balance": 7000.60

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

The following makes a transference between two accounts

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


### Exception Handing

Example response:

    HTTP 404 NOT FOUND 
    {
        "status": "404",
        "message": "Account with id '1234' not found"
    }    
