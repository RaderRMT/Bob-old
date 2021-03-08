package fr.rader.bob.packet.reader;

import java.util.ArrayList;

public class PacketData extends PacketBase {

    private ArrayList<PacketBase> data;

    private int index = 0;

    public PacketData(String name) {
        setName(name);

        this.data = new ArrayList<>();
    }

    public void add(PacketBase packetData) {
        this.data.add(packetData);
    }

    public PacketBase getNext() {
        PacketBase out = this.data.get(index);
        this.index++;
        return out;
    }

    public PacketBase get(String name) {
        for(PacketBase base : data) {
            if(base.getName().equals(name)) return base;
        }

        return null;
    }

    public void resetIndex() {
        this.index = 0;
    }

    public boolean hasNext() {
        return this.index == this.data.size() - 1;
    }
}
