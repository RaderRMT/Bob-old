package fr.rader.bob.types;

import fr.rader.bob.nbt.tags.NBTCompound;

public class Slot {

    // only for MC < 1.14
    private int blockID;

    // only for MC >= 1.14
    private boolean present;

    // for MC < 1.14, this is the item damage
    // for MC >= 1.14, this is the item ID
    private int itemDamage;

    private int itemCount;
    private NBTCompound nbt;

    public Slot(int blockID) {
        this.blockID = blockID;
    }

    public Slot(boolean present) {
        this.present = present;
    }

    public int getBlockID() {
        return blockID;
    }

    public boolean isPresent() {
        return present;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getItemDamage() {
        return itemDamage;
    }

    public int getItemID() {
        return itemDamage;
    }

    public NBTCompound getNbt() {
        return nbt;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setItemDamage(int itemDamage) {
        this.itemDamage = itemDamage;
    }

    public void setItemID(int itemID) {
        this.itemDamage = itemID;
    }

    public void setNbt(NBTCompound nbt) {
        this.nbt = nbt;
    }
}
