package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.UUID;
import fr.rader.bob.protocol.Packet;

public class SpawnLivingEntity implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private int entityID;
    private UUID entityUUID;
    private int type;
    private double x;
    private double y;
    private double z;
    private int pitch;
    private int yaw;
    private int headPitch;
    private int velX;
    private int velY;
    private int velZ;

    public SpawnLivingEntity(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        entityID = reader.readVarInt();
        entityUUID = reader.readUUID();
        type = reader.readVarInt();
        x = reader.readDouble();
        y = reader.readDouble();
        z = reader.readDouble();
        pitch = reader.readByte();
        yaw = reader.readByte();
        headPitch = reader.readByte();
        velX = reader.readShort();
        velY = reader.readShort();
        velZ = reader.readShort();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeVarInt(entityID);
        writer.writeUUID(entityUUID);
        writer.writeVarInt(type);
        writer.writeDouble(x);
        writer.writeDouble(y);
        writer.writeDouble(z);
        writer.writeByte(pitch);
        writer.writeByte(yaw);
        writer.writeByte(headPitch);
        writer.writeShort(velX);
        writer.writeShort(velY);
        writer.writeShort(velZ);

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

    public UUID getEntityUUID() {
        return entityUUID;
    }

    public void setEntityUUID(UUID entityUUID) {
        this.entityUUID = entityUUID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getHeadPitch() {
        return headPitch;
    }

    public void setHeadPitch(int headPitch) {
        this.headPitch = headPitch;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getVelZ() {
        return velZ;
    }

    public void setVelZ(int velZ) {
        this.velZ = velZ;
    }
}
