package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTFloat extends NBTBase {

    private float value;

    public NBTFloat(String name, float value) {
        setId(0x05);
        setName(name);

        this.value = value;
    }

    public NBTFloat(float value) {
        this.value = value;
    }

    public NBTFloat(byte[] rawData) {
        this.value = new DataReader(rawData).readFloat();
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
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

        writer.writeFloat(value);

        return writer.getData();
    }
}
