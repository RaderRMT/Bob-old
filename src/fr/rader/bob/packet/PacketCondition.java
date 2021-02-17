package fr.rader.bob.packet;

import java.util.LinkedHashMap;

public class PacketCondition {

    private LinkedHashMap<String, Object> data;

    private String conditionVariable;
    private String conditionType;
    private Object valueToCompare;

    public PacketCondition(LinkedHashMap<String, Object> data, String conditionVariable, String conditionType, Object valueToCompare) {
        this.data = data;
        this.conditionVariable = conditionVariable;
        this.conditionType = conditionType;
        this.valueToCompare = valueToCompare;
    }

    public LinkedHashMap<String, Object> getData() {
        return data;
    }

    public void setData(LinkedHashMap<String, Object> data) {
        this.data = data;
    }

    public String getConditionVariable() {
        return conditionVariable;
    }

    public void setConditionVariable(String conditionVariable) {
        this.conditionVariable = conditionVariable;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public Object getValueToCompare() {
        return valueToCompare;
    }

    public void setValueToCompare(Object valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    @Override
    public String toString() {
        return "PacketCondition{" +
                "data=" + data +
                ", conditionVariable='" + conditionVariable + '\'' +
                ", conditionType='" + conditionType + '\'' +
                ", valueToCompare=" + valueToCompare +
                '}';
    }
}