package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;

import java.util.Arrays;

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
    public String toString() {
        return "Int(\"" + getName() + "\"): " + getValue();
    }
}