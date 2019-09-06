package functional;

import com.ggiovannini.moneytransfers.model.Account;

import com.ggiovannini.moneytransfers.utils.IdGeneratorUtil;
import functional.config.FunctionalTestConfig;

import org.junit.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;

public class SimpleGetAccountTest extends FunctionalTestConfig {

    private Account account;



    @Override
    public void beforeEach() {
        super.beforeEach();
        account = new Account(IdGeneratorUtil.generateId(), new BigDecimal(6000));
        accountDAO.createAccount(account);
    }

    @Test
    public void testAccountNotFound() {
        String inexistentAccountId = account.getId() + "12312";
        when().get(buildPath("/account/" + inexistentAccountId))
                .then()
                .statusCode(404)
                .and().body("status", equalTo("404"))
                .and().body("message", equalTo("Account with id '" + inexistentAccountId + "' not found"));

    }

    @Test
    public void testGetAnAccountSuccessfully() {
        when().get(buildPath("/account/" + account.getId()))
                .then()
                .statusCode(200)
                .and().body("id", equalTo(account.getId()))
                .and().body("balance", equalTo(6000));
    }
}
