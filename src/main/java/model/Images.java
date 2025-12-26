package model;

public class Images {
    private int id;
    private String urlImage;

    public Images() {}

    public Images(int id, String urlImage) {
        this.id = id;
        this.urlImage = urlImage;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUrlImage() { return urlImage; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }
}