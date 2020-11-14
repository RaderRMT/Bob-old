package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.nbt.NBTTagCompound;
import fr.rader.bob.protocol.Packet;

public class ChunkData implements Packet {

    private int packetID;
    private int timestamp;
    private int packetSize;

    private int chunkX;
    private int chunkZ;
    private boolean fullChunk;
    private int bitMask;
    private NBTTagCompound heightmaps;
    private int biomesLength;
    private int[] biomes;
    private int size;
    private byte[] data;
    private int blockEntitiesAmount;
    private NBTTagCompound[] blockEntities;

    public ChunkData(int id, int timestamp, int packetSize, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.packetSize = packetSize;

        DataReader reader = new DataReader(rawData);

        chunkX = reader.readInt();
        chunkZ = reader.readInt();
        fullChunk = reader.readBoolean();
        bitMask = reader.readVarInt();
        heightmaps = reader.readNBT();

        if(fullChunk) {
            biomesLength = reader.readVarInt();
            biomes = reader.readVarIntArray(biomesLength);
        }

        size = reader.readVarInt();
        data = reader.readFollowingBytes(size);
        blockEntitiesAmount = reader.readVarInt();

        blockEntities = new NBTTagCompound[blockEntitiesAmount];
        for(int i = 0; i < blockEntitiesAmount; i++) {
            blockEntities[i] = reader.readNBT();
        }
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(packetSize);
        writer.writeVarInt(packetID);

        writer.writeInt(chunkX);
        writer.writeInt(chunkZ);
        writer.writeBoolean(fullChunk);
        writer.writeVarInt(bitMask);
        writer.writeNBT(heightmaps);

        if(fullChunk) {
            writer.writeVarInt(biomesLength);
            writer.writeVarIntArray(biomes);
        }

        writer.writeVarInt(size);
        writer.writeByteArray(data);
        writer.writeVarInt(blockEntitiesAmount);

        for(NBTTagCompound nbt : blockEntities) {
            writer.writeNBT(nbt);
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

    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public boolean isFullChunk() {
        return fullChunk;
    }

    public void setFullChunk(boolean fullChunk) {
        this.fullChunk = fullChunk;
    }

    public int getBitMask() {
        return bitMask;
    }

    public void setBitMask(int bitMask) {
        this.bitMask = bitMask;
    }

    public NBTTagCompound getHeightmaps() {
        return heightmaps;
    }

    public void setHeightmaps(NBTTagCompound heightmaps) {
        this.heightmaps = heightmaps;
    }

    public int getBiomesLength() {
        return biomesLength;
    }

    public void setBiomesLength(int biomesLength) {
        this.biomesLength = biomesLength;
    }

    public int[] getBiomes() {
        return biomes;
    }

    public void setBiomes(int[] biomes) {
        this.biomes = biomes;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getBlockEntitiesAmount() {
        return blockEntitiesAmount;
    }

    public void setBlockEntitiesAmount(int blockEntitiesAmount) {
        this.blockEntitiesAmount = blockEntitiesAmount;
    }

    public NBTTagCompound[] getBlockEntities() {
        return blockEntities;
    }

    public void setBlockEntities(NBTTagCompound[] blockEntities) {
        this.blockEntities = blockEntities;
    }
}