package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;

import java.util.Arrays;

public class NBTTagByteArray extends NBTBase {

    private byte[] value;

    public NBTTagByteArray(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        value = reader.readFollowingBytes(reader.readInt());

        setLength(reader.getOffset());
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ByteArray(\"" + getName() + "\"): " + Arrays.toString(getValue());
    }
}
