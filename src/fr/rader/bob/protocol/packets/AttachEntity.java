package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class AttachEntity implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int attachedEntityID;
    private int holdingEntityID;

    public AttachEntity(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        attachedEntityID = reader.readInt();
        holdingEntityID = reader.readInt();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeInt(attachedEntityID);
        writer.writeInt(holdingEntityID);

        return writer.getData();
    }

    @Override
    public int getLength() {
        return writePacket().length;
    }

    @Override
    public int getPacketID() {
        return packetID;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public int getAttachedEntityID() {
        return attachedEntityID;
    }

    public void setAttachedEntityID(int attachedEntityID) {
        this.attachedEntityID = attachedEntityID;
    }

    public int getHoldingEntityID() {
        return holdingEntityID;
    }

    public void setHoldingEntityID(int holdingEntityID) {
        this.holdingEntityID = holdingEntityID;
    }
}