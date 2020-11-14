package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class NamedSoundEffect implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private String soundName;
    private int soundCategory;
    private int effectPositionX;
    private int effectPositionY;
    private int effectPositionZ;
    private float volume;
    private float pitch;

    public NamedSoundEffect(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        soundName = reader.readIdentifier();
        soundCategory = reader.readVarInt();
        effectPositionX = reader.readInt();
        effectPositionY = reader.readInt();
        effectPositionZ = reader.readInt();
        volume = reader.readFloat();
        pitch = reader.readFloat();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeIdentifier(soundName);
        writer.writeVarInt(soundCategory);
        writer.writeInt(effectPositionX);
        writer.writeInt(effectPositionY);
        writer.writeInt(effectPositionZ);
        writer.writeFloat(volume);
        writer.writeFloat(pitch);

        return writer.getData();
    }

    @Override
    public int getLength() {
        return writePacket().length;
    }

    @Override
    public int getPacketID() {
        return packetID;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public int getSoundCategory() {
        return soundCategory;
    }

    public void setSoundCategory(int soundCategory) {
        this.soundCategory = soundCategory;
    }

    public int getEffectPositionX() {
        return effectPositionX;
    }

    public void setEffectPositionX(int effectPositionX) {
        this.effectPositionX = effectPositionX;
    }

    public int getEffectPositionY() {
        return effectPositionY;
    }

    public void setEffectPositionY(int effectPositionY) {
        this.effectPositionY = effectPositionY;
    }

    public int getEffectPositionZ() {
        return effectPositionZ;
    }

    public void setEffectPositionZ(int effectPositionZ) {
        this.effectPositionZ = effectPositionZ;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}