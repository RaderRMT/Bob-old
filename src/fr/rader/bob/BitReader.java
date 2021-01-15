package fr.rader.bob;

public class BitReader {

    private byte bitOffset = 7;
    private int byteOffset = 0;

    private byte[] rawData;

    public BitReader(byte[] data) {
        this.rawData = data;
    }

    public byte readBit() {
        byte out = (byte) ((rawData[byteOffset] >> bitOffset) & 1);

        bitOffset--;
        if(bitOffset < 0) {
            bitOffset = 7;
            byteOffset++;
        }

        return out;
    }

    public byte readBits(int size) {
        byte out = 0;

        for(int i = size; i > 0; i--) {
            out |= readBit() << i - 1;
        }

        return out;
    }

    public int getDataLength() {
        return rawData.length;
    }
}
