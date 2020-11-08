package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.UUID;
import fr.rader.bob.protocol.Packet;

public class BossBar implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private UUID uuid;
    private int action;

    // action == 0
    private String title;   // also for action == 3
    private float health; // also for action == 2
    private int color;    // also for action == 4
    private int division; // also for action == 4
    private int flags;    // also for action == 5

    public BossBar(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        uuid = reader.readUUID();
        action = reader.readVarInt();

        switch(action) {
            case 1:
                title = reader.readChat();
                health = reader.readFloat();
                color = reader.readVarInt();
                division = reader.readVarInt();
                flags = reader.readByte();
                break;
            case 2:
                break;
            case 3:
                health = reader.readFloat();
                break;
            case 4:
                color = reader.readVarInt();
                division = reader.readVarInt();
            case 5:
                flags = reader.readByte();
        }
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeUUID(uuid);
        writer.writeVarInt(action);

        switch(action) {
            case 1:
                writer.writeChat(title);
                writer.writeFloat(health);
                writer.writeVarInt(color);
                writer.writeVarInt(division);
                writer.writeByte(flags);
                break;
            case 2:
                break;
            case 3:
                writer.writeFloat(health);
                break;
            case 4:
                writer.writeVarInt(color);
                writer.writeVarInt(division);
            case 5:
                writer.writeByte(flags);
        }

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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }
}