package model;

public class Address {
    private int id;
    private int userId;
    private String province;
    private String district;
    private String commune;
    private String detail;
    private String name;
    private String phone;
    private int isDefault;

    // Constructor không tham số
    public Address() {
    }

    // Hàm tiện ích để in địa chỉ đầy đủ
    public String getFullAddress() {
        if (detail == null || detail.isEmpty()) return "Chưa cập nhật";
        return detail + ", " + commune + ", " + district + ", " + province;
    }

    // --- GETTERS AND SETTERS ---

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}