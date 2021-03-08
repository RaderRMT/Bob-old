package fr.rader.bob.packet.reader;

public class PacketVariable extends PacketBase {

    private String type;
    private Object value = null;
    private boolean isGlobal = false;

    public PacketVariable(String name, String type) {
        this.setName(name);

        this.type = type;
    }

    public PacketVariable(String name, String type, Object value) {
        this.setName(name);

        this.type = type;
        this.value = value;
    }

    public void setGlobal(boolean global) {
        this.isGlobal = global;
    }

    public boolean isGlobal() {
        return this.isGlobal;
    }

    /**
     * Return this packet's variable type
     * @return Packet type
     */
    public String getType() {
        return this.type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PacketVariable{" +
                "name='" + this.getName() + '\'' +
                ", type='" + this.type + '\'' +
                ", value='" + this.value + '\'' +
                ", isGlobal='" + this.isGlobal + '\'' +
                '}';
    }
}
