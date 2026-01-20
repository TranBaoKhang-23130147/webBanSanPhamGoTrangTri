package model; // Thay đổi package cho đúng với cấu trúc dự án của bạn

public class ProductVariant {
    private int id;
    private int productId;
    private int colorId;
    private int sizeId;
    private String sku;
    private int inventoryQuantity;
    private double variantPrice;

    // 1. Hàm khởi tạo không tham số (Bắt buộc nếu dùng một số Framework)
    public ProductVariant() {
    }

    // 2. Hàm khởi tạo dùng khi thêm mới (Không cần ID vì ID tự tăng trong DB)
    public ProductVariant(int colorId, int sizeId, String sku, int inventoryQuantity, double variantPrice) {
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.sku = sku;
        this.inventoryQuantity = inventoryQuantity;
        this.variantPrice = variantPrice;
    }

    // 3. Hàm khởi tạo đầy đủ (Dùng khi lấy dữ liệu từ DB lên)
    public ProductVariant(int id, int productId, int colorId, int sizeId, String sku, int inventoryQuantity, double variantPrice) {
        this.id = id;
        this.productId = productId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.sku = sku;
        this.inventoryQuantity = inventoryQuantity;
        this.variantPrice = variantPrice;
    }

    // --- GETTERS AND SETTERS ---


    private Integer soldQuantity;      // số lượng đã bán (nếu có)


    public Integer getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(int inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public double getVariantPrice() {
        return variantPrice;
    }

    public void setVariantPrice(double variantPrice) {
        this.variantPrice = variantPrice;
    }

    // Ghi đè phương thức toString để dễ dàng Debug (Kiểm tra dữ liệu)
    @Override
    public String toString() {
        return "ProductVariant{" +
                "id=" + id +
                ", productId=" + productId +
                ", sku='" + sku + '\'' +
                ", price=" + variantPrice +
                '}';
    }

}