package model;

public class Information {
    private int id;
    private String material;
    private String color;
    private String size;
    private String guarantee;


    public Information() {}

    public Information(int id, String material, String color, String size, String guarantee) {
        this.id = id;
        this.material = material;
        this.color = color;
        this.size = size;
        this.guarantee = guarantee;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }
    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getGuarantee() {
        return guarantee;
    }
    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }
}