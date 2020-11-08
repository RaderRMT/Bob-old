package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTTagInt extends NBTBase {

    private int value;

    public NBTTagInt(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        value = reader.readInt();

        setLength(reader.getOffset());
    }

    public int getValue() {
        return value;
    }

    @Override
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x03);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeInt(value);

        return writer.getData();
    }
}