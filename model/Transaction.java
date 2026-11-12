package model;
import java.time.LocalDateTime;

public abstract class Transaction {
    protected final double amount;
    protected final LocalDateTime date;
    protected final Category category;

    public Transaction(double amount, LocalDateTime date, Category category) {
        this.amount = amount;
        this.date = date;
        this.category = category;
    }
    
    abstract public double getEffect();

    public double getAmount(){ return amount;}
    public LocalDateTime getDate(){ return date; }
    public Category getCategory(){ return category; }
}
