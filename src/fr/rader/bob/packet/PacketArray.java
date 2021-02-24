package fr.rader.bob.packet;

import java.util.LinkedHashMap;

public class PacketArray {

    private String type;          // type of the data to read
    private String boundVariable; // length of the array based on the variable's value

    private LinkedHashMap<String, Object> arrayData;

    public PacketArray(String type, String boundVariable) {
        this.type = type;
        this.boundVariable = boundVariable;
    }

    public PacketArray(String boundVariable, LinkedHashMap<String, Object> arrayData) {
        this.boundVariable = boundVariable;
        this.arrayData = arrayData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBoundVariable() {
        return boundVariable;
    }

    public void setBoundVariable(String boundVariable) {
        this.boundVariable = boundVariable;
    }

    public LinkedHashMap<String, Object> getArrayData() {
        return arrayData;
    }

    public void setArrayData(LinkedHashMap<String, Object> arrayData) {
        this.arrayData = arrayData;
    }

    public boolean isInlineArray() {
        return type != null;
    }

    @Override
    public String toString() {
        return "PacketArray{" +
                "boundVariable='" + boundVariable + '\'' +
                ", arrayData=" + arrayData +
                '}';
    }
}
