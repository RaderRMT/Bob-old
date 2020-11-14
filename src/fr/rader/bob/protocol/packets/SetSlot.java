package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.types.Slot;
import fr.rader.bob.protocol.Packet;

public class SetSlot implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int windowID;
    private int slot;
    private Slot slotData;

    public SetSlot(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        windowID = reader.readByte();
        slot = reader.readShort();
        slotData = reader.readSlot();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeByte(windowID);
        writer.writeShort(slot);
        writer.writeSlot(slotData);

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

    public int getWindowID() {
        return windowID;
    }

    public void setWindowID(int windowID) {
        this.windowID = windowID;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Slot getSlotData() {
        return slotData;
    }

    public void setSlotData(Slot slotData) {
        this.slotData = slotData;
    }
}