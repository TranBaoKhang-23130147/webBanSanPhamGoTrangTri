package model;

import java.math.BigDecimal;

public class ProductSize {
    private int id;
    private String size_name;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;

    public ProductSize() {}

    public ProductSize(int id, String size_name, BigDecimal length, BigDecimal width, BigDecimal height) {
        this.id = id;
        this.size_name = size_name;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSize_name() { return size_name; }
    public void setSize_name(String size_name) { this.size_name = size_name; }

    public BigDecimal getLength() { return length; }
    public void setLength(BigDecimal length) { this.length = length; }

    public BigDecimal getWidth() { return width; }
    public void setWidth(BigDecimal width) { this.width = width; }

    public BigDecimal getHeight() { return height; }
    public void setHeight(BigDecimal height) { this.height = height; }
}