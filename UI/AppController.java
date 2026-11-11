package UI;

import model.Account;
import model.Category;
import service.AuthService;
import service.TransactionService;
import service.AppData;
import java.time.LocalDateTime;

public class AppController {
    private final ConsoleUI ui;
    private final AuthService authService;
    private final TransactionService transactionService;
    private final AppData appData;
    private Account currentAccount;

    public AppController(ConsoleUI ui, AuthService authService, TransactionService transactionService, AppData appData) {
        this.ui = ui;
        this.authService = authService;
        this.transactionService = transactionService;
        this.appData = appData;
    }

    public void start(){
        
    }
    private void handleLogin() {
        String userName = ui.readString("Введите имя: ");
        String password = ui.readString("Введите пароль:");
        short authAnswer = authService.authorisation(userName, password);
        switch (authAnswer) {
            case 1:
                ui.printError("Неправильный пароль, попробуйте еще раз");
                break;
            case -1:
                ui.printError("Пользователь с таким именем не найден");
                break;
            case 0:
                ui.printMessage("Добро пожаловать!");
                currentAccount = appData.findAccount(userName);
                break;
            default:
                break;
        }
    }
    private void handleRegistration() {
        String userName = ui.readString("Введите имя: ");
        String password = ui.readString("Введите пароль: ");
        boolean creationAnswer = authService.createAccount(userName, password);
        String attention = creationAnswer ? "Успешно зарегистрировано" : "Ошибка регистрации";
        if (creationAnswer) {
            ui.printMessage(attention);
            currentAccount = appData.findAccount(attention);
        } else { ui.printError(attention); }
        

    }
    private void handleAddIncome() {
        double value = ui.readDouble("Введите сумму дохода: ");
        String categoryName = ui.readString("Введите категорию дохода: ");
        Category category = new Category(categoryName, 0);
        transactionService.addIncome(currentAccount.getLoggin(), value, LocalDateTime.now(), category);
        ui.printMessage("Доход успешно добавлен!");
    }
    private void handleAddExpense() {
        String categoryName = ui.readString("Введите категорию затраты: ");
        double value = ui.readDouble("Введите сумму затраты: ");
        if  (!currentAccount.getWallet().getExpenseCategories().contains(categoryName)){
            double limit = ui.readDouble("Введите бюджет для новой категории: ");
            Category category = new Category(categoryName, limit);
        }else{Category category = }
        transactionService.addIncome(currentAccount.getLoggin(), value, LocalDateTime.now(), );
        ui.printMessage("Доход успешно добавлен!");
    }
    // private void handleShowReports() {}
    private void handleLogout() {}
}
