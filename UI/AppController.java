package UI;

import model.Account;
import model.Category;
import service.AuthService;
import service.TransactionService;
import service.TransactionService.OperationResult;
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
        Category category;
        OperationResult result = transactionService.addExpense(
            categoryName,
            value,
            LocalDateTime.now(), 
            categoryName
        );

        if (result.getResult()) {
            ui.printMessage("Расход Успешно добавлен");
        } else {
            ui.printError(result.getErrorMessage());
        }
        
    }
    private void handleShowReports() {
        ui.printMessage("Куда вывести отчёт?");
        ui.printMessage("<c>: в консоль");
        ui.printMessage("<f>: в файл");

        char choise = ui.readChar("Выберите оберацию: ");

        switch (choise) {
            case 'c': ui.printWalletReport(currentAccount); break;
            case 'f': //TODO Реализовать StorageService
                ui.printMessage("Сохранение очёта в файл");
                break;
            default:
                ui.printError("Неверный набор");
        }

    }
    //TODO: private void handleLogout() {}
    //TODO: private void handleTransfer() {}
    private void printGuestMenu() {
        ui.printMessage("\n=== ГЛАВНОЕ МЕНЮ ===");
        ui.printMessage("<l>: Войти в систему");
        ui.printMessage("<r>: Зарегистрироваться");
        ui.printMessage("<q>: Выйти из приложения");
    }
    private void printUserMenu() {
        ui.printMessage("\n=== ЛИЧНЫЙ КАБИНЕТ (" + currentAccount.getLoggin() + ") ===");
        ui.printMessage("<i>: Добавить доход");
        ui.printMessage("<e>: Добавить расход");
        ui.printMessage("<s>: Показать финансовый отчёт");
        // ui.printMessage("<t>: Перевести деньги другому пользователю");
        //ui.printMessage("<l>: Выйти из аккаунта (Logout)");
        ui.printMessage("<q>: Выйти из приложения");
    }
    public void start(){
        ui.printMessage("=== HELLO WORLD ===");

        while (true) {
            if (currentAccount == null) {
                printGuestMenu();
            }else{
                printUserMenu();
            }

            char choise = 0;
            try {
                choise = ui.readChar("Выберите дейтсвие: ");
            } catch (Exception e) {
                ui.printError("Пожалуйста введите другой символ");
            }

            if (currentAccount == null) {
                switch (choise) {
                    case 'l': handleLogin(); break;
                    case 'r': handleRegistration(); break;
                    case 'q': 
                        ui.printMessage("До свидания!");
                        return;
                    default:
                        break;
                }
            } else {
                switch (choise) {
                    case 'i': handleAddIncome(); break;
                    case 'e': handleAddExpense(); break;
                    case 's': handleShowReports(); break;
                    //case 't': TODO: handleTransfer(); break;
                    //case 'l': TODO: handleLogout(); break;
                    case 'q':
                        ui.printMessage("Сохранение данных и выход");
                        //TODO: Вызвать FileStorageService.save() перед выходом
                        return;
                    default:
                        ui.printError("Неверный пункт меню");
                }
            }
        }
    }
}
