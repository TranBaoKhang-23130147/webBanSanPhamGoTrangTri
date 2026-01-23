package model;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private String fullName; // Thêm trường này khớp với CheckoutServlet
    private String phone;    // Thêm trường này khớp với CheckoutServlet
    private String address;
    private String note;
    private Date createAt;
    private String paymentStatus;
    private String status;
    private List<OrderDetail> details;
    private double totalOrder;


    public Order() {}

    // Getter & Setter đầy đủ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Date getCreateAt() { return createAt; }
    public void setCreateAt(Date createAt) { this.createAt = createAt; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<OrderDetail> getDetails() { return details; }
    public void setDetails(List<OrderDetail> details) { this.details = details; }
    public double getTotalOrder() { return totalOrder; }
    public void setTotalOrder(double totalOrder) { this.totalOrder = totalOrder; }

    // Hàm giải quyết lỗi getTotalAmount trong MyPageServlet
    public double getTotalAmount() { return totalOrder; }
}