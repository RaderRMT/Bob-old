package fr.rader.bob.nbt.tags;

import fr.rader.bob.utils.DataReader;
import fr.rader.bob.utils.DataWriter;

import java.io.IOException;

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

    public NBTShort(DataReader reader) {
        try {
            this.value = reader.readShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void writeNBT(DataWriter writer) {
        if(getName() != null) {
            writer.writeByte(getId());
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeShort(value);
    }
}
