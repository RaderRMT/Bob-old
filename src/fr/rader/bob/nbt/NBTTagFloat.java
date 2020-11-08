package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

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
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x05);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeFloat(value);

        return writer.getData();
    }
}
