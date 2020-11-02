package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;

public class NBTTagFloat extends NBTBase {

    private float value;

    public NBTTagFloat(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        value = reader.readFloat();

        setLength(reader.getOffset());
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Float(\"" + getName() + "\"): " + getValue();
    }
}
