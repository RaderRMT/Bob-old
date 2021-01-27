package fr.rader.bob.nbt.tags;

import fr.rader.bob.DataWriter;

public class NBTIntArray extends NBTBase {

    private int[] value;

    public NBTIntArray(String name, int[] value) {
        setId(0x0b);
        setName(name);

        this.value = value;
    }

    public NBTIntArray(int[] value) {
        this.value = value;
    }

    public int[] getValue() {
        return value;
    }

    public void setValue(int[] value) {
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

        writer.writeInt(value.length);
        writer.writeIntArray(value);

        return writer.getData();
    }
}