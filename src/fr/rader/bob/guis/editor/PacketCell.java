package fr.rader.bob.guis.editor;

import javax.swing.tree.DefaultMutableTreeNode;

public class PacketCell extends DefaultMutableTreeNode {

    private String name;
    private String icon;

    public PacketCell(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public PacketCell(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
