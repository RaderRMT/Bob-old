package fr.rader.bob;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.rader.bob.nbt.NBTCompound;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.UUID;

public class DataReader {

    private byte[] data;
    private int offset = 0;

    public DataReader(byte[] data) {
        this.data = data;
    }

    public DataReader(File file) {
        try {
            this.data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataReader(byte[] data, int startAt) {
        this.data = data;
        this.offset = startAt;
    }

    public void startAt(int startByte) {
        offset = startByte;
    }

    // 1 byte
    public int readByte() {
        int out = data[offset] & 0xff;
        offset++;
        return out;
    }

    // 2 bytes
    public int readShort() {
        return (readByte() << 8 | readByte()) & 0xffff;
    }

    // 4 bytes
    public int readInt() {
        return readShort() << 16 | readShort();
    }

    // 8 bytes
    public long readLong() {
        return (long) (readInt()) << 32 | readInt();
    }

    // 4 bytes
    public float readFloat() {
        return ByteBuffer.wrap(readFollowingBytes(4)).order(ByteOrder.BIG_ENDIAN).getFloat();
    }

    // 8 bytes
    public double readDouble() {
        return ByteBuffer.wrap(readFollowingBytes(8)).order(ByteOrder.BIG_ENDIAN).getDouble();
    }

    // 1 byte
    public boolean readBoolean() {
        return (readByte() & 0x01) == 1;
    }

    // 1 byte
    public char readAsciiChar() {
        return (char) readByte();
    }

    public int readVarInt() {
        int numRead = 0;
        int result = 0;
        int read;

        do {
            read = readByte();
            int value = (read & 0x7f);
            result |= value << (7 * numRead);

            numRead++;
            if(numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0x80) != 0);

        return result;
    }

    public long readVarLong() {
        int numRead = 0;
        long result = 0;
        int read;
        do {
            read = readByte();
            long value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if(numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public String readString(int size) {
        String out = "";

        while(size != 0) {
            out += readAsciiChar();
            size--;
        }

        return out;
    }

    public byte[] readFollowingBytes(int length) {
        byte[] out = new byte[length];

        for(int i = 0; i < length; i++) {
            out[i] = data[i + offset];
        }

        offset += length;

        return out;
    }

    public byte[] getFromOffset(boolean incrementOffset) {
        byte[] out = new byte[data.length - offset];

        for(int i = 0; i < out.length; i++) {
            out[i] = data[i + offset];
        }

        if(incrementOffset) offset += out.length;

        return out;
    }

    public NBTCompound readNBT() {
        if(readByte() == 0x00) return null;
        else removeOffset(1);

        NBTCompound out = new NBTCompound(getFromOffset(false), true);
        addOffset(out.getLength());
        return out;
    }

    public String readChat() {
        return new Gson().fromJson(readString(readVarInt()), JsonObject.class).toString();
    }

    public int getOffset() {
        return this.offset;
    }

    public void addOffset(int i) {
        this.offset += i;
    }

    public void removeOffset(int i) {
        this.offset -= i;
    }

    public int getDataLength() {
        return this.data.length;
    }

    public String readIdentifier() {
        return readString(readByte());
    }

    public int[] readVarIntArray(int size) {
        int[] out = new int[size];

        for(int i = 0; i < size; i++) {
            out[i] = readVarInt();
        }

        return out;
    }

    public long[] readVarLongArray(int size) {
        long[] out = new long[size];

        for(int i = 0; i < size; i++) {
            out[i] = readVarLong();
        }

        return out;
    }

    public int[] readIntArray(int size) {
        int[] out = new int[size];

        for(int i = 0; i < size; i++) {
            out[i] = readInt();
        }

        return out;
    }

    public long[] readLongArray(int size) {
        long[] out = new long[size];

        for(int i = 0; i < size; i++) {
            out[i] = readLong();
        }

        return out;
    }

    public UUID readUUID() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(readFollowingBytes(16));
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
