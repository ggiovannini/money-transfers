package functional;

import com.ggiovannini.moneytransfers.model.Account;
import com.ggiovannini.moneytransfers.utils.IdGeneratorUtil;
import functional.config.FunctionalTestConfig;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class SimpleMoneyTransferTest extends FunctionalTestConfig {

    private Account sourceAccount;
    private Account targetAccount;

    @Override
    public void beforeEach() {
        super.beforeEach();
        sourceAccount = new Account(IdGeneratorUtil.generateId(), new BigDecimal(6000));
        targetAccount = new Account(IdGeneratorUtil.generateId(), new BigDecimal(0));

        accountDAO.createAccount(sourceAccount);
        accountDAO.createAccount(targetAccount);
    }


    @Test
    public void invalidBodyParametersTest() {
        given().body("{\"transaction_amount\":\"0\",\"source_account\":\"3\",\"target_account\":\"4\"}")
                .when().post(buildPath("/transaction/transfer"))
                .then()
                .statusCode(400)
                .and().body("status", CoreMatchers.equalTo("400"))
                .and().body("message", CoreMatchers.equalTo("Invalid request body parameter"));

    }

    @Test
    public void accountNotFoundTest() {
        String inexistentAccountId = sourceAccount.getId() + targetAccount.getId();

        given().body("{\"transaction_amount\":\"2000\",\"source_account\":\"" + inexistentAccountId + "\",\"target_account\":\"" + targetAccount.getId() + "\"}")
                .when().post(buildPath("/transaction/transfer"))
                .then()
                .statusCode(404)
                .and().body("status", CoreMatchers.equalTo("404"))
                .and().body("message", CoreMatchers.equalTo("Account with id '" + inexistentAccountId + "' not found"));
    }

    @Test
    public void insufficientAccountMoneyTest() {
        given().body("{\"transaction_amount\":\"8000\",\"source_account\":\"" + sourceAccount.getId() + "\",\"target_account\":\"" + targetAccount.getId() + "\"}")
                .when().post(buildPath("/transaction/transfer"))
                .then()
                .statusCode(500)
                .and().body("status", CoreMatchers.equalTo("500"))
                .and().body("message", CoreMatchers.equalTo("Insufficient account money to make the transaction"));
    }

    @Test
    public void successfullMoneyTransferTest() {
        given().body("{\"transaction_amount\":\"2000\",\"source_account\":\"" + sourceAccount.getId() + "\",\"target_account\":\"" + targetAccount.getId() + "\"}")
                .when().post(buildPath("/transaction/transfer"))
                .then()
                .statusCode(200)
                .and().body("id", matchesPattern(REGEX_NUMBER))
                .and().body("status", equalTo("DONE"))
                .and().body("source_account", equalTo(sourceAccount.getId()))
                .and().body("target_account", equalTo(targetAccount.getId()))
                .and().body("transaction_amount", equalTo(2000));
    }
}
