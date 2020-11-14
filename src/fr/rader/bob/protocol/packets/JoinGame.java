package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.nbt.NBTTagCompound;
import fr.rader.bob.protocol.Packet;

public class JoinGame implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int entityID;
    private boolean isHardcore;
    private int gamemode;
    private int previousGamemode;
    private int worldCount;
    private String[] worldNames;
    private NBTTagCompound dimensionCodec;
    private NBTTagCompound dimension;
    private String worldName;
    private long hashedSeed;
    private int maxPlayers;
    private int viewDistance;
    private boolean reducedDebugInfo;
    private boolean enableRespawnScreen;
    private boolean isDebug;
    private boolean isFlat;

    public JoinGame(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        entityID = reader.readInt();
        isHardcore = reader.readBoolean();
        gamemode = reader.readByte();
        previousGamemode = reader.readByte();
        worldCount = reader.readVarInt();

        worldNames = new String[worldCount];
        for(int i = 0; i < worldCount; i++)
            worldNames[i] = reader.readString(reader.readByte());

        dimensionCodec = reader.readNBT();
        dimension = reader.readNBT();
        worldName = reader.readString(reader.readByte());
        hashedSeed = reader.readLong();
        maxPlayers = reader.readVarInt();
        viewDistance = reader.readVarInt();
        reducedDebugInfo = reader.readBoolean();
        enableRespawnScreen = reader.readBoolean();
        isDebug = reader.readBoolean();
        isFlat = reader.readBoolean();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeInt(entityID);
        writer.writeBoolean(isHardcore);
        writer.writeByte(gamemode);
        writer.writeByte(previousGamemode);
        writer.writeVarInt(worldCount);

        for(String name : worldNames) {
            writer.writeByte(name.length());
            writer.writeString(name);
        }

        writer.writeNBT(dimensionCodec);
        writer.writeNBT(dimension);
        writer.writeByte(worldName.length());
        writer.writeString(worldName);
        writer.writeLong(hashedSeed);
        writer.writeVarInt(maxPlayers);
        writer.writeVarInt(viewDistance);
        writer.writeBoolean(reducedDebugInfo);
        writer.writeBoolean(enableRespawnScreen);
        writer.writeBoolean(isDebug);
        writer.writeBoolean(isFlat);

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

    public boolean isHardcore() {
        return isHardcore;
    }

    public void setHardcore(boolean hardcore) {
        isHardcore = hardcore;
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

    public int getWorldCount() {
        return worldCount;
    }

    public void setWorldCount(int worldCount) {
        this.worldCount = worldCount;
    }

    public String[] getWorldNames() {
        return worldNames;
    }

    public void setWorldNames(String[] worldNames) {
        this.worldNames = worldNames;
    }

    public NBTTagCompound getDimensionCodec() {
        return dimensionCodec;
    }

    public void setDimensionCodec(NBTTagCompound dimensionCodec) {
        this.dimensionCodec = dimensionCodec;
    }

    public NBTTagCompound getDimension() {
        return dimension;
    }

    public void setDimension(NBTTagCompound dimension) {
        this.dimension = dimension;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public long getHashedSeed() {
        return hashedSeed;
    }

    public void setHashedSeed(long hashedSeed) {
        this.hashedSeed = hashedSeed;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    public boolean isReducedDebugInfo() {
        return reducedDebugInfo;
    }

    public void setReducedDebugInfo(boolean reducedDebugInfo) {
        this.reducedDebugInfo = reducedDebugInfo;
    }

    public boolean isEnableRespawnScreen() {
        return enableRespawnScreen;
    }

    public void setEnableRespawnScreen(boolean enableRespawnScreen) {
        this.enableRespawnScreen = enableRespawnScreen;
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
}