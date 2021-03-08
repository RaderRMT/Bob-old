package fr.rader.bob.packet.reader;

import java.util.ArrayList;

public class PacketArray extends PacketBase {

    private ArrayList<PacketBase> data;
    private String boundVariable;
    private String type;

    private int index = 0;

    public PacketArray(String name, String type, String boundVariable) {
        this.setName(name);

        this.data = new ArrayList<>();
        this.type = type;
        this.boundVariable = boundVariable;
    }

    public PacketArray(PacketArray arrayBase) {
        this.setName(arrayBase.getName());
        this.data = new ArrayList<>();
        this.type = arrayBase.getType();
        this.boundVariable = arrayBase.getBoundVariable();
    }

    /**
     * Overwrite the current data with a new one
     * @param packetData New packet data
     */
    public void set(ArrayList<PacketBase> packetData) {
        this.data = packetData;
    }

    /**
     * Add a new packet data to the array data
     * @param packetData Packet to add
     */
    public void add(PacketBase packetData) {
        this.data.add(packetData);
    }

    public void add(ArrayList<PacketBase> packetBases) {
        for(PacketBase base : packetBases) {
            add(base);
        }
    }

    /**
     * Get a packet based on the name of the packet
     * @param name Name of the packet to get
     * @return A packet
     */
    public PacketBase get(String name) {
        for(PacketBase base : this.data) {
            if(base.getName().equals(name)) return base;
        }

        return null;
    }

    /**
     * Get the next item in the array
     * @return A packet
     */
    public PacketBase getNext() {
        PacketBase out = this.data.get(index);
        this.index++;
        return out;
    }

    /**
     * Reset the pointer to the start of the packet data list
     */
    public void resetIndex() {
        this.index = 0;
    }

    /**
     * Look if there is an other packet in the data list
     * @return true if we're not at the end of the data list<br/>false otherwise
     */
    public boolean hasNext() {
        return this.index == this.data.size() - 1;
    }

    public ArrayList<PacketBase> getData() {
        return data;
    }

    public String getBoundVariable() {
        return boundVariable;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "PacketArray{" +
                "name='" + getName() + '\'' +
                ", data=" + data +
                ", boundVariable='" + boundVariable + '\'' +
                ", type='" + type + '\'' +
                ", index=" + index +
                '}';
    }
}
