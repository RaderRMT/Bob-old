package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTTagLongArray extends NBTBase {

    private long[] value;

    public NBTTagLongArray(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        int len = reader.readInt();
        value = new long[len];

        for(int i = 0; i < len; i++) {
            value[i] = reader.readLong();
        }

        setLength(reader.getOffset());
    }

    public long[] getValue() {
        return value;
    }

    @Override
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x0c);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeInt(value.length);
        writer.writeLongArray(value);

        return writer.getData();
    }
}