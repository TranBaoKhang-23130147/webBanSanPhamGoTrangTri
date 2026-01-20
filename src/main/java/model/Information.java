package model;

public class Information {
    private int id;
    private String material;  // Chất liệu
    private String color;     // Màu sắc
    private String size;      // Kích thước
    private String guarantee; // Bảo hành


    public Information() {}

    public Information(int id, String material, String color, String size, String guarantee) {
        this.id = id;
        this.material = material;
        this.color = color;
        this.size = size;
        this.guarantee = guarantee;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getGuarantee() { return guarantee; }
    public void setGuarantee(String guarantee) { this.guarantee = guarantee; }
}