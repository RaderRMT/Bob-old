package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

import java.util.ArrayList;
import java.util.List;

public class UpdateLight implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private int chunkX;
    private int chunkZ;
    private boolean trustEdges;
    private int skyLightMask;
    private int blockLightMask;
    private int emptySkyLightMask;
    private int emptyBlockLightMask;
    private List<byte[]> skyLightArrays;
    private List<byte[]> blockLightArrays;

    public UpdateLight(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        chunkX = reader.readVarInt();
        chunkZ = reader.readVarInt();
        trustEdges = reader.readBoolean();
        skyLightMask = reader.readVarInt();
        blockLightMask = reader.readVarInt();
        emptySkyLightMask = reader.readVarInt();
        emptyBlockLightMask = reader.readVarInt();

        skyLightArrays = new ArrayList<>();
        for(int i = 0; i < 18; i++) {
            skyLightArrays.add(reader.readFollowingBytes(reader.readVarInt()));
        }

        blockLightArrays = new ArrayList<>();
        for(int i = 0; i < 18; i++) {
            blockLightArrays.add(reader.readFollowingBytes(reader.readVarInt()));
        }
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeVarInt(chunkX);
        writer.writeVarInt(chunkZ);
        writer.writeBoolean(trustEdges);
        writer.writeVarInt(skyLightMask);
        writer.writeVarInt(blockLightMask);
        writer.writeVarInt(emptySkyLightMask);
        writer.writeVarInt(emptyBlockLightMask);

        for(int i = 0; i < 18; i++) {
            writer.writeVarInt(skyLightArrays.get(i).length);
            writer.writeByteArray(skyLightArrays.get(i));
        }

        for(int i = 0; i < 18; i++) {
            writer.writeVarInt(blockLightArrays.get(i).length);
            writer.writeByteArray(blockLightArrays.get(i));
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    public boolean isTrustEdges() {
        return trustEdges;
    }

    public void setTrustEdges(boolean trustEdges) {
        this.trustEdges = trustEdges;
    }

    public int getSkyLightMask() {
        return skyLightMask;
    }

    public void setSkyLightMask(int skyLightMask) {
        this.skyLightMask = skyLightMask;
    }

    public int getBlockLightMask() {
        return blockLightMask;
    }

    public void setBlockLightMask(int blockLightMask) {
        this.blockLightMask = blockLightMask;
    }

    public int getEmptySkyLightMask() {
        return emptySkyLightMask;
    }

    public void setEmptySkyLightMask(int emptySkyLightMask) {
        this.emptySkyLightMask = emptySkyLightMask;
    }

    public int getEmptyBlockLightMask() {
        return emptyBlockLightMask;
    }

    public void setEmptyBlockLightMask(int emptyBlockLightMask) {
        this.emptyBlockLightMask = emptyBlockLightMask;
    }

    public List<byte[]> getSkyLightArrays() {
        return skyLightArrays;
    }

    public void setSkyLightArrays(List<byte[]> skyLightArrays) {
        this.skyLightArrays = skyLightArrays;
    }

    public List<byte[]> getBlockLightArrays() {
        return blockLightArrays;
    }

    public void setBlockLightArrays(List<byte[]> blockLightArrays) {
        this.blockLightArrays = blockLightArrays;
    }
}