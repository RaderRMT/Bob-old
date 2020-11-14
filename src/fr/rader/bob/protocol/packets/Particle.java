package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;
import fr.rader.bob.types.Slot;

public class Particle implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int particleID;
    private boolean longDistance;
    private double x;
    private double y;
    private double z;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float particleData;
    private int particleCount;
    private Object data;

    public Particle(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        particleID = reader.readInt();
        longDistance = reader.readBoolean();
        x = reader.readDouble();
        y = reader.readDouble();
        z = reader.readDouble();
        offsetX = reader.readFloat();
        offsetY = reader.readFloat();
        offsetZ = reader.readFloat();
        particleData = reader.readFloat();
        particleCount = reader.readInt();

        switch(particleID) {
            case 3:
            case 23:
                data = reader.readVarInt();   // blockstate
                break;
            case 13:
                data = new float[] {
                        reader.readFloat(),   // red
                        reader.readFloat(),   // green
                        reader.readFloat(),   // blue
                        reader.readFloat()    // scale
                };
                break;
            case 32:
                data = reader.readSlot();
                break;
        }
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeInt(particleID);
        writer.writeBoolean(longDistance);
        writer.writeDouble(x);
        writer.writeDouble(y);
        writer.writeDouble(z);
        writer.writeFloat(offsetX);
        writer.writeFloat(offsetY);
        writer.writeFloat(offsetZ);
        writer.writeFloat(particleData);
        writer.writeInt(particleCount);

        if(data instanceof Integer) {
            writer.writeVarInt((Integer) data);
        } else if(data instanceof Float[]) {
            for(float value : (Float[]) data)
                writer.writeFloat(value);
        } else if(data instanceof Slot) {
            writer.writeSlot((Slot) data);
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getParticleID() {
        return particleID;
    }

    public void setParticleID(int particleID) {
        this.particleID = particleID;
    }

    public boolean isLongDistance() {
        return longDistance;
    }

    public void setLongDistance(boolean longDistance) {
        this.longDistance = longDistance;
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

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(float offsetZ) {
        this.offsetZ = offsetZ;
    }

    public float getParticleData() {
        return particleData;
    }

    public void setParticleData(float particleData) {
        this.particleData = particleData;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public void setParticleCount(int particleCount) {
        this.particleCount = particleCount;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}