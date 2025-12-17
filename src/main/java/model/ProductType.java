package model;

public class ProductType {
    private int id;
    private String productTypeName;
    private int categoryId;


    public ProductType() {}

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getProductTypeName() { return productTypeName; }
    public void setProductTypeName(String name) { this.productTypeName = name; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int id) { this.categoryId = id; }

}