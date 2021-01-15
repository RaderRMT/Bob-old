package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTLong extends NBTBase {

    private long value;

    public NBTLong(String name, long value) {
        setId(0x04);
        setName(name);

        this.value = value;
    }

    public NBTLong(long value) {
        this.value = value;
    }

    public NBTLong(byte[] rawData) {
        this.value = new DataReader(rawData).readLong();
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
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

        writer.writeLong(value);

        return writer.getData();
    }
}
