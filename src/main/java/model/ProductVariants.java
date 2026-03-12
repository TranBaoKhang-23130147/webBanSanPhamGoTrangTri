package model;

import java.math.BigDecimal;
import java.util.Date;

public class ProductVariants {
    private int id;
    private int product_id;
    private int color_id;
    private int size_id;
    private String sku;
    private int inventory_quantity;
    private BigDecimal variant_price;
    private ProductColor color;
    private ProductSize size;
    private int discount_percent;
    private Date discount_end_date;

    public ProductVariants() {
    }

    public ProductVariants(int id, int product_id, int color_id, int size_id, String sku, int inventory_quantity, BigDecimal variant_price) {
        this.id = id;
        this.product_id = product_id;
        this.color_id = color_id;
        this.size_id = size_id;
        this.sku = sku;
        this.inventory_quantity = inventory_quantity;
        this.variant_price = variant_price;
    }

    public ProductVariants(int id, int product_id, int color_id, int size_id,
                           String sku, int inventory_quantity,
                           BigDecimal variant_price,
                           int discount_percent, Date discount_end_date) {
        this.id = id;
        this.product_id = product_id;
        this.color_id = color_id;
        this.size_id = size_id;
        this.sku = sku;
        this.inventory_quantity = inventory_quantity;
        this.variant_price = variant_price;
        this.discount_percent = discount_percent;
        this.discount_end_date = discount_end_date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public ProductColor getColor() {
        return color;
    }
    public void setColor(ProductColor color) {
        this.color = color;
    }

    public ProductSize getSize() {
        return size;
    }
    public void setSize(ProductSize size) {
        this.size = size;
    }

    public int getProduct_id() {
        return product_id;
    }
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getColor_id() {
        return color_id;
    }
    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public int getSize_id() {
        return size_id;
    }
    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getInventory_quantity() {
        return inventory_quantity;
    }
    public void setInventory_quantity(int inventory_quantity) {
        this.inventory_quantity = inventory_quantity;
    }

    public BigDecimal getVariant_price() {
        return variant_price;
    }
    public void setVariant_price(BigDecimal variant_price) {
        this.variant_price = variant_price;
    }
    public int getDiscount_percent() {
        return discount_percent;
    }
    public void setDiscount_percent(int discount_percent) {
        this.discount_percent = discount_percent;
    }

    public Date getDiscount_end_date() {
        return discount_end_date;
    }
    public void setDiscount_end_date(Date discount_end_date) {
        this.discount_end_date = discount_end_date;
    }

    public BigDecimal getFinal_price() {
        if (discount_percent > 0 && discount_end_date != null
                && discount_end_date.after(new java.util.Date())) {

            BigDecimal discount = variant_price
                    .multiply(BigDecimal.valueOf(discount_percent))
                    .divide(BigDecimal.valueOf(100));

            return variant_price.subtract(discount);
        }
        return variant_price;
    }
    public boolean isDiscountActive() {
        return discount_percent > 0
                && discount_end_date != null
                && discount_end_date.after(new java.util.Date());
    }
    @Override
    public String toString() {
        return "ProductVariant{" +
                "id=" + id +
                ", productId=" + product_id +
                ", sku='" + sku + '\'' +
                ", price=" + variant_price +
                '}';
    }
}