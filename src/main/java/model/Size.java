package model;

public class Size {
    private int id;
    private String sizeName;

    public Size() {}

    // Constructor khớp với SizeDao của bạn
    public Size(int id, String sizeName) {
        this.id = id;
        this.sizeName = sizeName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSizeName() { return sizeName; }
    public void setSizeName(String sizeName) { this.sizeName = sizeName; }
}