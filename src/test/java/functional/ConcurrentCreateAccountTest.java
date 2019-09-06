package functional;

import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.model.ErrorResponse;

import functional.config.FunctionalTestConfig;

import org.junit.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class ConcurrentCreateAccountTest extends FunctionalTestConfig {

    @Test
    public void shouldCreate500AccountsSuccessfullyAnd500AccountsFailedTest() throws InterruptedException {
        Set<Account> successfulResponses = ConcurrentHashMap.newKeySet();
        Set<ErrorResponse> errorResult = ConcurrentHashMap.newKeySet();


        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 500).forEach(c -> {
            executorService.submit(() -> {
                String accountJson = given().body("{\"balance\":\"1\"}").post(buildPath("/account")).then().assertThat().statusCode(201).extract().asString();
                successfulResponses.add(gson.fromJson(accountJson, Account.class));
            });
        });
        IntStream.range(0, 500).forEach(c -> {
            executorService.submit(() -> {
                ErrorResponse errorResponse = given().body("{\"balance\":\"-1\"}").post(buildPath("/account")).then().assertThat().statusCode(400).extract().as(ErrorResponse.class);
                errorResult.add(errorResponse);
            });
        });
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(500, successfulResponses.size());
        assertEquals(500, errorResult.size());
    }
}
