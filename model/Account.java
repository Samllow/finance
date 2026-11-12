package model;
public class Account {
    private final String login;
    private final String password;
    private final Wallet wallet;

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
        this.wallet = new Wallet(login);
    }

    public String getLoggin() {
        return this.login;
    }

    public Wallet getWallet(){return wallet;}
    
    public boolean isPasswordCorrect(String password) {
        if (this.password.equals(password)){ return true;}
        return false;
    }

    public String getPassword(){ return password;}
    
}
