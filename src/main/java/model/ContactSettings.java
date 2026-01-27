package model;

public class ContactSettings {
    private int id = 1;  // mặc định là 1 vì chỉ có 1 bản ghi config
    private String phone;
    private String email;
    private String address;
    private String facebookUrl;
    private String instagramUrl;
    private String twitterUrl;
    private String googleUrl;

    // Constructor mặc định
    public ContactSettings() {}

    // Constructor đầy đủ (dùng khi lấy từ DB)
    public ContactSettings(int id, String phone, String email, String address,
                           String facebookUrl, String instagramUrl, String twitterUrl, String googleUrl) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
        this.twitterUrl = twitterUrl;
        this.googleUrl = googleUrl;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getGoogleUrl() {
        return googleUrl;
    }

    public void setGoogleUrl(String googleUrl) {
        this.googleUrl = googleUrl;
    }

    @Override
    public String toString() {
        return "ContactSettings{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", facebookUrl='" + facebookUrl + '\'' +
                ", instagramUrl='" + instagramUrl + '\'' +
                ", twitterUrl='" + twitterUrl + '\'' +
                ", googleUrl='" + googleUrl + '\'' +
                '}';
    }
}