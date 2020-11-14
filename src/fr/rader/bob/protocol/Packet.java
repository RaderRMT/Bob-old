package fr.rader.bob.protocol;

public interface Packet {

    byte[] writePacket();

    int getLength();

    int getPacketID();

    int getTimestamp();
}
