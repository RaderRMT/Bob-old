package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTTagByte extends NBTBase {

    private int value;

    public NBTTagByte(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        value = reader.readByte();

        setLength(reader.getOffset());
    }

    public int getValue() {
        return value;
    }

    @Override
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x01);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeByte(value);

        return writer.getData();
    }
}
