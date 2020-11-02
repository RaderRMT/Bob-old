package fr.rader.bob.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.Packet;
import fr.rader.bob.PacketHeader;

public class P5ADeclareRecipes extends Packet {

    private int numRecipes;

    public P5ADeclareRecipes(PacketHeader header, byte[] rawData) {
        super(header, rawData);

        readData(rawData);
    }

    private void readData(byte[] rawData) {
        DataReader reader = new DataReader(rawData);
        reader.startAt(1);

        numRecipes = reader.readVarInt();

        System.out.println(Integer.toHexString(rawData[reader.getOffset()]));
    }
}
