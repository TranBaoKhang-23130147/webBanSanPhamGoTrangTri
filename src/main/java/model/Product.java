package model;

import java.sql.Date;

public class Product {
    private int id;
    private String nameProduct;
    private String description;
    private int categoryId;
    private int sourceId;
    private int productTypeId;
    private double price;
    private int primaryImageId;
    private int isActive;
    private Date mfgDate;
    private String imageUrl;
    private Reviews reviews;
    private double averageRating;

    public Product() {}
    public Product(int id, String nameProduct, double price, String imageUrl, int isActive) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }

    // Giữ nguyên các Getter/Setter cũ của bạn và thêm Getter/Setter cho imageUrl
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNameProduct() { return nameProduct; }
    public void setNameProduct(String nameProduct) { this.nameProduct = nameProduct; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getSourceId() { return sourceId; }
    public void setSourceId(int sourceId) { this.sourceId = sourceId; }
    public int getProductTypeId() { return productTypeId; }
    public void setProductTypeId(int productTypeId) { this.productTypeId = productTypeId; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getPrimaryImageId() { return primaryImageId; }
    public void setPrimaryImageId(int primaryImageId) { this.primaryImageId = primaryImageId; }
    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }
    public Date getMfgDate() { return mfgDate; }
    public void setMfgDate(Date mfgDate) { this.mfgDate = mfgDate; }
    public Reviews getReviews() { return reviews; }
    public void setReviews(Reviews reviews) { this.reviews = reviews; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
}