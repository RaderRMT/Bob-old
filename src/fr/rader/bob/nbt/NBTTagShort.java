package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTTagShort extends NBTBase {

    private int value;

    public NBTTagShort(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        value = reader.readShort();

        setLength(reader.getOffset());
    }

    public int getValue() {
        return value;
    }

    @Override
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x02);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeShort(value);

        return writer.getData();
    }
}