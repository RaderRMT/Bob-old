package fr.rader.bob.packet.reader;

public class PacketBase {

    private String name;

    public String getName() {
        return name;
    }

    public PacketData getAsPacketData() {
        return (PacketData) this;
    }

    public PacketVariable getAsPacketVariable() {
        return (PacketVariable) this;
    }

    public PacketMatchData getAsPacketMatchData() {
        return (PacketMatchData) this;
    }

    public void setName(String name) {
        this.name = name;
    }
}
