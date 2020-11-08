package fr.rader.bob.protocol;

public class VersionRange {

    private String baseVersion;
    private int from;
    private int to;

    public VersionRange(String baseVersion, int from, int to) {
        this.baseVersion = baseVersion;
        this.from = from;
        this.to = to;
    }

    public String getBaseVersion() {
        return baseVersion;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
