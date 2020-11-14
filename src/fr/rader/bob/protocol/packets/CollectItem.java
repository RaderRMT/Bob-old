package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class CollectItem implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int collectedEntityID;
    private int collectorEntityID;
    private int pickupItemCount;

    public CollectItem(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        collectedEntityID = reader.readVarInt();
        collectorEntityID = reader.readVarInt();
        pickupItemCount = reader.readVarInt();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeVarInt(collectedEntityID);
        writer.writeVarInt(collectorEntityID);
        writer.writeVarInt(pickupItemCount);

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

    public int getCollectedEntityID() {
        return collectedEntityID;
    }

    public void setCollectedEntityID(int collectedEntityID) {
        this.collectedEntityID = collectedEntityID;
    }

    public int getCollectorEntityID() {
        return collectorEntityID;
    }

    public void setCollectorEntityID(int collectorEntityID) {
        this.collectorEntityID = collectorEntityID;
    }

    public int getPickupItemCount() {
        return pickupItemCount;
    }

    public void setPickupItemCount(int pickupItemCount) {
        this.pickupItemCount = pickupItemCount;
    }
}