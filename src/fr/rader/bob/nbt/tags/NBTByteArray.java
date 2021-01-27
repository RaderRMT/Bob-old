package fr.rader.bob.nbt.tags;

import fr.rader.bob.DataWriter;

public class NBTByteArray extends NBTBase {

    private byte[] value;

    public NBTByteArray(String name, byte[] value) {
        setId(0x07);
        setName(name);

        this.value = value;
    }

    public NBTByteArray(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
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
        writer.writeByteArray(value);

        return writer.getData();
    }
}