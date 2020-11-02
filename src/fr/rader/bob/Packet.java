package fr.rader.bob;

public class Packet {

    private PacketHeader header;
    private byte[] data;

    public Packet(PacketHeader header, byte[] rawData) {
        this.header = header;
        this.data = rawData;
    }

    public PacketHeader getHeader() {
        return header;
    }

    public byte[] getRawData() {
        return data;
    }
}
