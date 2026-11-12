package model;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


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

    public Map<Category, Double> calculateExpenseByCategory(Set<Category> categories){
        Map<Category, Double> expenses = new HashMap<>();
        for (Transaction transaction : transactions) {
            if (transaction instanceof Expense && categories.contains(transaction.getCategory())) {
                Category cat = transaction.getCategory();
                double currentSum = expenses.getOrDefault(cat, 0.0);
                expenses.put(cat, currentSum + transaction.getAmount());
            }
        }
        return expenses;
    }

    public Map<Category, Double> calculateIncomeByCategory(Set<Category> categories){
        Map<Category, Double> incomes = new HashMap<>();
        for (Transaction transaction : transactions) {
            if (transaction instanceof Income && categories.contains(transaction.getCategory())) {
                Category cat = transaction.getCategory();
                double currentSum = incomes.getOrDefault(cat, 0.0);
                incomes.put(cat, currentSum + transaction.getAmount());
            }
        }
        return incomes;
    }

    public double getBalance(double income, double expense){
        return income + expense;
    }
    
    public Set<Category> getIncomeCategories(){
        Set<Category> categories = new HashSet<>();
        for (Transaction transaction : transactions) {
            if (transaction instanceof Income) categories.add(transaction.getCategory());
        }
        return categories;
    }

    public Set<Category> getExpenseCategories(){
        Set<Category> categories = new HashSet<>();
        for (Transaction transaction : transactions) {
            if (transaction instanceof Expense) categories.add(transaction.getCategory());
        }
        return categories;
    }
    
    public Category getExpenseCategory(String categoryName){
        Set<Category> categories = getExpenseCategories();
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) return category;
        }
        return null;
    }

    public List<Transaction> getTransactions(){return transactions;}
}
