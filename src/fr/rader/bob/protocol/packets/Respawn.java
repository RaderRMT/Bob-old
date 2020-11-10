package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.nbt.NBTTagCompound;
import fr.rader.bob.protocol.Packet;

public class Respawn implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private NBTTagCompound dimension;
    private String identifier;
    private long hashedSeed;
    private int gamemode;
    private int previousGamemode;
    private boolean isDebug;
    private boolean isFlat;
    private boolean copyMetadata;

    public Respawn(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        dimension = reader.readNBT();
        identifier = reader.readIdentifier();
        hashedSeed = reader.readLong();
        gamemode = reader.readByte();
        previousGamemode = reader.readByte();
        isDebug = reader.readBoolean();
        isFlat = reader.readBoolean();
        copyMetadata = reader.readBoolean();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeNBT(dimension);
        writer.writeIdentifier(identifier);
        writer.writeLong(hashedSeed);
        writer.writeByte(gamemode);
        writer.writeByte(previousGamemode);
        writer.writeBoolean(isDebug);
        writer.writeBoolean(isFlat);
        writer.writeBoolean(copyMetadata);

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

    public NBTTagCompound getDimension() {
        return dimension;
    }

    public void setDimension(NBTTagCompound dimension) {
        this.dimension = dimension;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public long getHashedSeed() {
        return hashedSeed;
    }

    public void setHashedSeed(long hashedSeed) {
        this.hashedSeed = hashedSeed;
    }

    public int getGamemode() {
        return gamemode;
    }

    public void setGamemode(int gamemode) {
        this.gamemode = gamemode;
    }

    public int getPreviousGamemode() {
        return previousGamemode;
    }

    public void setPreviousGamemode(int previousGamemode) {
        this.previousGamemode = previousGamemode;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public boolean isFlat() {
        return isFlat;
    }

    public void setFlat(boolean flat) {
        isFlat = flat;
    }

    public boolean isCopyMetadata() {
        return copyMetadata;
    }

    public void setCopyMetadata(boolean copyMetadata) {
        this.copyMetadata = copyMetadata;
    }
}