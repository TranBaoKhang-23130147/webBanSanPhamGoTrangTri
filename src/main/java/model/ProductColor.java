package model;

public class ProductColor {
    private int id;
    private String colorName;

    public ProductColor(int id, String colorName) {
        this.id = id;
        this.colorName = colorName;
    }
    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
