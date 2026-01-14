package model;

public class Payment {
    private int id;
    private int userId;
    private String type;           // Ví dụ: VISA, MASTERCARD
    private String duration;       // Ngày hết hạn (Date)
//    private String cardNumber;     // Số thẻ (**** **** **** 9999)
//    private String cardHolderName; // Tên chủ thẻ (TRAN THI THUY KIEU)
//    private boolean isDefault;     // Thẻ mặc định hay không

    // Constructor mặc định
    public Payment() {
    }

    // Constructor đầy đủ tham số
//    public Payment(int id, int userId, String type, String duration, String cardNumber, String cardHolderName, boolean isDefault) {
//        this.id = id;
//        this.userId = userId;
//        this.type = type;
//        this.duration = duration;
//        this.cardNumber = cardNumber;
//        this.cardHolderName = cardHolderName;
//        this.isDefault = isDefault;
//    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

//    public String getCardNumber() {
//        return cardNumber;
//    }
//
//    public void setCardNumber(String cardNumber) {
//        this.cardNumber = cardNumber;
//    }
//
//    public String getCardHolderName() {
//        return cardHolderName;
//    }
//
//    public void setCardHolderName(String cardHolderName) {
//        this.cardHolderName = cardHolderName;
//    }
//
//    public boolean isIsDefault() { // Lưu ý: kiểu boolean thường dùng is...
//        return isDefault;
//    }
//
//    public void setIsDefault(boolean isDefault) {
//        this.isDefault = isDefault;
//    }
//
//    // Hàm toString để hỗ trợ debug/kiểm tra dữ liệu nhanh
//    @Override
//    public String toString() {
//        return "Payment{" + "id=" + id + ", userId=" + userId + ", type=" + type + ", duration=" + duration + ", cardNumber=" + cardNumber + ", cardHolderName=" + cardHolderName + ", isDefault=" + isDefault + '}';
//    }
}