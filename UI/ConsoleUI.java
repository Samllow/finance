package UI;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import model.Category;
import model.Wallet;
import service.BudgetObserver;
import model.Account;


public class ConsoleUI implements BudgetObserver {
    private final Scanner scanner;
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }
    
    public void printMessage(String message){
        System.out.println(message);
    }
    
    public void printError(String errorMessage){
        String line = "#".repeat(80);
        System.out.println("\n\n" + line);
        System.out.println("\n" + errorMessage + "\n");
        System.out.println(line);
    }
    
    public String readString(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine();
            if (input != null) {
                return input;
            }else{
                continue;
            }
        }
    }
    
    public double readDouble(String prompt){
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Это не число");
            }
        }
    }

    public char readChar(String prompt){
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input.charAt(0);
            }else{
                System.out.println("Вы ничего не ввели.");
                continue;
            }
        }

    }

    public void printWalletReport(Account account) {
        Wallet wallet = account.getWallet();
        System.out.println("\n\n=== РЕЗУЛЬТАТЫ ПОДСЧЕТОВ ===");

        double totalIncome = wallet.calculateTotalIncome();
        System.out.printf("Общий доход: %.2f\n", totalIncome);
        System.out.println("Доходы по категориям:");
        Map<Category, Double> incomes = wallet.calculateIncomeByCategory(wallet.getIncomeCategories());
        
        if (incomes.isEmpty()) {
            System.out.println("(нет данных)");
        } else {
            for (Map.Entry<Category, Double> income : incomes.entrySet()) {
                System.out.printf("%s : %.2f\n", income.getKey().getName(), income.getValue());
            }
        }

        double totalExpenses = wallet.calculateTotalExpense();
        System.out.printf("\nОбщие расходы: %.2f\n", totalExpenses);
        
        System.out.println("\nБюджет по категориям:");
        Map<Category, Double> expenses = wallet.calculateExpenseByCategory(wallet.getExpenseCategories());
        for (Map.Entry<Category, Double> entry : expenses.entrySet()) {
            Category cat = entry.getKey();
            double spent = entry.getValue();
            double budget = cat.getLimit();
            double remaining = budget - spent;
            System.out.printf("%s : %.2f , Оставшийся бюджет: %.2f\n", cat.getName(), spent, remaining);

        }
    }
    
    public void printCategories(Set<Category> categories) {
        for (Category category : categories) {
            System.out.println(category.getName());
        }
    }

    @Override 
    public void onBudgetExceeded(String categoryName, double limit, double spent){
        System.out.println("\nВы превысили бюджет");
        System.out.printf("Категория: %s | Лимит: %.2f | Потрачено: %.2f%n", categoryName, limit, spent);
    }
    @Override
    public void onBudgetWarning(String categoryName, double limit, double spent) {
        System.out.println("\nВы потратили 80% бюджета!");
        System.out.printf("Категория: %s | Лимит: %.2f | Потрачено: %.2f%n", categoryName, limit, spent);
    }
    @Override
    public void onZeroBalance(double currentBalance) {
        System.out.println("\nНа вашем балансе не осталось денег!");
        System.out.printf("Текущий баланс: %.2f%n", currentBalance);
    }
}
