package model;

import java.math.BigDecimal;

public class CartItem {
    private Product product;
    private ProductVariants variant;
    private int quantity;
    private BigDecimal totalPrice;

    public CartItem() {}

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductVariants getVariant() {
        return variant;
    }
    public void setVariant(ProductVariants variant) {
        this.variant = variant;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return variant.getVariant_price()
                .multiply(BigDecimal.valueOf(quantity));
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
