package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class EntitySoundEffect implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int soundID;
    private int soundCategory;
    private int entityID;
    private float volume;
    private float pitch;

    public EntitySoundEffect(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        soundID = reader.readVarInt();
        soundCategory = reader.readVarInt();
        entityID = reader.readVarInt();
        volume = reader.readFloat();
        pitch = reader.readFloat();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeVarInt(soundID);
        writer.writeVarInt(soundCategory);
        writer.writeVarInt(entityID);
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

    public int getSoundID() {
        return soundID;
    }

    public void setSoundID(int soundID) {
        this.soundID = soundID;
    }

    public int getSoundCategory() {
        return soundCategory;
    }

    public void setSoundCategory(int soundCategory) {
        this.soundCategory = soundCategory;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
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