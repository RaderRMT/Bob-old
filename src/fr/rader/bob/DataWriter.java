package fr.rader.bob;

import fr.rader.bob.nbt.NBTTagCompound;
import fr.rader.bob.types.Equipment;
import fr.rader.bob.types.Position;
import fr.rader.bob.types.Slot;
import fr.rader.bob.types.UUID;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DataWriter {

    private List<Byte> data;

    public DataWriter() {
        this.data = new ArrayList<>();
    }

    public void writeByte(int value) {
        data.add((byte) value);
    }

    public void writeShort(int value) {
        writeByte(value >>> 8);
        writeByte(value & 0xff);
    }

    public void writeInt(int value) {
        writeShort(value >>> 16);
        writeShort(value & 0xffff);
    }

    public void writeLong(long value) {
        writeInt((int) (value >>> 32));
        writeInt((int) value);
    }

    public void writeFloat(float value) {
        writeByteArray(ByteBuffer.allocate(4).putFloat(value).array());
    }

    public void writeDouble(double value) {
        writeByteArray(ByteBuffer.allocate(8).putDouble(value).array());
    }

    public void writeBoolean(boolean value) {
        writeByte(value ? 0x01 : 0x00);
    }

    public void writeVarInt(int value) {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;

            if(value != 0) {
                temp |= 0b10000000;
            }

            writeByte(temp);
        } while (value != 0);
    }

    public void writeVarLong(long value) {
        do {
            byte temp = (byte)(value & 0b01111111);
            value >>>= 7;

            if(value != 0) {
                temp |= 0b10000000;
            }

            writeByte(temp);
        } while (value != 0);
    }

    public void writeString(String value) {
        writeByteArray(value.getBytes(StandardCharsets.UTF_8));
    }

    public void writeAsciiChar(char value) {
        writeByte(value);
    }

    public void writeByteArray(byte[] values) {
        for(byte value : values) {
            writeByte(value);
        }
    }

    public void writeUUID(UUID uuid) {
        writeByteArray(uuid.getRawUUID());
    }

    public void writeNBT(NBTTagCompound nbt) {
        if(nbt == null) writeByte(0x00);
        else writeByteArray(nbt.toByteArray(false));
    }

    public byte[] getData() {
        byte[] out = new byte[data.size()];

        for(int i = 0; i < out.length; i++) {
            out[i] = data.get(i);
        }

        return out;
    }

    public void writeIntArray(int[] values) {
        for(int value : values) {
            writeInt(value);
        }
    }

    public void writeLongArray(long[] values) {
        for(long value : values) {
            writeLong(value);
        }
    }

    public void writePosition(Position position) {
        writeLong(((long) (position.getX() & 0x3FFFFFF) << 38) | ((long) (position.getZ() & 0x3FFFFFF) << 12) | (position.getY() & 0xFFF));
    }

    public void writeChat(String value) {
        writeVarInt(value.length());
        writeString(value);
    }

    public void writeSlot(Slot slot) {
        writeBoolean(slot.isPresent());

        if(slot.isPresent()) {
            writeVarInt(slot.getItemID());
            writeByte(slot.getItemCount());
            writeNBT(slot.getNbt());
        }
    }

    public void writeEquipment(Equipment equipment) {
        writeByte(equipment.getSlot());
        writeSlot(equipment.getItem());
    }

    public void writeIdentifier(String identifier) {
        writeChat(identifier);
    }

    public void writeVarIntArray(int[] values) {
        for(int value : values)
            writeVarInt(value);
    }

    public void writeFloatArray(float[] values) {
        for(float value : values)
            writeFloat(value);
    }

    public void writeVarLongArray(long[] values) {
        for(long value : values)
            writeVarLong(value);
    }
}
