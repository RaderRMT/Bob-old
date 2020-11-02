package fr.rader.bob;

public class PacketHeader {

    private int timestamp;
    private int dataLength;

    public PacketHeader(int timestamp, int dataLength) {
        this.timestamp = timestamp;
        this.dataLength = dataLength;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getDataLength() {
        return dataLength;
    }
}
