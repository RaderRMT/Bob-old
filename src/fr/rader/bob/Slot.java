package fr.rader.bob;

import fr.rader.bob.nbt.NBTTagCompound;

public class Slot {
    
    private boolean present;
    private int itemID;
    private int itemCount;
    private NBTTagCompound nbt;

    public Slot(boolean present, int itemID, int itemCount, NBTTagCompound nbt) {
        this.present = present;
        this.itemID = itemID;
        this.itemCount = itemCount;
        this.nbt = nbt;
    }

    public Slot(boolean present) {
        this.present = present;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public NBTTagCompound getNbt() {
        return nbt;
    }

    public void setNbt(NBTTagCompound nbt) {
        this.nbt = nbt;
    }
}
