package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class MultiBlockChange implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private long chunkSectionPosition;
    private boolean invertTrustEdges;
    private int blocksArraySize;
    private byte[] blocksData;

    public MultiBlockChange(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        chunkSectionPosition = reader.readLong();
        invertTrustEdges = reader.readBoolean();
        blocksArraySize = reader.readVarInt();
        blocksData = reader.readFollowingBytes(blocksArraySize);
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeLong(chunkSectionPosition);
        writer.writeBoolean(invertTrustEdges);
        writer.writeVarInt(blocksArraySize);
        writer.writeByteArray(blocksData);

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

    public long getChunkSectionPosition() {
        return chunkSectionPosition;
    }

    public void setChunkSectionPosition(long chunkSectionPosition) {
        this.chunkSectionPosition = chunkSectionPosition;
    }

    public boolean isInvertTrustEdges() {
        return invertTrustEdges;
    }

    public void setInvertTrustEdges(boolean invertTrustEdges) {
        this.invertTrustEdges = invertTrustEdges;
    }

    public int getBlocksArraySize() {
        return blocksArraySize;
    }

    public void setBlocksArraySize(int blocksArraySize) {
        this.blocksArraySize = blocksArraySize;
    }

    public byte[] getBlocksData() {
        return blocksData;
    }

    public void setBlocksData(byte[] blocksData) {
        this.blocksData = blocksData;
    }
}