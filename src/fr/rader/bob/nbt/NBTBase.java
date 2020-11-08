package fr.rader.bob.nbt;

public class NBTBase {

    private int length;
    private int id;
    private String name;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] toByteArray(boolean fromList) {
        return null;
    }
}
