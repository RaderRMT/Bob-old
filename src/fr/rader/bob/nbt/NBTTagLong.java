package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTTagLong extends NBTBase {

    private long value;

    public NBTTagLong(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        value = reader.readLong();

        setLength(reader.getOffset());
    }

    public long getValue() {
        return value;
    }

    @Override
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x04);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeLong(value);

        return writer.getData();
    }
}
