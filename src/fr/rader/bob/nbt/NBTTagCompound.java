package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

import java.util.ArrayList;
import java.util.List;

public class NBTTagCompound extends NBTBase {

    private List<NBTBase> value = new ArrayList<>();

    public NBTTagCompound(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        while(true) {
            NBTBase tag;

            int tagID = reader.readByte();
            if(tagID == 0x00) {
                setLength(reader.getOffset());
                return;
            }

            String tagName = reader.readString(reader.readShort());
            reader.removeOffset(tagName.length() + 3);

            switch(tagID) {
                case 1:
                    tag = new NBTTagByte(reader.getFromOffset(false), false);
                    break;
                case 2:
                    tag = new NBTTagShort(reader.getFromOffset(false), false);
                    break;
                case 3:
                    tag = new NBTTagInt(reader.getFromOffset(false), false);
                    break;
                case 4:
                    tag = new NBTTagLong(reader.getFromOffset(false), false);
                    break;
                case 5:
                    tag = new NBTTagFloat(reader.getFromOffset(false), false);
                    break;
                case 6:
                    tag = new NBTTagDouble(reader.getFromOffset(false), false);
                    break;
                case 7:
                    tag = new NBTTagByteArray(reader.getFromOffset(false), false);
                    break;
                case 8:
                    tag = new NBTTagString(reader.getFromOffset(false), false);
                    break;
                case 9:
                    tag = new NBTTagList(reader.getFromOffset(false), false);
                    break;
                case 10:
                    tag = new NBTTagCompound(reader.getFromOffset(false), false);
                    break;
                case 11:
                    tag = new NBTTagIntArray(reader.getFromOffset(false), false);
                    break;
                case 12:
                    tag = new NBTTagLongArray(reader.getFromOffset(false), false);
                    break;
                default:
                    throw new IllegalStateException("Unexpected tag: " + Integer.toHexString(tagID));
            }

            value.add(tag);
            reader.addOffset(tag.getLength());
            setLength(reader.getOffset());
        }
    }

    public List<NBTBase> getValue() {
        return value;
    }

    @Override
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x0a);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        for(NBTBase base : value) {
            writer.writeByteArray(base.toByteArray(false));
        }

        writer.writeByte(0);

        return writer.getData();
    }
}
