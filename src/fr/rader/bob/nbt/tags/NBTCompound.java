package fr.rader.bob.nbt.tags;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

import java.util.ArrayList;
import java.util.List;

public class NBTCompound extends NBTBase {

    private List<NBTBase> components = new ArrayList<>();
    private List<String> names = new ArrayList<>();

    public NBTCompound(String tagName) {
        setId(0x0a);
        setName(tagName);
    }

    public NBTCompound() {
        setId(0x0a);
    }

    public NBTCompound(byte[] rawData, boolean hasName) {
        DataReader reader = new DataReader(rawData);

        if(hasName) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        while(true) {
            int tagID = reader.readByte();
            if(tagID == 0x00) {
                setLength(reader.getOffset());
                return;
            }

            String tagName = reader.readString(reader.readShort());

            switch(tagID) {
                case 1:
                    addByte(tagName, reader.readByte());
                    break;
                case 2:
                    addShort(tagName, reader.readShort());
                    break;
                case 3:
                    addInt(tagName, reader.readInt());
                    break;
                case 4:
                    addLong(tagName, reader.readLong());
                    break;
                case 5:
                    addFloat(tagName, reader.readFloat());
                    break;
                case 6:
                    addDouble(tagName, reader.readDouble());
                    break;
                case 7:
                    addByteArray(tagName, reader.readFollowingBytes(reader.readInt()));
                    break;
                case 8:
                    addString(tagName, reader.readString(reader.readShort()));
                    break;
                case 9:
                    reader.removeOffset(tagName.length() + 3);
                    addList(tagName, new NBTList(reader.getFromOffset(false), true));
                    break;
                case 10:
                    reader.removeOffset(tagName.length() + 3);
                    addCompound(tagName, new NBTCompound(reader.getFromOffset(false), true));
                    break;
                case 11:
                    addIntArray(tagName, reader.readIntArray(reader.readInt()));
                    break;
                case 12:
                    addLongArray(tagName, reader.readLongArray(reader.readInt()));
                    break;
                default:
                    throw new IllegalStateException("Unexpected tag: " + Integer.toHexString(tagID));
            }
            
            reader.addOffset(components.get(components.size() - 1).getLength());
            setLength(reader.getOffset());
        }
    }

    public NBTBase getComponent(String name) {
        return components.get(names.indexOf(name));
    }

    public NBTBase[] getComponents() {
        return components.toArray(new NBTBase[0]);
    }

    public boolean contains(String componentName) {
        return names.contains(componentName);
    }

    @Override
    public byte[] toByteArray() {
        DataWriter writer = new DataWriter();

        if(getName() != null) {
            writer.writeByte(getId());
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        for(NBTBase component : components) {
            writer.writeByteArray(component.toByteArray());
        }

        writer.writeByte(0);

        return writer.getData();
    }

    public NBTCompound addComponent(NBTBase component) {
        components.add(component);
        names.add(component.getName());
        return this;
    }

    public NBTCompound addByte(NBTByte element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addShort(NBTShort element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addInt(NBTInt element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addLong(NBTLong element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addFloat(NBTFloat element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addDouble(NBTDouble element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addString(NBTString element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addByteArray(NBTByteArray element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addIntArray(NBTIntArray element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addLongArray(NBTLongArray element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addCompound(NBTCompound element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addList(NBTList element) {
        components.add(element);
        names.add(element.getName());
        return this;
    }

    public NBTCompound addByte(String name, int value) {
        components.add(new NBTByte(name, value));
        names.add(name);
        return this;
    }

    public NBTCompound addShort(String name, int value) {
        components.add(new NBTShort(name, value));
        names.add(name);
        return this;
    }

    public NBTCompound addInt(String name, int value) {
        components.add(new NBTInt(name, value));
        names.add(name);
        return this;
    }

    public NBTCompound addLong(String name, long value) {
        components.add(new NBTLong(name, value));
        names.add(name);
        return this;
    }

    public NBTCompound addFloat(String name, float value) {
        components.add(new NBTFloat(name, value));
        names.add(name);
        return this;
    }

    public NBTCompound addDouble(String name, double value) {
        components.add(new NBTDouble(name, value));
        names.add(name);
        return this;
    }

    public NBTCompound addByteArray(String name, byte[] value) {
        components.add(new NBTByteArray(name, value));
        names.add(name);
        return this;
    }

    public NBTCompound addString(String name, String value) {
        components.add(new NBTString(name, value));
        names.add(name);
        return this;
    }

    public NBTCompound addList(String name, NBTList value) {
        if(!name.equals(value.getName())) throw new IllegalArgumentException("Both names should be equal!");
        components.add(value);
        names.add(value.getName());
        return this;
    }

    public NBTCompound addCompound(String name, NBTCompound value) {
        if(!name.equals(value.getName())) throw new IllegalArgumentException("Both names should be equal!");
        components.add(value);
        names.add(value.getName());
        return this;
    }

    public NBTCompound addIntArray(String name, int[] value) {
        components.add(new NBTIntArray(name, value));
        names.add(name);
        return this;
    }

    public NBTCompound addLongArray(String name, long[] value) {
        components.add(new NBTLongArray(name, value));
        names.add(name);
        return this;
    }

    public void removeComponentAt(int index) {
        components.remove(index);
        names.remove(index);
    }
}
