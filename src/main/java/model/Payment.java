package model;

import java.sql.Date;

public class Payment {
    private int id;
    private int userId;
    private String cardNumber; // Thêm mới
    private Date duration;
    private String type;

    // Cập nhật Constructor
    public Payment(int id, int userId, String cardNumber, Date duration, String type) {
        this.id = id;
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.duration = duration;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}