package UI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import model.Category;
import model.Income;
import model.Wallet;
import model.Account;


public class ConsoleUI {
    private final Scanner scanner;
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }
    
    public void printMessage(String message){
        System.out.println(message);
    }
    
    public void printError(String errorMessage){
        System.out.println(errorMessage);
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
        System.out.println("===ДОХОДЫ===");
        System.out.printf("Общий доход: %.2f\n" + wallet.calculateTotalIncome());
        System.out.printf("Общий расход: %.2f\n" + wallet.calculateTotalExpense());
        System.out.printf("Баланс: %.2f\n" + wallet.getBalance(wallet.calculateTotalIncome(), wallet.calculateTotalExpense()));
        
        System.out.println("===РАСХОДЫ===");
        double spent = wallet.calculateTotalExpense();
        System.out.printf("Общие расходы: %.2f\n", spent);
        for (Map.Entry<Category, Double> entry : wallet.calculateExpenseByCategory(wallet.getExpenseCategories()).entrySet()) {
            System.out.printf("Категория: %s | Потрачено %.2f\n", entry.getKey().getName(), entry.getValue());
        }
    }
    
    public void printCategories(Set<Category> categories) {
        for (Category category : categories) {
            System.out.println(category.getName());
        }
    }
}
