package fr.rader.bob.protocol.versions;

import fr.rader.bob.protocol.Packet;

public class Version751 {

    public static Packet getPacket(int id, int timestamp, int size, byte[] data) {
        return Version754.getPacket(id, timestamp, size, data);
    }
}
