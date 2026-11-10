package model;

public class Category {
    private String category;
    private int limit;

    public Category(String category, int limit) {
        this.category = category;
        this.limit = limit;
    }
    
    public String getName(){ return category; }
    public int getLimit(){ return limit; } 
}
