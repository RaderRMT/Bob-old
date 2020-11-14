package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class Explosion implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private float x;
    private float y;
    private float z;
    private float strength;
    private int recordCount;
    private int[][] records;
    private float playerMotionX;
    private float playerMotionY;
    private float playerMotionZ;

    public Explosion(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        x = reader.readFloat();
        y = reader.readFloat();
        z = reader.readFloat();
        strength = reader.readFloat();

        recordCount = reader.readInt();
        records = new int[recordCount][3];
        for(int i = 0; i < recordCount; i++) {
            records[i][0] = reader.readByte();
            records[i][1] = reader.readByte();
            records[i][2] = reader.readByte();
        }

        playerMotionX = reader.readFloat();
        playerMotionY = reader.readFloat();
        playerMotionZ = reader.readFloat();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeFloat(x);
        writer.writeFloat(y);
        writer.writeFloat(z);
        writer.writeFloat(strength);
        writer.writeInt(recordCount);

        for(int[] values : records) {
            writer.writeByte(values[0]);
            writer.writeByte(values[1]);
            writer.writeByte(values[2]);
        }

        writer.writeFloat(playerMotionX);
        writer.writeFloat(playerMotionY);
        writer.writeFloat(playerMotionZ);

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int[][] getRecords() {
        return records;
    }

    public void setRecords(int[][] records) {
        this.records = records;
    }

    public float getPlayerMotionX() {
        return playerMotionX;
    }

    public void setPlayerMotionX(float playerMotionX) {
        this.playerMotionX = playerMotionX;
    }

    public float getPlayerMotionY() {
        return playerMotionY;
    }

    public void setPlayerMotionY(float playerMotionY) {
        this.playerMotionY = playerMotionY;
    }

    public float getPlayerMotionZ() {
        return playerMotionZ;
    }

    public void setPlayerMotionZ(float playerMotionZ) {
        this.playerMotionZ = playerMotionZ;
    }
}