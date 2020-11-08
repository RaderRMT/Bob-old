package fr.rader.bob.protocol;

public interface Packet {

    byte[] writePacket();

    int getLength();

    byte getPacketID();

    int getTimestamp();
}
