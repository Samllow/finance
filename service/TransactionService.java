package service;
import java.time.LocalDateTime;

import model.Income;
import model.Wallet;
import model.Account;
import model.Category;
import model.Expense;

public class TransactionService {

    private AppData appData;

    public TransactionService(AppData appData){ this.appData = appData; }
    
    public OperationResult addIncome(String login, double value, LocalDateTime date, Category category) {
        Account account = appData.findAccount(login);
        if (account == null) return new OperationResult(false, "Пользователь не существует");
        if (value <= 0) {
            return new OperationResult(false, "Введите число большее чем 0");
        }
        if (category == null) {
            return new OperationResult(false, "Эта категория не существует");
        }

        account.getWallet()
            .addTransaction(new Income(value, date, category));
        return new OperationResult(true, null);
    }

    public OperationResult addExpense(String login, double value, LocalDateTime date, String categoryName) {
        Account account = appData.findAccount(login);

        if (account == null) return new OperationResult(false, "Пользователь не существует");
        if (value <= 0) {
            return new OperationResult(false, "Введите число большее чем 0");
        }
        Category category = account.getWallet().getExpenseCategory(categoryName);
        if (category == null) {
            category = new Category(categoryName, 0.0);
            // return new OperationResult(false, "Эта категория не существует");
        }

        account.getWallet()
            .addTransaction(new Expense(value, date, category));
        return new OperationResult(true, null);
    }

    public OperationResult updateCategoryLimit(String login, String categoryName, double newLimit) {
        Account account = appData.findAccount(login);
        if (account == null) { return new OperationResult(false, "Пользователь не найден");}
        if (newLimit < 0) {return new OperationResult(false, "\nЛимит не может быть отрицательным\n");}
        Wallet wallet = account.getWallet();
        Category category = wallet.getExpenseCategory(categoryName);
        if (category == null){
            return new OperationResult(false, "\nКатегория не найдена. Сначала добавьте расход по этой категории.\n");
        }
        category.setLimit(newLimit);

        //double currentSpent = TODO: 


        return new OperationResult(true, "Лимит для категории '" + categoryName + "' успешно обновлён!");

    }

    public class OperationResult {
        private final boolean success;
        private final String errorMessage;

        public OperationResult(boolean result, String message) {
            this.success = result;
            this.errorMessage = message;
        }

        public boolean getResult() { return success;}
        public String getErrorMessage() {return errorMessage;}
    }

}
