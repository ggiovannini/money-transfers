package functional;

import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.model.ErrorResponse;
import com.ggiovannini.moneytransfers.utils.IdGeneratorUtil;
import functional.config.FunctionalTestConfig;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.get;
import static org.junit.Assert.assertEquals;

public class ConcurrentGetAccountTest extends FunctionalTestConfig {

    @Test
    public void shouldGet500AccountsSuccessfullyAnd500FailedTest() throws InterruptedException {
        Set<Account> successfulResult = ConcurrentHashMap.newKeySet();
        Set<ErrorResponse> errorResult = ConcurrentHashMap.newKeySet();

        IntStream.range(0, 500).forEach(i -> {
            accountDAO.createAccount(new Account(IdGeneratorUtil.generateId(), BigDecimal.ZERO));
        });

        assertEquals(500, accountDAO.readAllAccounts().size());

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 500).forEach(i -> {
            executorService.submit(() -> {
                String accountJson = get(buildPath("/account/" + (i + 1))).then().assertThat().statusCode(200).extract().asString();
                successfulResult.add(gson.fromJson(accountJson, Account.class));
            });
        });
        IntStream.range(0, 500).forEach(i -> {
            executorService.submit(() -> {
                ErrorResponse errorResponse = get(buildPath("/account/" + (i + 501))).then().assertThat().statusCode(404).extract().as(ErrorResponse.class);
                errorResult.add(errorResponse);
            });
        });
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(500, successfulResult.size());
        assertEquals(500, errorResult.size());

    }
}

