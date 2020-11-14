package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class StopSound implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int flags;
    private int source;
    private String identifier;

    public StopSound(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        flags = reader.readByte();

        if((flags & 0x01) == 1) {
            source = reader.readVarInt();
        }

        if((flags & 0x02) >> 1 == 1) {
            identifier = reader.readIdentifier();
        }
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeByte(flags);

        if((flags & 0x01) == 1) {
            writer.writeVarInt(source);
        }

        if((flags & 0x02) >> 1 == 1) {
            writer.writeIdentifier(identifier);
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

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}