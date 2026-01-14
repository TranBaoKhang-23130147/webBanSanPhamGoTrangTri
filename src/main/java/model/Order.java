package model;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;              // id
    private int userId;          // user_id
    private int addressId;       // address_id
    private int cartId;          // cart_id
    private String note;         // note
    private Date createAt;       // createAt (datetime)
    private int paymentId;       // payment_id
    private String paymentStatus;// payment_status
    private String status;       // status

    // Dùng để hiển thị danh sách sản phẩm trong đơn và tổng tiền
    private List<OrderDetail> details;
    private double totalOrder;

    public Order() {}

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getCreateAt() { return createAt; }
    public void setCreateAt(Date createAt) { this.createAt = createAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public List<OrderDetail> getDetails() { return details; }
    public void setDetails(List<OrderDetail> details) { this.details = details; }
    public double getTotalOrder() { return totalOrder; }
    public void setTotalOrder(double totalOrder) { this.totalOrder = totalOrder; }
    // ... Thêm các getter/setter còn lại nếu cần ...
}