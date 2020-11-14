package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class FacePlayer implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int feetOrEyes;
    private double targetX;
    private double targetY;
    private double targetZ;
    private boolean isEntity;
    private int entityID;
    private int entityFeetOrEyes;

    public FacePlayer(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        feetOrEyes = reader.readVarInt();
        targetX = reader.readDouble();
        targetY = reader.readDouble();
        targetZ = reader.readDouble();
        isEntity = reader.readBoolean();

        if(isEntity) {
            entityID = reader.readVarInt();
            entityFeetOrEyes = reader.readVarInt();
        }
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeVarInt(feetOrEyes);
        writer.writeDouble(targetX);
        writer.writeDouble(targetY);
        writer.writeDouble(targetZ);
        writer.writeBoolean(isEntity);

        if(isEntity) {
            writer.writeVarInt(entityID);
            writer.writeVarInt(entityFeetOrEyes);
        }

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

    public int getFeetOrEyes() {
        return feetOrEyes;
    }

    public void setFeetOrEyes(int feetOrEyes) {
        this.feetOrEyes = feetOrEyes;
    }

    public double getTargetX() {
        return targetX;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    public double getTargetZ() {
        return targetZ;
    }

    public void setTargetZ(double targetZ) {
        this.targetZ = targetZ;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public void setEntity(boolean entity) {
        isEntity = entity;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public int getEntityFeetOrEyes() {
        return entityFeetOrEyes;
    }

    public void setEntityFeetOrEyes(int entityFeetOrEyes) {
        this.entityFeetOrEyes = entityFeetOrEyes;
    }
}