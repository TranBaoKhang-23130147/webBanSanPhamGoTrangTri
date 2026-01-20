package model;

public class Color {
    private int id;
    private String colorName;
    private String colorCode;

    // Constructor không tham số
    public Color() {}

    // Constructor khớp với ColorDao của bạn
    public Color(int id, String colorName, String colorCode) {
        this.id = id;
        this.colorName = colorName;
        this.colorCode = colorCode;
    }


    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getColorName() { return colorName; }
    public void setColorName(String colorName) { this.colorName = colorName; }

    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }
}