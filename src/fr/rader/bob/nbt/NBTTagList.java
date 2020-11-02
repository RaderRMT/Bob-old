package fr.rader.bob.nbt;

import fr.rader.bob.DataReader;

import java.util.Arrays;

public class NBTTagList extends NBTBase {

    private NBTBase[] value;

    public NBTTagList(byte[] rawData, boolean fromList) {
        DataReader reader = new DataReader(rawData);

        if(!fromList) {
            setId(reader.readByte());
            setName(reader.readString(reader.readShort()));
        }

        readData(reader);
    }

    private void readData(DataReader reader) {
        int tagID = reader.readByte();
        int length = reader.readInt();

        value = new NBTBase[length];

        for(int i = 0; i < length; i++) {
            NBTBase tag = null;

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
    public String toString() {
        return "List(\"" + getName() + "\"): " + Arrays.toString(getValue());
    }
}