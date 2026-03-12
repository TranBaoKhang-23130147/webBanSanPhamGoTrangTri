package model;

public class ProductType {
    private int id;
    private String productTypeName;
    private long totalInventory;

    public ProductType() {}

//    public ProductType(int id, String productTypeName) {
//    }

    public long getTotalInventory() {
        return totalInventory;
    }
    public void setTotalInventory(long totalInventory) {
        this.totalInventory = totalInventory;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getProductTypeName() {
        return productTypeName;
    }
    public void setProductTypeName(String name) {
        this.productTypeName = name;
    }


}