package fr.rader.bob.nbt.tags;

import fr.rader.bob.utils.DataReader;
import fr.rader.bob.utils.DataWriter;

public class NBTShort extends NBTBase {

    private int value;

    public NBTShort(String name, int value) {
        setId(0x02);
        setName(name);

        this.value = value;
    }

    public NBTShort(int value) {
        this.value = value;
    }

    public NBTShort(byte[] rawData) {
        this.value = new DataReader(rawData).readShort();
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

        writer.writeShort(value);

        return writer.getData();
    }
}
