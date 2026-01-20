package model;


import java.sql.Date;
import java.util.List;

public class Product {
    private int id;
    private String nameProduct;
    private int descriptionId;
    private int categoryId;
    private int sourceId;
    private int productTypeId;
    private double price;
    private int totalStock; // Thêm trường này

    private int primaryImageId;
    private int isActive;
    private Date mfgDate;
    private String imageUrl;
    private Reviews reviews;
    private double averageRating;
    private Source source;
    private List<Images> subImages; // Danh sách ảnh biến thể
    private Description detailDescription; // Chứa introduce, highlights và Information object
    private List<ProductVariants> variants; // Danh sách các biến thể màu/size
    private List<Reviews> reviewList;       // Danh sách các đánh giá chi tiết
    private int totalReviews;
    private String categoryName; // Thêm dòng này
    private String typeName;     // Thêm dòng này

    public Product(String name, double price, int catId, int typeId, int sourceId, String mfgDate) {
    }

    // Bắt buộc phải có Getter và Setter cho 2 biến trên thì DAO mới gọi được
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public Product() {}
    public Product(int id, String nameProduct, double price, String imageUrl, double averageRating) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.price = price;
        this.imageUrl = imageUrl;
        this.averageRating = averageRating;
    }

    // Giữ nguyên các Getter/Setter cũ của bạn và thêm Getter/Setter cho imageUrl
    public Source getSource() { return source; }
    public void setSource(Source source) { this.source = source; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNameProduct() { return nameProduct; }
    public void setNameProduct(String nameProduct) { this.nameProduct = nameProduct; }
    public int getDescriptionID() { return descriptionId; }
    public void setDescription(int descriptionId) { this.descriptionId = descriptionId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getSourceId() { return sourceId; }
    public void setSourceId(int sourceId) { this.sourceId = sourceId; }
    public int getProductTypeId() { return productTypeId; }
    public void setProductTypeId(int productTypeId) { this.productTypeId = productTypeId; }

    public int getPrimaryImageId() { return primaryImageId; }
    public void setPrimaryImageId(int primaryImageId) { this.primaryImageId = primaryImageId; }
    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }
    public Date getMfgDate() {
        return mfgDate;
    }

    // Setter
    public void setMfgDate(Date mfgDate) {
        this.mfgDate = mfgDate;
    }
    public Reviews getReviews() { return reviews; }
    public void setReviews(Reviews reviews) { this.reviews = reviews; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public List<Images> getSubImages() { return subImages; }
    public void setSubImages(List<Images> subImages) { this.subImages = subImages; }
    public Description getDetailDescription() { return detailDescription; }
    public void setDetailDescription(Description desc) { this.detailDescription = desc; }
    public List<ProductVariants> getVariants() { return variants; }
    public void setVariants(List<ProductVariants> variants) { this.variants = variants; }
    public List<Reviews> getReviewList() { return reviewList; }
    public void setReviewList(List<Reviews> reviewList) { this.reviewList = reviewList; }
    public int getTotalReviews() { return totalReviews; }
    public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }

        // ... các trường cũ ...

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public int getTotalStock() { return totalStock; }
        public void setTotalStock(int totalStock) { this.totalStock = totalStock; }

    public void setProductTypeName(String productTypeName) {
    }

    public void setTypeId(int typeId) {
    }
}