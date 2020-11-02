package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;

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
    public String toString() {
        return "Short(\"" + getName() + "\"): " + getValue();
    }
}