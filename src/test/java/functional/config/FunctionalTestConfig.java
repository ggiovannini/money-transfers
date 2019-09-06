package functional.config;

import com.ggiovannini.moneytransfers.config.AppInjectorModule;
import com.ggiovannini.moneytransfers.dao.AccountDAO;
import com.ggiovannini.moneytransfers.dao.TransactionDAO;
import com.ggiovannini.moneytransfers.router.Router;
import com.ggiovannini.moneytransfers.router.impl.TestRouter;
import com.ggiovannini.moneytransfers.utils.IdGeneratorUtil;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import spark.Spark;

import static com.ggiovannini.moneytransfers.router.impl.TestRouter.TEST_APPLICATION_PORT;

public abstract class FunctionalTestConfig {

    protected static AccountDAO accountDAO;
    protected static TransactionDAO transactionDAO;
    protected static Gson gson;

    protected static Router router;

    public static final String BASE_PATH = "http://localhost:" + TEST_APPLICATION_PORT + "/api/1.0";
    public static final String REGEX_NUMBER = "^[0-9]*$";

    @BeforeClass
    public static void beforeAll() {
        Injector injector = Guice.createInjector(new AppInjectorModule());
        transactionDAO = injector.getInstance(TransactionDAO.class);
        accountDAO = injector.getInstance(AccountDAO.class);
        gson = injector.getInstance(Gson.class);
        router = injector.getInstance(TestRouter.class);
        router.init();
        Spark.awaitInitialization();
    }

    @AfterClass
    public static void afterAll() {
        accountDAO.deleteAllAccounts();
        transactionDAO.deleteAllTransactions();
        router.destroy();
        Spark.awaitStop();
    }

    @Before
    public void beforeEach() {
        IdGeneratorUtil.resetIdGenerator();
    }

    @After
    public void afterEach() {
        accountDAO.deleteAllAccounts();
        IdGeneratorUtil.resetIdGenerator();
    }

    public String buildPath(String path) {
        return BASE_PATH + path;
    }
}
