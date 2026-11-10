package service;
import java.time.LocalDateTime;
import model.Income;
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

    public OperationResult addExpense(String login, double value, LocalDateTime date, Category category) {
        Account account = appData.findAccount(login);

        if (account == null) return new OperationResult(false, "Пользователь не существует");
        if (value <= 0) {
            return new OperationResult(false, "Введите число большее чем 0");
        }
        if (category == null) {
            return new OperationResult(false, "Эта категория не существует");
        }

        account.getWallet()
            .addTransaction(new Expense(value, date, category));
        return new OperationResult(true, null);
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
