package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.Position;
import fr.rader.bob.nbt.NBTTagCompound;
import fr.rader.bob.protocol.Packet;

public class BlockEntityData implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private Position location;
    private int action;
    private NBTTagCompound data;

    public BlockEntityData(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        location = reader.readPosition();
        action = reader.readByte();
        data = reader.readNBT();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writePosition(location);
        writer.writeByte(action);
        writer.writeNBT(data);

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

    public Position getLocation() {
        return location;
    }

    public void setLocation(Position location) {
        this.location = location;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public NBTTagCompound getData() {
        return data;
    }

    public void setData(NBTTagCompound data) {
        this.data = data;
    }
}