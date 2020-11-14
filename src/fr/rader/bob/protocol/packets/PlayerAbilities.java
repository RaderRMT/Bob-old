package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class PlayerAbilities implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int flags;
    private float flyingSpeed;
    private float fovModifier;

    public PlayerAbilities(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        flags = reader.readByte();
        flyingSpeed = reader.readFloat();
        fovModifier = reader.readFloat();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeByte(flags);
        writer.writeFloat(flyingSpeed);
        writer.writeFloat(fovModifier);

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

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public float getFlyingSpeed() {
        return flyingSpeed;
    }

    public void setFlyingSpeed(float flyingSpeed) {
        this.flyingSpeed = flyingSpeed;
    }

    public float getFovModifier() {
        return fovModifier;
    }

    public void setFovModifier(float fovModifier) {
        this.fovModifier = fovModifier;
    }
}