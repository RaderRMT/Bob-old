package fr.rader.bob.nbt.tags;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTInt extends NBTBase {

    private int value;

    public NBTInt(String name, int value) {
        setId(0x03);
        setName(name);

        this.value = value;
    }

    public NBTInt(int value) {
        this.value = value;
    }

    public NBTInt(byte[] rawData) {
        this.value = new DataReader(rawData).readInt();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public byte[] toByteArray() {
        DataWriter writer = new DataWriter();

        if(getName() != null) {
            writer.writeByte(getId());
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeInt(value);

        return writer.getData();
    }
}
