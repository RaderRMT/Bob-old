package fr.rader.bob.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.Packet;
import fr.rader.bob.PacketHeader;
import fr.rader.bob.nbt.NBTTagCompound;

public class P24JoinGame extends Packet {

    private int entityID;
    private boolean isHardcore;
    private int gamemode; // should be byte, but java don't have unsigned byte
    private int previousGamemode; // should be byte, but java don't have unsigned byte
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

    public P24JoinGame(PacketHeader header, byte[] rawData) {
        super(header, rawData);

        readData(rawData);
    }

    private void readData(byte[] rawData) {
        DataReader reader = new DataReader(rawData);
        reader.startAt(1);

        entityID = reader.readInt();
        isHardcore = reader.readBoolean();
        gamemode = reader.readByte();
        previousGamemode = reader.readByte();
        worldCount = reader.readVarInt();

        worldNames = new String[worldCount];
        for(int i = 0; i < worldCount; i++)
            worldNames[i] = reader.readString(reader.readByte());

        dimensionCodec = new NBTTagCompound(reader.getFromOffset(false), false);
        reader.addOffset(dimensionCodec.getLength());
        dimension = new NBTTagCompound(reader.getFromOffset(false), false);
        reader.addOffset(dimension.getLength());

        worldName = reader.readString(reader.readByte());
        hashedSeed = reader.readLong();
        maxPlayers = reader.readVarInt();
        viewDistance = reader.readVarInt();
        reducedDebugInfo = reader.readBoolean();
        enableRespawnScreen = reader.readBoolean();
        isDebug = reader.readBoolean();
        isFlat = reader.readBoolean();
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
