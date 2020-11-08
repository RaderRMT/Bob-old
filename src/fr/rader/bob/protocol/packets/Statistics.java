package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class Statistics implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private int[][] statistic;

    public Statistics(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        statistic = new int[reader.readVarInt()][3];

        for(int i = 0; i < statistic.length; i++) {
            statistic[i][0] = reader.readVarInt(); // category id
            statistic[i][1] = reader.readVarInt(); // statistic id
            statistic[i][2] = reader.readVarInt(); // value
        }
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeVarInt(statistic.length);

        for(int[] ints : statistic) {
            writer.writeVarInt(ints[0]); // category id
            writer.writeVarInt(ints[1]); // statistic id
            writer.writeVarInt(ints[2]); // value
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

    public int[][] getStatistic() {
        return statistic;
    }

    public void setStatistic(int[][] statistic) {
        this.statistic = statistic;
    }
}