package functional;

import functional.config.FunctionalTestConfig;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class SimpleCreateAccountTest extends FunctionalTestConfig {

    @Test
    public void createAccountSuccessfullyTest() {
        given().body("{\"balance\":\"70000\"}")
                .when().post(buildPath("/account"))
                .then()
                .statusCode(201)
                .body("balance", equalTo(70000))
                .body("id", matchesPattern(REGEX_NUMBER));
    }

    @Test
    public void invalidParametersTest() {
        given().body("{\"wrong_param\":\"w\"}")
                .when().post(buildPath("/account"))
                .then()
                .statusCode(400)
                .and().body("status", equalTo("400"))
                .and().body("message", equalTo("Invalid request body parameter"));
    }
}
