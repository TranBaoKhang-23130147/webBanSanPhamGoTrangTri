package model;

public class OrderDetail {
    private int id;                 // id
    private int orderId;            // order_id
    private int productVariantId;   // product_variant_id
    private int quantity;           // quantity
    private double total;           // total (decimal)

    // Các thuộc tính bổ sung để hiển thị lên giao diện (JOIN từ bảng sản phẩm)
    private String productName;
    private String productImg;
    private String color;
    private String size;

    public OrderDetail() {}

    // Getter và Setter
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductImg() { return productImg; }
    public void setProductImg(String productImg) { this.productImg = productImg; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    // ... Thêm các getter/setter còn lại ...
}