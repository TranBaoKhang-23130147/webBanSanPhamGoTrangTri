package model;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private String fullName;
    private String phone;
    private String address;
    private String note;
    private Date createAt;
    private String paymentStatus;
    private String status;
    private List<OrderDetail> details;
    private double totalOrder;
    private String productName;
    private double subTotal;
    private double taxAmount;
    private double shippingFee;


    public Order() {}

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
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Date getCreateAt() {
        return createAt;
    }
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<OrderDetail> getDetails() {
        return details;
    }
    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }
    public double getTotalOrder() {
        return totalOrder;
    }
    public void setTotalOrder(double totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAddressDetail() {
        return address;
    }
    public double getTotalAmount() {
        return totalOrder;
    }

    public double getSubTotal() {
        return subTotal;
    }
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTaxAmount() {
        return taxAmount;
    }
    public void setTaxAmount(double taxAmount) {
         this.taxAmount = taxAmount;
    }

    public double getShippingFee() {
        return shippingFee;
    }
    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }


}