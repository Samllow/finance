package service;
import model.Account;
import model.Wallet;
import model.Category;
import model.Income;
import model.Expense;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;


public class StorageService  {
    private static final String FILE_PATH = "src/storages/finance_data.txt";

    public void save(AppData appData){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))){
            for (Account account : appData.getAllAccounts()) {
                writer.write("USER:" + account.getLoggin() + ";" + account.getPassword());
                writer.newLine();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void load(AppData appData){

    }
}
