package model;

public class Description {
    private int id;
    private String introduce;
    private String highlights;
    private int informationId;
    private Information information;

    public Description() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getIntroduce() {
        return introduce;
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getHighlights() {
        return highlights;
    }
    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public int getInformationId() {
        return informationId;
    }
    public void setInformationId(int informationId) {
        this.informationId = informationId;
    }

    public Information getInformation() {
        return information;
    }
    public void setInformation(Information information) {
        this.information = information;
    }
}