package fr.rader.bob.nbt.tags;

import fr.rader.bob.DataWriter;

public class NBTString extends NBTBase {

    private String value;

    public NBTString(String name, String value) {
        setId(0x08);
        setName(name);

        this.value = value;
    }

    public NBTString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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

        writer.writeShort(value.length());
        writer.writeString(value);

        return writer.getData();
    }
}