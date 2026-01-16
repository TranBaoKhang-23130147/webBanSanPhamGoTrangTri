package model;
import java.sql.Date;

public class Payment {
    private int id;
    private int userId;
    private Date duration;
    private String type;

    public Payment() {}
    public Payment(int id, int userId, Date duration, String type) {
        this.id = id;
        this.userId = userId;
        this.duration = duration;
        this.type = type;
    }
    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Date getDuration() { return duration; }
    public void setDuration(Date duration) { this.duration = duration; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}