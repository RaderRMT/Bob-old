package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;

public class NBTTagList extends NBTBase {

    private NBTBase[] value;

    private int tagID;
    private int length;

    public NBTTagList(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        tagID = reader.readByte();
        length = reader.readInt();

        value = new NBTBase[length];

        for(int i = 0; i < length; i++) {
            NBTBase tag;

            switch(tagID) {
                case 1:
                    tag = new NBTTagByte(reader.getFromOffset(false), true);
                    break;
                case 2:
                    tag = new NBTTagShort(reader.getFromOffset(false), true);
                    break;
                case 3:
                    tag = new NBTTagInt(reader.getFromOffset(false), true);
                    break;
                case 4:
                    tag = new NBTTagLong(reader.getFromOffset(false), true);
                    break;
                case 5:
                    tag = new NBTTagFloat(reader.getFromOffset(false), true);
                    break;
                case 6:
                    tag = new NBTTagDouble(reader.getFromOffset(false), true);
                    break;
                case 7:
                    tag = new NBTTagByteArray(reader.getFromOffset(false), true);
                    break;
                case 8:
                    tag = new NBTTagString(reader.getFromOffset(false), true);
                    break;
                case 9:
                    tag = new NBTTagList(reader.getFromOffset(false), true);
                    break;
                case 10:
                    tag = new NBTTagCompound(reader.getFromOffset(false), true);
                    break;
                case 11:
                    tag = new NBTTagIntArray(reader.getFromOffset(false), true);
                    break;
                case 12:
                    tag = new NBTTagLongArray(reader.getFromOffset(false), true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected tag: " + Integer.toHexString(tagID));
            }

            value[i] = tag;
            reader.addOffset(tag.getLength());
        }

        setLength(reader.getOffset());
    }

    public NBTBase[] getValue() {
        return value;
    }

    @Override
    public byte[] toByteArray(boolean fromList) {
        DataWriter writer = new DataWriter();

        if(!fromList) {
            writer.writeByte(0x09);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeByte(this.tagID);
        writer.writeInt(this.length);

        for(NBTBase base : this.value) {
            writer.writeByteArray(base.toByteArray(true));
        }

        return writer.getData();
    }
}