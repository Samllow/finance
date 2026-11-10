package UI;

import model.Account;
import service.AuthService;
import service.TransactionService;

public class AppController {
    private final ConsoleUI ui;
    private final AuthService authService;
    private final TransactionService transactionService;

    private Account currentAccount;
    public AppController(ConsoleUI ui, AuthService authService, TransactionService transactionService) {
        this.ui = ui;
        this.authService = authService;
        this.transactionService = transactionService;
    }

    public void start(){}
}
