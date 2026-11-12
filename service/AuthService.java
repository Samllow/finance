package service;
import model.Account;

public class AuthService {
    private AppData appData;

    public AuthService(AppData appData){
        this.appData = appData;
    }

    public boolean createAccount(String login, String password) {
        
        if (appData.findAccount(login) != null) {
            return false;            
        }
        

        Account newAccount = new Account(login, password);
        appData.addAccount(login, newAccount);
        
        return true;
    }

    public short authorisation(String login, String password) {
        // 1 - данные не совпадают
        // -1 - аккаунт с таким логином не найден
        // 0 - успешная авторизация
        if (appData.findAccount(login) == null) {
            return -1;
        }

        if(appData.findAccount(login).isPasswordCorrect(password) == false){
            return 1;
        }
        
        return 0;
    }
}
