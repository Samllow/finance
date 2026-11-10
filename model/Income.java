package model;
import java.time.LocalDateTime;

public class Income extends Transaction{
    public Income(double value, LocalDateTime date, Category category) {
        super(value, date, category);
    }
    public double getEffect() {
        return 1.0;
    }
}
