package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class SelectAdvancementTab implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private byte[] rawData;

    public SelectAdvancementTab(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;
        this.rawData = rawData;
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeByteArray(rawData);

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

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }
}