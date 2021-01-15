package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTByte extends NBTBase {

    private int value;

    public NBTByte(String name, int value) {
        setId(0x01);
        setName(name);

        this.value = value;
    }

    public NBTByte(int value) {
        this.value = value;
    }

    public NBTByte(byte[] rawData) {
        this.value = new DataReader(rawData).readByte();
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

        writer.writeByte(value);

        return writer.getData();
    }
}
