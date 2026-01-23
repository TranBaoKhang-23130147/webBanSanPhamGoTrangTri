package model;

public class OrderDetail {
    private int id;
    private int orderId;
    private int productVariantId;
    private int quantity;
    private double total;
    private String productImage; // 👈 THÊM DÒNG NÀY

    // Thuộc tính JOIN từ bảng Product và ProductVariant
    private String productName;
    private String productImg;
    private String color;
    private String size;

    public OrderDetail() {}
    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    // --- ĐẦY ĐỦ GETTER VÀ SETTER ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductVariantId() { return productVariantId; }
    public void setProductVariantId(int productVariantId) { this.productVariantId = productVariantId; }

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
}