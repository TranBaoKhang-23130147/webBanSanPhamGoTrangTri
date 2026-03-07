package model;
public class ProductColor {
    private int id;
    private String colorName;
    private String colorCode;
    public ProductColor() {}

    public ProductColor(int id, String colorName) {
        this.id = id;
        this.colorName = colorName;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getColorName() {
        return colorName;
    }
    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
    public String getColorCode() {
        return colorCode;
    }
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}