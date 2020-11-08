package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTTagDouble extends NBTBase {

    private double value;

    public NBTTagDouble(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        value = reader.readDouble();

        setLength(reader.getOffset());
    }

    public double getValue() {
        return value;
    }

    @Override
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x06);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeDouble(value);

        return writer.getData();
    }
}