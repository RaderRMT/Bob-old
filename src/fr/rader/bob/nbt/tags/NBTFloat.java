package fr.rader.bob.nbt.tags;

import fr.rader.bob.utils.DataReader;
import fr.rader.bob.utils.DataWriter;

import java.io.IOException;

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

    public NBTFloat(DataReader reader) {
        try {
            this.value = reader.readFloat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void writeNBT(DataWriter writer) {
        if(getName() != null) {
            writer.writeByte(getId());
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeFloat(value);
    }
}
