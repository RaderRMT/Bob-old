package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class CombatEvent implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private int event;
    private int duration; // event == 1
    private int entityID; // event == 1 && 2
    private int playerID; // event == 2
    private String message; // event == 2

    public CombatEvent(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        event = reader.readVarInt();
        switch(event) {
            case 1:
                duration = reader.readVarInt();
                entityID = reader.readInt();
                break;
            case 2:
                playerID = reader.readVarInt();
                entityID = reader.readInt();
                message = reader.readChat();
                break;
        }
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeVarInt(event);
        switch(event) {
            case 1:
                writer.writeVarInt(duration);
                writer.writeInt(entityID);
                break;
            case 2:
                writer.writeVarInt(playerID);
                writer.writeInt(entityID);
                writer.writeChat(message);
                break;
        }

        return writer.getData();
    }

    @Override
    public int getLength() {
        return writePacket().length;
    }

    @Override
    public byte getPacketID() {
        return packetID;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}