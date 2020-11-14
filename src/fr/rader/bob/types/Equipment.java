package fr.rader.bob.types;

public class Equipment {

    private int slot;
    private Slot item;

    public Equipment(int slot, Slot item) {
        this.slot = slot;
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Slot getItem() {
        return item;
    }

    public void setItem(Slot item) {
        this.item = item;
    }
}