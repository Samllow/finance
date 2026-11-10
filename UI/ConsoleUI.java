package UI;
import java.util.Scanner;
import java.util.Set;
import model.Category;

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

    public void printWalletReport(Account account) {
        System.out.println();
    }
    
    public void printCategories(Set<Category> categories) {
        for (Category category : categories) {
            System.out.println("Категория: " + category.getName() + " Бюджет: " + category.getLimit());
        }
    }
}
