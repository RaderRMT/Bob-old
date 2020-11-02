package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;

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
    public String toString() {
        return "Long(\"" + getName() + "\"): " + getValue();
    }
}
