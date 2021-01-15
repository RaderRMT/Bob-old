package fr.rader.bob.packet;

import java.util.LinkedHashMap;

public class Packet {

    private LinkedHashMap<String, Object> properties;
    private int packetID;

    public Packet(int packetID) {
        this.packetID = packetID;
        properties = new LinkedHashMap<>();
    }

    public int getPacketID() {
        return packetID;
    }

    public void setProperties(LinkedHashMap<String, Object> properties) {
        this.properties = properties;
    }

    public LinkedHashMap<String, Object> getProperties() {
        return properties;
    }

    public void addProperty(String name, Object value) {
        properties.put(name, value);
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public String toString() {
        return "Packet{" +
                "properties=" + properties +
                '}';
    }
}
