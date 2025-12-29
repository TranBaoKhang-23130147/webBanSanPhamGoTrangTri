package model;

public class Source {
    private int id;
    private String sourceName;

    public Source() {}
    public Source(int id, String sourceName) {
        this.id = id;
        this.sourceName = sourceName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
}
