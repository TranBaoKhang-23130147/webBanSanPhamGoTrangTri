package model;

public class Source {
    private int id;
    private String sourceName;

    public Source() {}
    public Source(int id, String sourceName) {
        this.id = id;
        this.sourceName = sourceName;
    }
    private long totalInventory;   // ← thêm field này

    // constructor, getters, setters
    public long getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(long totalInventory) {
        this.totalInventory = totalInventory;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
}
