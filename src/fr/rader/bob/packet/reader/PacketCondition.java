package fr.rader.bob.packet.reader;

import java.util.ArrayList;

public class PacketCondition extends PacketBase {

    private ArrayList<PacketBase> data;

    private String conditionVariable;
    private String conditionType;
    private Object valueToCompare;

    public PacketCondition(String conditionVariable, String conditionType, Object valueToCompare, ArrayList<PacketBase> data) {
        setName("If " + conditionVariable
                + " " + conditionType
                + " " + valueToCompare);

        this.data = data;
        this.conditionVariable = conditionVariable;
        this.conditionType = conditionType;
        this.valueToCompare = valueToCompare;
    }

    public PacketCondition(PacketCondition conditionBase) {
        this.setName(conditionBase.getName());
        this.data = new ArrayList<>();
        this.conditionVariable = conditionBase.getConditionVariable();
        this.conditionType = conditionBase.getConditionType();
        this.valueToCompare = conditionBase.getValueToCompare();
    }

    public String getConditionVariable() {
        return conditionVariable;
    }

    public String getConditionType() {
        return conditionType;
    }

    public Object getValueToCompare() {
        return valueToCompare;
    }

    public void setData(ArrayList<PacketBase> data) {
        this.data = data;
    }

    public ArrayList<PacketBase> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PacketCondition{" +
                "name='" + getName() + '\'' +
                ", data=" + data +
                ", conditionVariable='" + conditionVariable + '\'' +
                ", conditionType='" + conditionType + '\'' +
                ", valueToCompare=" + valueToCompare +
                '}';
    }
}
