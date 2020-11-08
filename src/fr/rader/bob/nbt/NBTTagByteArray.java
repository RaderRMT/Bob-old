package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

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
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x07);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeInt(value.length);
        writer.writeByteArray(value);

        return writer.getData();
    }
}
