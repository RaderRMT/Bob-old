package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

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
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x08);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeShort(value.length());
        writer.writeString(value);

        return writer.getData();
    }
}