package fr.rader.bob.packet;

public class Packet {

    private final byte[] rawData;
    private final int packetID;

    private int timestamp;

    public Packet(byte[] rawData, int packetID) {
        this.rawData = rawData;
        this.packetID = packetID;
    }

    public int getPacketID() {
        return packetID;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getTimestamp() {
        return timestamp;
    }
}
