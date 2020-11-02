package fr.rader.bob.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.Packet;
import fr.rader.bob.PacketHeader;

public class P3FHeldItemChange extends Packet {

    private int slot;

    public P3FHeldItemChange(PacketHeader header, byte[] rawData) {
        super(header, rawData);

        readData(rawData);
    }

    private void readData(byte[] rawData) {
        DataReader reader = new DataReader(rawData);
        reader.startAt(1);

        slot = reader.readByte();
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
