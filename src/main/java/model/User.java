
    package model;

    import java.util.Date;

    public class User {
        private int id;
        private String username;
        private String password;
        private String status;
        private String role;
        private String email;
        private String displayName;
        private String phone;
        private Date birthDate;
        private String gender;
        private String avatarUrl;


        private Integer avatarId;       // ID ảnh đại diện (khóa ngoại)
        private Date createAt;// Giới tính// Ngày sinh
        private Address address;

        // Constructor đầy đủ
        public User(String username, String password, String status, String role, String email, int id) {
            this.username = username;
            this.password = password;
            this.status = status;
            this.role = role;
            this.email = email;
            this.id = id;
        }

        // Constructor rút gọn
        public User(int id, String username, String status, String password) {
            this.id = id;
            this.username = username;
            this.status = status;
            this.password = password;
        }

        // 2. Constructor đầy đủ (Cho việc lấy dữ liệu từ DB)
        public User(int id, String username, String displayName, Date birthDate, String email,
                    String password, String phone, String gender, int avatarId, Date createAt,
                    String role, String status) {
            this.id = id;
            this.username = username;
            this.displayName = displayName;
            this.birthDate = birthDate;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.gender = gender;
            this.avatarId = avatarId;
            this.createAt = createAt;
            this.role = role;
            this.status = status;
        }

        // Constructor rỗng
        public User() {
        }

        // Getters and Setters

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Date getCreateAt() {
            return createAt;
        }

        public void setCreateAt(Date createAt) {
            this.createAt = createAt;
        }

        public Integer getAvatarId() {
            return avatarId;
        }

        public void setAvatarId(Integer avatarId) {
            this.avatarId = avatarId;
        }

        public Date getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Date birthDate) { // Tham số phải là birthDate
            this.birthDate = birthDate;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

    }
