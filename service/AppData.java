package service;
import model.Account;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppData {
    
    private Map<String, Account> accounts = new HashMap<>();

    public void addAccount(String login, Account account) {
        accounts.put(login, account);
    }
    
    public Account findAccount(String login) {
        return accounts.get(login);        
    }

    public List<Account> getAllAccounts(){
        List<Account> accountList = new ArrayList<>();
        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            accountList.add(entry.getValue());
        }
        return accountList;
    }
}
