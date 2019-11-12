package dk.trustworks.marginservice.network.model;

public class MarginResult {

    private int margin;

    public MarginResult() {
    }

    public MarginResult(int margin) {
        this.margin = margin;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    @Override
    public String toString() {
        return "MarginResult{" +
                "margin=" + margin +
                '}';
    }
}
