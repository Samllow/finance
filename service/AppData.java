package service;
import model.Account;

import java.util.Map;
import java.util.HashMap;

public class AppData {
    
    private Map<String, Account> accounts = new HashMap<>();

    public void addAccount(String login, Account account) {
        accounts.put(login, account);
    }
    
    public Account findAccount(String login) {
        return accounts.get(login);        
    }

}
