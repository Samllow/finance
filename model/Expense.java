package model;
import java.time.LocalDateTime;

public class Expense extends Transaction {
    public Expense(double value, LocalDateTime date, Category category) {
        super(value, date, category);
    }

    public double getEffect(){ return -1.0;}
}
