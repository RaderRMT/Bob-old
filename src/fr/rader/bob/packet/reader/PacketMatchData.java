package fr.rader.bob.packet.reader;

import java.util.ArrayList;

public class PacketMatchData extends PacketBase {

    private ArrayList<PacketBase> data;

    public PacketMatchData(String name, ArrayList<PacketBase> data) {
        setName(name);
        this.data = data;
    }

    public ArrayList<PacketBase> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PacketMatchData{" +
                "name='" + getName() + '\'' +
                ", data=" + data +
                '}';
    }
}
