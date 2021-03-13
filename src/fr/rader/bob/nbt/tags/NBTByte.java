package fr.rader.bob.nbt.tags;

import fr.rader.bob.utils.DataReader;
import fr.rader.bob.utils.DataWriter;

import java.io.IOException;

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

    public NBTByte(DataReader reader) {
        try {
            this.value = reader.readByte();
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

        writer.writeByte(value);
    }
}
