package model;
import java.util.List;
import java.util.Map;
import java.util.Set;

import service.BudgetObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Wallet {
    private final String ownerLogin;
    private List<Transaction> transactions = new ArrayList<>();
    private List<BudgetObserver> observers = new ArrayList<>();

    public Wallet(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }
    
    public void addObserver(BudgetObserver observer){
        observers.add(observer);
    }
    public String getOwner(){return ownerLogin;}
    public void addTransaction(Transaction t){
        double incomeBefore = calculateTotalIncome();
        double expenseBefore = calculateTotalExpense();
        double balanceBefore = getBalance(incomeBefore, expenseBefore);

        if(transactions instanceof Expense){
            Category category = t.getCategory();
            double limit = category.getLimit();
            
            if (limit > 0) {
                double spentBefore = calculateExpenseByCategory(Set.of(category)).getOrDefault(category, 0.0);
                double spentAfter = spentBefore + t.getAmount();
                double threshould80 = limit * 0.8;
                if (spentBefore < threshould80 && spentAfter >= threshould80 && spentAfter <= limit){
                    notifyWarning(category.getName(), limit, spentAfter);
                }

                if (spentBefore <= limit && spentAfter > limit) {
                    notifyExceeded(category.getName(), limit, spentAfter);
                }
            }
        }

        this.transactions.add(t);

        double incomeAfter = calculateTotalIncome();
        double expenseAfter = calculateTotalExpense();
        double balanceAfter = getBalance(incomeAfter, expenseAfter);

        if (balanceBefore > 0 && balanceAfter <= 0){
            notifyZeroBalance(balanceAfter);
        }
    }

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

    private void notifyWarning(String cat, double limit, double spent){
        for (BudgetObserver o : observers) {
            o.onBudgetWarning(cat, limit, spent);
        }
    }
    private void notifyExceeded(String cat, double limit, double spent) {
        for (BudgetObserver o : observers) o.onBudgetExceeded(cat, limit, spent);
    }
    private void notifyZeroBalance(double balance) {
        for (BudgetObserver o : observers) o.onZeroBalance(balance);
    }
}
