package fr.rader.bob;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DataReader {

    private byte[] data;
    private int offset = 0;

    public DataReader(byte[] data) {
        this.data = data;
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
        return readByte() << 24 | readByte() << 16 | readByte() << 8 | readByte();
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

    /**
     * Return data that was not read previously
     * @param incrementOffset increment the offset
     * @return byte array of unread data
     */
    public byte[] getFromOffset(boolean incrementOffset) {
        byte[] out = new byte[data.length - offset];

        for(int i = 0; i < out.length; i++) {
            out[i] = data[i + offset];
        }

        if(incrementOffset) offset += out.length;

        return out;
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

    public UUID readUUID() {
        return new UUID(readFollowingBytes(16));
    }
}
