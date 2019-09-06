package functional;

import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.model.ErrorResponse;
import functional.config.FunctionalTestConfig;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class ConcurrentMoneyTransferTest extends FunctionalTestConfig {


    @Test
    public void concurrentMoneyTransfersTest() throws InterruptedException {
        Set<ErrorResponse> errorResult = ConcurrentHashMap.newKeySet();

        Account accountA = new Account("acA", new BigDecimal(1000));
        Account accountB = new Account("acB", new BigDecimal(1000));
        Account accountC = new Account("acC", new BigDecimal(1000));
        accountDAO.createAccount(accountA);
        accountDAO.createAccount(accountB);
        accountDAO.createAccount(accountC);

        Account account1 = new Account("ac1", new BigDecimal(1000));
        Account account2 = new Account("ac2", new BigDecimal(1000));
        Account account3 = new Account("ac3", new BigDecimal(1000));
        accountDAO.createAccount(account1);
        accountDAO.createAccount(account2);
        accountDAO.createAccount(account3);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        /*
         *  acA gives $200 to ac1
         *  acA gives $200 to ac2
         *  acA gives $200 to ac3
         */
        IntStream.range(0, 10).forEach(c -> {
            executorService.submit(() -> {
                given().body("{\"transaction_amount\":20,\"source_account\":\"" + accountA.getId() + "\",\"target_account\":\"" + account1.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":20,\"source_account\":\"" + accountA.getId() + "\",\"target_account\":\"" + account2.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":20,\"source_account\":\"" + accountA.getId() + "\",\"target_account\":\"" + account3.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

            });
        });

        /*
         *  ac1 gives $100 to acA
         *  ac1 gives $100 to acB
         *  ac1 gives $100 to acC
         */
        IntStream.range(0, 10).forEach(c -> {
            executorService.submit(() -> {
                given().body("{\"transaction_amount\":10,\"source_account\":\"" + account1.getId() + "\",\"target_account\":\"" + accountA.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":10,\"source_account\":\"" + account1.getId() + "\",\"target_account\":\"" + accountB.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":10,\"source_account\":\"" + account1.getId() + "\",\"target_account\":\"" + accountC.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

            });
        });

        /*
         *  acB gives $200 to ac1
         *  acB gives $200 to ac2
         *  acB gives $200 to ac3
         */
        IntStream.range(0, 10).forEach(c -> {
            executorService.submit(() -> {
                given().body("{\"transaction_amount\":20,\"source_account\":\"" + accountB.getId() + "\",\"target_account\":\"" + account1.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":20,\"source_account\":\"" + accountB.getId() + "\",\"target_account\":\"" + account2.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":20,\"source_account\":\"" + accountB.getId() + "\",\"target_account\":\"" + account3.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);
            });
        });

        /*
         *  ac2 gives $100 to acA
         *  ac2 gives $100 to acB
         *  ac2 gives $100 to acC
         */        IntStream.range(0, 10).forEach(c -> {
            executorService.submit(() -> {
                given().body("{\"transaction_amount\":10,\"source_account\":\"" + account2.getId() + "\",\"target_account\":\"" + accountA.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":10,\"source_account\":\"" + account2.getId() + "\",\"target_account\":\"" + accountB.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":10,\"source_account\":\"" + account2.getId() + "\",\"target_account\":\"" + accountC.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

            });
        });

        /*
         *  acC gives $200 to ac1
         *  acC gives $200 to ac2
         *  acC gives $200 to ac3
         */
        IntStream.range(0, 10).forEach(c -> {
            executorService.submit(() -> {
                given().body("{\"transaction_amount\":20,\"source_account\":\"" + accountC.getId() + "\",\"target_account\":\"" + account1.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":20,\"source_account\":\"" + accountC.getId() + "\",\"target_account\":\"" + account2.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":20,\"source_account\":\"" + accountC.getId() + "\",\"target_account\":\"" + account3.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);
            });
        });

        /*
         *  ac3 gives $100 to acA
         *  ac3 gives $100 to acB
         *  ac3 gives $100 to acC
         */
        IntStream.range(0, 10).forEach(c -> {
            executorService.submit(() -> {
                given().body("{\"transaction_amount\":10,\"source_account\":\"" + account3.getId() + "\",\"target_account\":\"" + accountA.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":10,\"source_account\":\"" + account3.getId() + "\",\"target_account\":\"" + accountB.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);

                given().body("{\"transaction_amount\":10,\"source_account\":\"" + account3.getId() + "\",\"target_account\":\"" + accountC.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(200);
            });
        });

        /*
         *  Request with exceptions
         *
         *  1) 10 request with insufficient Account Money
         *  2) 10 request with account not found
         */
        IntStream.range(0, 10).forEach(c -> {
            executorService.submit(() -> {
                ErrorResponse insAccMoney = given().body("{\"transaction_amount\":\"10000\",\"source_account\":\"" + account1.getId() + "\",\"target_account\":\"" + accountA.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(500)
                        .extract().as(ErrorResponse.class);
                errorResult.add(insAccMoney);

                ErrorResponse accNotFound = given().body("{\"transaction_amount\":\"10\",\"source_account\":\"invalidAccountId\",\"target_account\":\"" + accountB.getId() + "\"}")
                        .post(buildPath("/transaction/transfer"))
                        .then().assertThat().statusCode(404)
                        .extract().as(ErrorResponse.class);
                errorResult.add(accNotFound);
            });
        });

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(new BigDecimal(700), accountA.getBalance().getAmount());
        assertEquals(new BigDecimal(700), accountB.getBalance().getAmount());
        assertEquals(new BigDecimal(700), accountC.getBalance().getAmount());

        assertEquals(new BigDecimal(1300), account1.getBalance().getAmount());
        assertEquals(new BigDecimal(1300), account2.getBalance().getAmount());
        assertEquals(new BigDecimal(1300), account3.getBalance().getAmount());

        assertEquals(20, errorResult.size());
    }
}
