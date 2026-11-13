package UI;

import model.Account;
import model.Category;
import model.Wallet;
import service.AuthService;
import service.StorageService;
import service.TransactionService;
import service.TransactionService.OperationResult;
import service.AppData;
import java.time.LocalDateTime;
import java.util.Set;

public class AppController {
    private final ConsoleUI ui;
    private final AuthService authService;
    private final TransactionService transactionService;
    private final AppData appData;
    private Account currentAccount;
    private StorageService storageService;

    public AppController(ConsoleUI ui, AuthService authService, 
        TransactionService transactionService, AppData appData, 
        StorageService storageService) {
        this.ui = ui;
        this.authService = authService;
        this.transactionService = transactionService;
        this.appData = appData;
        this.storageService = storageService;
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
                currentAccount.getWallet().addObserver(ui);
                break;
            default:
                break;
        }
    }
    private void handleRegistration() {
        String userName = ui.readString("Введите имя: ");
        String password = ui.readString("Введите пароль: ");
        boolean creationAnswer = authService.createAccount(userName, password);
        String attention = creationAnswer ? "Успешно зарегистрировано" : "Ошибка регистрации. Аккаунт с таким именем уже существует";
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
        OperationResult result = transactionService.addExpense(
            currentAccount.getLoggin(),
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
            case 'f': 
                storageService.load(appData);
                ui.printMessage("Сохранение очёта в файл");
                break;
            default:
                ui.printError("Неверный набор");
        }

    }
    private void handleLogout(){
        if (currentAccount != null){
            ui.printMessage("Пользователь " + currentAccount.getLoggin() + " вышел из системы.");
            storageService.save(appData);
            this.currentAccount = null;

        } else{ ui.printError("Вы и так не авторизованы."); }
    }
    //TODO: private void handleTransfer() {}
    private void handleUpdateBudget() {
        Wallet wallet = currentAccount.getWallet();

        Set<Category> categories = wallet.getExpenseCategories();
        if (categories.isEmpty()){
            ui.printMessage("У вас пока нет категорий расходов. Добавьте хотя бы один расход.");
            return;
        }

        ui.printMessage("\n--- ТЕКУЩИЕ БЮДЖЕТЫ ---");
        for (Category cat : categories) {
            ui.printMessage("  " + cat.getName() + " | Лимит: " + cat.getLimit());            
        }
        String categoryName = ui.readString("\nВведите название категории для изменения (или Enter для отмены): ");
        if (categoryName.isBlank()) {return;}
        double newLimit = ui.readDouble("Введите новый лимит: ");
        OperationResult result = transactionService.updateCategoryLimit(currentAccount.getLoggin(), categoryName, newLimit);
        
        if (result.getResult()) {
            ui.printMessage(result.getErrorMessage());
        } else {
            ui.printError(result.getErrorMessage());
        }
    }
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
        ui.printMessage("<v>: Показать финансовый отчёт");
        ui.printMessage("<b>: Изменить бюджет");
        // ui.printMessage("<t>: Перевести деньги другому пользователю");
        ui.printMessage("<l>: Выйти из аккаунта (Logout)");
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
                        storageService.save(appData); 
                        ui.printMessage("До свидания!");
                        return;
                    default:
                        break;
                }
            } else {
                switch (choise) {
                    case 'i': handleAddIncome(); break;
                    case 'e': handleAddExpense(); break;
                    case 'v': handleShowReports(); break;
                    case 'b': handleUpdateBudget(); break;
                    //case 't': TODO: handleTransfer(); break;
                    case 'l': handleLogout(); break;
                    case 'q':
                        ui.printMessage("Сохранение данных и выход");
                        storageService.save(appData);
                        return;
                    default:
                        ui.printError("Неверный пункт меню");
                }
            }
        }
    }
}
