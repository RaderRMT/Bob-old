package fr.rader.bob.nbt.tags;

import fr.rader.bob.utils.DataReader;
import fr.rader.bob.utils.DataWriter;

import java.util.ArrayList;
import java.util.List;

public class NBTList extends NBTBase {

    private List<NBTBase> values = new ArrayList<>();

    private int tagID;

    public NBTList(String tagName, int tagID) {
        this.tagID = tagID;

        setId(0x09);
        setName(tagName);
    }

    public NBTList(byte[] rawData, boolean hasName) {
        DataReader reader = new DataReader(rawData);

        if(hasName) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    public NBTList(int tagID) {
        this.tagID = tagID;
    }

    private void readData(DataReader reader) {
        tagID = reader.readByte();

        int length = reader.readInt();
        for(int i = 0; i < length; i++) {
            switch(tagID) {
                case 1:
                    addByte(new NBTByte(reader.readByte()));
                    break;
                case 2:
                    addShort(new NBTShort(reader.readShort()));
                    break;
                case 3:
                    addInt(new NBTInt(reader.readInt()));
                    break;
                case 4:
                    addLong(new NBTLong(reader.readLong()));
                    break;
                case 5:
                    addFloat(new NBTFloat(reader.readFloat()));
                    break;
                case 6:
                    addDouble(new NBTDouble(reader.readDouble()));
                    break;
                case 7:
                    addByteArray(new NBTByteArray(reader.readFollowingBytes(reader.readInt())));
                    break;
                case 8:
                    addString(new NBTString(reader.readString(reader.readShort())));
                    break;
                case 9:
                    addList(new NBTList(reader.getFromOffset(false), false));
                    break;
                case 10:
                    addCompound(new NBTCompound(reader.getFromOffset(false), false));
                    break;
                case 11:
                    addIntArray(new NBTIntArray(reader.readIntArray(reader.readInt())));
                    break;
                case 12:
                    addLongArray(new NBTLongArray(reader.readLongArray(reader.readInt())));
                    break;
            }

            reader.addOffset(values.get(i).getLength());
        }

        setLength(reader.getOffset());
    }

    public NBTBase[] getComponents() {
        return values.toArray(new NBTBase[0]);
    }

    @Override
    public byte[] toByteArray() {
        DataWriter writer = new DataWriter();

        if(getName() != null) {
            writer.writeByte(0x09);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeByte(this.tagID);
        writer.writeInt(values.size());

        for(NBTBase base : values) {
            writer.writeByteArray(base.toByteArray());
        }

        return writer.getData();
    }

    public NBTList addComponent(NBTBase base) {
        values.add(base);
        return this;
    }

    public NBTList addByte(NBTByte component) {
        values.add(component);
        return this;
    }

    public void removeByte(NBTByte component) {
        values.removeIf(base -> base.getAsByte() == component.getAsByte());
    }

    public NBTList addShort(NBTShort component) {
        values.add(component);
        return this;
    }

    public void removeShort(NBTShort component) {
        values.removeIf(base -> base.getAsShort() == component.getAsShort());
    }

    public NBTList addInt(NBTInt component) {
        values.add(component);
        return this;
    }

    public void removeInt(NBTInt component) {
        values.removeIf(base -> base.getAsInt() == component.getAsInt());
    }

    public NBTList addLong(NBTLong component) {
        values.add(component);
        return this;
    }

    public void removeLong(NBTLong component) {
        values.removeIf(base -> base.getAsLong() == component.getAsLong());
    }

    public NBTList addFloat(NBTFloat component) {
        values.add(component);
        return this;
    }

    public void removeFloat(NBTFloat component) {
        values.removeIf(base -> base.getAsFloat() == component.getAsFloat());
    }

    public NBTList addDouble(NBTDouble component) {
        values.add(component);
        return this;
    }

    public void removeDouble(NBTDouble component) {
        values.removeIf(base -> base.getAsDouble() == component.getAsDouble());
    }

    public NBTList addString(NBTString component) {
        values.add(component);
        return this;
    }

    public void removeString(NBTString component) {
        values.removeIf(base -> base.getAsString().equals(component.getAsString()));
    }

    public NBTList addByteArray(NBTByteArray component) {
        values.add(component);
        return this;
    }

    public void removeByteArray(NBTByteArray component) {
        values.removeIf(base -> base.getAsByteArray() == component.getAsByteArray());
    }

    public NBTList addIntArray(NBTIntArray component) {
        values.add(component);
        return this;
    }

    public void removeIntArray(NBTIntArray component) {
        values.removeIf(base -> base.getAsIntArray() == component.getAsIntArray());
    }

    public NBTList addLongArray(NBTLongArray component) {
        values.add(component);
        return this;
    }

    public void removeLongArray(NBTLongArray component) {
        values.removeIf(base -> base.getAsLongArray() == component.getAsLongArray());
    }

    public NBTList addCompound(NBTCompound component) {
        values.add(component);
        return this;
    }

    public void removeCompound(NBTCompound component) {
        values.removeIf(base -> base.getAsCompound() == component.getAsCompound());
    }

    public NBTList addList(NBTList component) {
        values.add(component);
        return this;
    }

    public void removeList(NBTList component) {
        values.removeIf(base -> base.getAsList() == component.getAsList());
    }

    public NBTList addByte(int value) {
        values.add(new NBTByte(value));
        return this;
    }

    public void removeByte(int value) {
        for(NBTBase base : values) {
            if(base instanceof NBTByte) {
                if(base.getAsByte() == value) {
                    values.remove(base);
                    return;
                }
            }
        }
    }

    public NBTList addShort(int value) {
        values.add(new NBTShort(value));
        return this;
    }

    public void removeShort(int value) {
        for(NBTBase base : values) {
            if(base instanceof NBTShort) {
                if(base.getAsShort() == value) {
                    values.remove(base);
                    return;
                }
            }
        }
    }

    public NBTList addInt(int value) {
        values.add(new NBTInt(value));
        return this;
    }

    public void removeInt(int value) {
        for(NBTBase base : values) {
            if(base instanceof NBTInt) {
                if(base.getAsInt() == value) {
                    values.remove(base);
                    return;
                }
            }
        }
    }

    public NBTList addLong(long value) {
        values.add(new NBTLong(value));
        return this;
    }

    public void removeLong(long value) {
        for(NBTBase base : values) {
            if(base instanceof NBTLong) {
                if(base.getAsLong() == value) {
                    values.remove(base);
                    return;
                }
            }
        }
    }

    public NBTList addFloat(float value) {
        values.add(new NBTFloat(value));
        return this;
    }

    public void removeFloat(float value) {
        for(NBTBase base : values) {
            if(base instanceof NBTFloat) {
                if(base.getAsFloat() == value) {
                    values.remove(base);
                    return;
                }
            }
        }
    }

    public NBTList addDouble(double value) {
        values.add(new NBTDouble(value));
        return this;
    }

    public void removeDouble(double value) {
        for(NBTBase base : values) {
            if(base instanceof NBTDouble) {
                if(base.getAsDouble() == value) {
                    values.remove(base);
                    return;
                }
            }
        }
    }

    public NBTList addByteArray(byte[] value) {
        values.add(new NBTByteArray(value));
        return this;
    }

    public void removeByteArray(byte[] value) {
        for(NBTBase base : values) {
            if(base instanceof NBTByteArray) {
                if(base.getAsByteArray() == value) {
                    values.remove(base);
                    return;
                }
            }
        }
    }

    public NBTList addString(String value) {
        values.add(new NBTString(value));
        return this;
    }

    public void removeString(String value) {
        for(NBTBase base : values) {
            if(base instanceof NBTString) {
                if(base.getAsString().equals(value)) {
                    values.remove(base);
                    return;
                }
            }
        }
    }

    public NBTList addIntArray(int[] value) {
        values.add(new NBTIntArray(value));
        return this;
    }

    public void removeIntArray(int[] value) {
        for(NBTBase base : values) {
            if(base instanceof NBTIntArray) {
                if(base.getAsIntArray() == value) {
                    values.remove(base);
                    return;
                }
            }
        }
    }

    public NBTList addLongArray(long[] value) {
        values.add(new NBTLongArray(value));
        return this;
    }

    public void removeLongArray(long[] value) {
        for(NBTBase base : values) {
            if(base instanceof NBTLongArray) {
                if(base.getAsLongArray() == value) {
                    values.remove(base);
                    return;
                }
            }
        }
    }

    public int getTagID() {
        return tagID;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public void removeComponentAt(int index) {
        values.remove(index);
    }
}
