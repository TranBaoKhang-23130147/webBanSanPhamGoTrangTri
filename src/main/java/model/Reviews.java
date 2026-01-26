package model;

import java.sql.Timestamp;

public class Reviews {
    private int id;
    private int userId;
    private int productId;
    private int rating;
    private String comment;
    private Timestamp createAt;
    private boolean rated;

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }


    // Thêm các trường hỗ trợ hiển thị từ JOIN SQL
    private String productName;
    private String productImg;

    public Reviews() {}

    // Getters và Setters cho các trường cũ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Timestamp getCreateAt() { return createAt; }
    public void setCreateAt(Timestamp createAt) { this.createAt = createAt; }

    // Getters và Setters cho các trường mới thêm
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductImg() { return productImg; }
    public void setProductImg(String productImg) { this.productImg = productImg; }
}