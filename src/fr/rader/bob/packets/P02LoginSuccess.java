package fr.rader.bob.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.Packet;
import fr.rader.bob.PacketHeader;
import fr.rader.bob.UUID;

public class P02LoginSuccess extends Packet {

    private UUID uuid;
    private String username;

    public P02LoginSuccess(PacketHeader header, byte[] rawData) {
        super(header, rawData);

        readData(rawData);
    }

    private void readData(byte[] rawData) {
        DataReader reader = new DataReader(rawData);
        reader.startAt(1);

        uuid = reader.readUUID();
        username = reader.readString(reader.readByte());
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
