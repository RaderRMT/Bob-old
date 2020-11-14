package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.types.Position;
import fr.rader.bob.protocol.Packet;

public class BlockBreakAnimation implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int entityID;
    private Position location;
    private int destroyStage;

    public BlockBreakAnimation(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        entityID = reader.readVarInt();
        location = reader.readPosition();
        destroyStage = reader.readByte();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeVarInt(entityID);
        writer.writePosition(location);
        writer.writeByte(destroyStage);

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

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public Position getLocation() {
        return location;
    }

    public void setLocation(Position location) {
        this.location = location;
    }

    public int getDestroyStage() {
        return destroyStage;
    }

    public void setDestroyStage(int destroyStage) {
        this.destroyStage = destroyStage;
    }
}
