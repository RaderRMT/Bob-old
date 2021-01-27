package fr.rader.bob.nbt.tags;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTDouble extends NBTBase {

    private double value;

    public NBTDouble(String name, double value) {
        setId(0x06);
        setName(name);

        this.value = value;
    }

    public NBTDouble(double value) {
        this.value = value;
    }

    public NBTDouble(byte[] rawData) {
        this.value = new DataReader(rawData).readDouble();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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

        writer.writeDouble(value);

        return writer.getData();
    }
}
