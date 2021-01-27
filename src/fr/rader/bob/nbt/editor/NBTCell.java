package fr.rader.bob.nbt.editor;

import fr.rader.bob.nbt.tags.NBTBase;
import fr.rader.bob.nbt.tags.NBTList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class NBTCell extends DefaultMutableTreeNode {

    private final NBTBase associatedNBT;

    private String name;
    private String iconName;

    private int tagID;

    public NBTCell(NBTBase associatedNBT, int id) {
        this.associatedNBT = associatedNBT;

        this.name = "";
        if(associatedNBT.getName() != null) this.name = associatedNBT.getName() + ": ";

        tagID = associatedNBT.getId();
        if(tagID <= 0) tagID = id;
        switch(tagID) {
            case 1:
                this.iconName = "byte";
                this.name += associatedNBT.getAsByte();
                break;
            case 2:
                this.iconName = "short";
                this.name += associatedNBT.getAsShort();
                break;
            case 3:
                this.iconName = "int";
                this.name += associatedNBT.getAsInt();
                break;
            case 4:
                this.iconName = "long";
                this.name += associatedNBT.getAsLong();
                break;
            case 5:
                this.iconName = "float";
                this.name += associatedNBT.getAsFloat();
                break;
            case 6:
                this.iconName = "double";
                this.name += associatedNBT.getAsDouble();
                break;
            case 7:
                this.iconName = "byteArray";
                this.name += associatedNBT.getAsByteArray().length + " bytes";
                break;
            case 8:
                this.iconName = "string";
                this.name += associatedNBT.getAsString();
                break;
            case 9:
                this.iconName = "list";
                this.name += associatedNBT.getAsList().getComponents().length + " entries";
                break;
            case 10:
                this.iconName = "compound";
                this.name += associatedNBT.getAsCompound().getComponents().length + " entries";
                break;
            case 11:
                this.iconName = "intArray";
                this.name += associatedNBT.getAsIntArray().length + " integers";
                break;
            case 12:
                this.iconName = "longArray";
                this.name += associatedNBT.getAsLongArray().length + " long integers";
                break;
        }
    }

    @Override
    public String toString() {
        return "NBTCell{" +
                "name='" + name + '\'' +
                ", iconName='" + iconName + '\'' +
                '}';
    }

    public boolean invokeNameEditor() {
        if(getName().split(": ").length == 0) return false;

        NBTCell parent = (NBTCell) getParent();
        if(parent.getAssociatedNBT() instanceof NBTList) return false;

        String input = (String) JOptionPane.showInputDialog(null, null, "Edit Name...", JOptionPane.PLAIN_MESSAGE, null, null, getName().split(": ")[0]);
        if(input == null) return false;

        setName(input + ": " + getName().split(": ")[1]);
        associatedNBT.setName(input);
        return true;
    }

    public boolean invokeValueEditor() {
        String input;
        switch(iconName) {
            case "byte":
                input = showEditValueBox();
                if(input == null) return false;

                try {
                    byte value = Byte.parseByte(input);
                    if(getName().split(": ").length == 2) setName(getName().split(": ")[0] + ": " + value);
                    else setName(Byte.toString(value));
                    associatedNBT.getAsNBTByte().setValue(value);
                    return true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Value must be between " + Byte.MIN_VALUE + " and " + Byte.MAX_VALUE);
                }
                return false;
            case "short":
                input = showEditValueBox();
                if(input == null) return false;

                try {
                    short value = Short.parseShort(input);
                    if(getName().split(": ").length == 2) setName(getName().split(": ")[0] + ": " + value);
                    else setName(Short.toString(value));
                    associatedNBT.getAsNBTShort().setValue(value);
                    return true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Value must be between " + Short.MIN_VALUE + " and " + Short.MAX_VALUE);
                }
                return false;
            case "int":
                input = showEditValueBox();
                if(input == null) return false;

                try {
                    int value = Integer.parseInt(input);
                    if(getName().split(": ").length == 2) setName(getName().split(": ")[0] + ": " + value);
                    else setName(Integer.toString(value));
                    associatedNBT.getAsNBTInt().setValue(value);
                    return true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Value must be between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE);
                }
                return false;
            case "long":
                input = showEditValueBox();
                if(input == null) return false;

                try {
                    long value = Long.parseLong(input);
                    if(getName().split(": ").length == 2) setName(getName().split(": ")[0] + ": " + value);
                    else setName(Long.toString(value));
                    associatedNBT.getAsNBTLong().setValue(value);
                    return true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Value must be between " + Long.MIN_VALUE + " and " + Long.MAX_VALUE);
                }
                return false;
            case "float":
                input = showEditValueBox();
                if(input == null) return false;

                try {
                    float value = Float.parseFloat(input);
                    if(getName().split(": ").length == 2) setName(getName().split(": ")[0] + ": " + value);
                    else setName(Float.toString(value));
                    associatedNBT.getAsNBTFloat().setValue(value);
                    return true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Value must be between " + Float.MIN_VALUE + " and " + Float.MAX_VALUE);
                }
                return false;
            case "double":
                input = showEditValueBox();
                if(input == null) return false;

                try {
                    double value = Double.parseDouble(input);
                    if(getName().split(": ").length == 2) setName(getName().split(": ")[0] + ": " + value);
                    else setName(Double.toString(value));
                    associatedNBT.getAsNBTDouble().setValue(value);
                    return true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Value must be between " + Double.MIN_VALUE + " and " + Double.MAX_VALUE);
                }
                return false;
            case "string":
                input = showEditValueBox();
                if(input == null) return false;

                if(getName().split(": ").length == 2) setName(getName().split(": ")[0] + ": " + input);
                else setName(input);
                associatedNBT.getAsNBTString().setValue(input);
                return true;
            case "byteArray":
                NBTArrayEditor byteArrayEditor = new NBTArrayEditor();
                byteArrayEditor.invokeEditor(associatedNBT);
                if(byteArrayEditor.getByteArrayData() == null) return false;

                if(getName().split(": ").length == 2) setName(getName().split(": ")[0] + ": " + byteArrayEditor.getByteArrayData().length + " entries");
                else setName(byteArrayEditor.getByteArrayData().length + " entries");
                return true;
            case "intArray":
                NBTArrayEditor intArrayEditor = new NBTArrayEditor();
                intArrayEditor.invokeEditor(associatedNBT);
                if(intArrayEditor.getIntArrayData() == null) return false;

                if(getName().split(": ").length == 2) setName(getName().split(": ")[0] + ": " + intArrayEditor.getIntArrayData().length + " entries");
                else setName(intArrayEditor.getIntArrayData().length + " entries");
                return true;
            case "longArray":
                NBTArrayEditor longArrayEditor = new NBTArrayEditor();
                longArrayEditor.invokeEditor(associatedNBT);
                if(longArrayEditor.getLongArrayData() == null) return false;

                if(getName().split(": ").length == 2) setName(getName().split(": ")[0] + ": " + longArrayEditor.getLongArrayData().length + " entries");
                else setName(longArrayEditor.getLongArrayData().length + " entries");
                return true;
            default:
                return false;
        }
    }

    private String showEditValueBox() {
        String input;
        if(getName().split(": ").length == 2) input = (String) JOptionPane.showInputDialog(null, null, "Edit Value...", JOptionPane.PLAIN_MESSAGE, null, null, getName().split(": ")[1]);
        else input = (String) JOptionPane.showInputDialog(null, null, "Edit Value...", JOptionPane.PLAIN_MESSAGE, null, null, getName());
        return input;
    }

    public String getName() {
        return name;
    }

    public String getRealName() {
        if(getName().split(": ").length == 2) return name.split(": ")[0];
        else return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconName() {
        return iconName;
    }

    public NBTBase getAssociatedNBT() {
        return associatedNBT;
    }

    public int getTagID() {
        return tagID;
    }
}
