package fr.rader.bob.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.Packet;
import fr.rader.bob.PacketHeader;

public class P1CUnloadChunk extends Packet {

    private int chunkX;
    private int chunkY;

    public P1CUnloadChunk(PacketHeader header, byte[] rawData) {
        super(header, rawData);

        readData(rawData);
    }

    private void readData(byte[] rawData) {
        DataReader reader = new DataReader(rawData);
        reader.startAt(1);

        chunkX = reader.readInt();
        chunkY = reader.readInt();
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public void setChunkY(int chunkY) {
        this.chunkY = chunkY;
    }
}
