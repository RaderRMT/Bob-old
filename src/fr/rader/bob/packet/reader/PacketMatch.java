package fr.rader.bob.packet.reader;

import java.util.ArrayList;

public class PacketMatch extends PacketBase {

    private ArrayList<PacketBase> data;
    private String boundVariable;

    public PacketMatch(String boundVariable) {
        setName("Matches " + boundVariable);
        this.data = new ArrayList<>();
        this.boundVariable = boundVariable;
    }

    public PacketMatch(PacketMatch packetMatch) {
        setName(packetMatch.getName());
        this.data = new ArrayList<>();
        this.boundVariable = packetMatch.getBoundVariable();
    }

    /**
     * Overwrite the current data with a new one
     * @param packetData New packet data
     */
    public void set(ArrayList<PacketBase> packetData) {
        this.data = packetData;
    }

    /**
     * Add a new packet data to the match
     * @param packetData Packet data to add
     */
    public void add(PacketBase packetData) {
        this.data.add(packetData);
    }

    /**
     * Get the packet data based on it's index
     * @param index The match value
     * @return Packet data is data contains the match value<br/>null otherwise
     */
    public PacketBase get(int index) {
        for(PacketBase base : data) {
            if(base.getName().equals(Integer.toString(index)))
                return base;
        }

        return null;
    }

    public String getBoundVariable() {
        return boundVariable;
    }

    public ArrayList<PacketBase> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PacketMatch{" +
                "name='" + getName() + '\'' +
                ", data=" + data +
                ", boundVariable='" + boundVariable + '\'' +
                '}';
    }
}
