package service;

public interface BudgetObserver {
    //доситжение 80%
    void onBudgetWarning(String categoryName, double limit, double spent);
    //превышение
    void onBudgetExceeded(String categoryName, double limit, double spent);
    //при 0 балансе
    void onZeroBalance(double currentBalance);
}
