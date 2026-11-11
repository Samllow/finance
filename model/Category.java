package model;

public class Category {
    private String category;
    private double limit;

    public Category(String category, double limit) {
        this.category = category;
        this.limit = limit;
    }
    
    public String getName(){ return category; }
    public double getLimit(){ return limit; } 
}
