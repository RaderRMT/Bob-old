package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class UpdateHealth implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private float health;
    private int food;
    private float foodSaturation;

    public UpdateHealth(int id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        health = reader.readFloat();
        food = reader.readVarInt();
        foodSaturation = reader.readFloat();
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeFloat(health);
        writer.writeVarInt(food);
        writer.writeFloat(foodSaturation);

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

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public float getFoodSaturation() {
        return foodSaturation;
    }

    public void setFoodSaturation(float foodSaturation) {
        this.foodSaturation = foodSaturation;
    }
}