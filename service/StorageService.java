package service;
import model.Account;
import model.Category;
import model.Income;
import model.Transaction;
import model.Expense;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.io.File;


public class StorageService  {
    private static final String FILE_PATH = "src/finance_data.txt";

    public void save(AppData appData){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))){
            for (Account account : appData.getAllAccounts()) {
                writer.write("USER;" + account.getLoggin() + ";" + account.getPassword());
                writer.newLine();

                for (Transaction transaction : account.getWallet().getTransactions()) {
                    String type = (transaction instanceof Income) ? "INCOME" : "EXPENSE";
                    String date = transaction.getDate().toString();
                    String categoryName = transaction.getCategory().getName();
                    double amount = transaction.getAmount();
                    double limit = transaction.getCategory().getLimit();

                    String line = type + ";" + account.getLoggin() + ";" + 
                        date + ";" + categoryName + ";" + amount + ";" + limit;
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (Exception e) {
            System.err.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    public void load(AppData appData){
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = reader.readLine()) != null){
                if (line.isBlank()) continue;
                String[] parts = line.split(";");
                if (parts[0].equals("USER")) {
                    Account account = new Account(parts[1], parts[2]);
                    appData.addAccount(parts[1], account);
                } else if (parts[0].equals("INCOME") || parts[0].equals("EXPENSE")){
                    String type = parts[0];
                    String login = parts[1];
                    LocalDateTime date = LocalDateTime.parse(parts[2]);
                    String cateagoryName = parts[3];
                    double amount = Double.parseDouble(parts[4]);
                    double limit = Double.parseDouble(parts[5]);

                    Account account = appData.findAccount(login);
                    if (account != null) {
                        Category category = account.getWallet().getExpenseCategory(cateagoryName);
                        
                        if (category == null) {
                            category = new Category(cateagoryName, limit);
                        }

                        Transaction t = type.equals("INCOME") 
                        ? new Income(amount, date, category)
                        : new Expense(amount, date, category);
                        
                        account.getWallet().addTransaction(t);
                    }
                }
            }
        } catch (IOException | DateTimeParseException e) {
            System.err.println("Ошибка при загрузке данных: " + e.getMessage());
        }

    }
}
