package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class KeepAlive implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private long keepAliveID;

    public KeepAlive(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        keepAliveID = reader.readLong();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeLong(keepAliveID);

        return writer.getData();
    }

    @Override
    public int getLength() {
        return writePacket().length;
    }

    @Override
    public byte getPacketID() {
        return packetID;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public long getKeepAliveID() {
        return keepAliveID;
    }

    public void setKeepAliveID(long keepAliveID) {
        this.keepAliveID = keepAliveID;
    }
}