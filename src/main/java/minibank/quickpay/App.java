package minibank.quickpay;

import minibank.quickpay.service.AccountService;
import minibank.quickpay.service.AccountServiceImpl;
import minibank.quickpay.domain.service.MoneyTransactionService;
import minibank.quickpay.domain.service.MoneyTransactionServiceImpl;
import minibank.quickpay.handler.AccountHandler;
import minibank.quickpay.handler.MoneyTransactionHandler;
import minibank.quickpay.infrastructure.AccountRepository;
import minibank.quickpay.infrastructure.AccountRepositoryImpl;
import minibank.quickpay.util.DbUtil;
import minibank.quickpay.handler.QuickPayExceptionHandler;

import static spark.Spark.*;

/**
 * this is main class for quickpay app
 */
public class App {

    public static void main(String[] args) {
        init();
    }


    /**
     * This method will initialize Schema for application.
     * Then it will wire the dependency together
     *
     */
    public static void init() {

        //Initialize and populate schema
        DbUtil.initialize();

        //wire dependency for service repository and handler
        AccountRepository repository = new AccountRepositoryImpl();
        setupAccountHandler(repository);
        setupMoneyTransactionHandler(repository);

        //setup general error messages
        notFound(QuickPayExceptionHandler::notFound);
        internalServerError(QuickPayExceptionHandler::internalServerError);
        exception(IllegalArgumentException.class, QuickPayExceptionHandler::process);
    }

    private static void setupMoneyTransactionHandler(AccountRepository accountRepository) {
        MoneyTransactionService moneyTransactionService = new MoneyTransactionServiceImpl(accountRepository);
        MoneyTransactionHandler moneyTransactionHandler = new MoneyTransactionHandler(moneyTransactionService);
        moneyTransactionHandler.setupEndpoints();
    }

    private static void setupAccountHandler(AccountRepository accountRepository) {
        AccountService accountService = new AccountServiceImpl(accountRepository);
        AccountHandler accountHandler = new AccountHandler(accountService);
        accountHandler.setupEndpoints();
    }
}
