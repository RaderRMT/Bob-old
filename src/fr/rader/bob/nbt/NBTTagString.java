package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;

public class NBTTagString extends NBTBase {

    private String value;

    public NBTTagString(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        value = reader.readString(reader.readShort());

        setLength(reader.getOffset());
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "String(\"" + getName() + "\"): \"" + getValue() + "\"";
    }
}