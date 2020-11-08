package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.UUID;
import fr.rader.bob.protocol.Packet;

public class SpawnPlayer implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private int entityID;
    private UUID playerUUID;
    private double x;
    private double y;
    private double z;
    private int pitch;
    private int yaw;

    public SpawnPlayer(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        entityID = reader.readVarInt();
        playerUUID = reader.readUUID();
        x = reader.readDouble();
        y = reader.readDouble();
        z = reader.readDouble();
        pitch = reader.readByte();
        yaw = reader.readByte();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeVarInt(entityID);
        writer.writeUUID(playerUUID);
        writer.writeDouble(x);
        writer.writeDouble(y);
        writer.writeDouble(z);
        writer.writeByte(pitch);
        writer.writeByte(yaw);

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

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getYaw() {
        return yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }
}
