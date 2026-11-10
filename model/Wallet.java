package model;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class Wallet {
    private final String ownerLogin;
    private List<Transaction> transactions = new ArrayList<>();

    public Wallet(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }
    public String getOwner(){return ownerLogin;}
    public void addTransaction(Transaction t){ this.transactions.add(t); }

    public double calculateTotalIncome() {
        double total = 0;
        for (Transaction transaction : transactions) {
            if (transaction instanceof Income){ total += transaction.getAmount() * transaction.getEffect(); }
        }
        return total;
    }

    public double calculateTotalExpense() {
        double total = 0;
        for (Transaction transaction : transactions) {
            if (transaction instanceof Expense){ total += transaction.getAmount() * transaction.getEffect(); }
        }
        return total;
    }

    public double calculateExpenseByCategory(Set<Category> categories){
        double result = 0;
        for (Transaction transaction : transactions) {
            if (categories.contains(transaction.getCategory()) && transaction instanceof Expense)
            { result += transaction.getAmount(); }
            
        }
        return result;
    }

    public double calculateIncomeByCategory(Set<Category> categories){
        double result = 0;
        for (Transaction transaction : transactions) {
            if (categories.contains(transaction.getCategory()) && transaction instanceof Income)
            { result += transaction.getAmount(); }
        }
            
        return result;
    }
}
