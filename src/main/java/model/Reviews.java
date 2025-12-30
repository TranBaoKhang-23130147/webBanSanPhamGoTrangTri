package model;

public class Reviews {
    private int id;
    private int userId;
    private int productId;
    private int rating;
    private String comment;

    public Reviews() {}

    public Reviews(int id, int userId,int productId, int rating, String comment) {
        this.id = id;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.userId = userId;
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
