package model;

public class Category {
    private int id;
    private String categoryName;
    private long totalInventory;
    public Category() {}
    public Category(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    // Getter và Setter
    public long getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(long totalInventory) {
        this.totalInventory = totalInventory;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
