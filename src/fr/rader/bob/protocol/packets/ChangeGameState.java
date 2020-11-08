package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class ChangeGameState implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private int reason;
    private float value;

    public ChangeGameState(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        reason = reader.readByte();
        value = reader.readFloat();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeByte(reason);
        writer.writeFloat(value);

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

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}