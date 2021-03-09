package fr.rader.bob.packet.reader;

import fr.rader.bob.Main;
import fr.rader.bob.nbt.tags.NBTCompound;
import fr.rader.bob.packet.Packet;
import fr.rader.bob.types.Position;
import fr.rader.bob.utils.DataReader;
import fr.rader.bob.utils.DataWriter;
import fr.rader.bob.utils.OS;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class PacketReader {

    private final int packetID;

    private final String[] variableTypes = {
            "boolean",
            "angle",
            "byte",
            "short",
            "int",
            "long",
            "float",
            "double",
            "varint",
            "varlong",
            "string",
            "chat",
            "identifier",
            "nbt",
            "position",
            "uuid",
            "slot",
            "metadata"
    };

    private final String[] actionTypes = {
            "array",
            "match",
            "if"
    };

    private ArrayList<PacketBase> properties;
    private ArrayList<PacketVariable> globalVariables;

    public PacketReader(int packetID) {
        this.packetID = packetID;

        properties = readProperties();

        //System.out.println(properties);
    }

    public ArrayList<PacketBase> deserializePacket(Packet packet) {
        DataReader reader = new DataReader(packet.getRawData());

        globalVariables = new ArrayList<>();
        return deserializePacketData(properties, reader);
    }

    private ArrayList<PacketBase> deserializePacketData(ArrayList<PacketBase> properties, DataReader reader) {
        ArrayList<PacketBase> out = new ArrayList<>();

        for(PacketBase base : properties) {
            if(base instanceof PacketVariable) {
                PacketVariable variableBase = ((PacketVariable) base);

                PacketVariable variable = new PacketVariable(variableBase.getName(),
                        variableBase.getType(),
                        getValue(reader, variableBase.getType()));

                variable.setGlobal(variableBase.isGlobal());

                if(variableBase.isGlobal()) globalVariables.add(variable);
                out.add(variable);
            }

            if(base instanceof PacketArray) {
                PacketArray arrayBase = ((PacketArray) base);

                PacketArray array = new PacketArray(arrayBase);
                if(array.getType() == null) {
                    for(int i = 0; i < (int) getGlobalValue(globalVariables, array.getBoundVariable()); i++) {
                        array.add(deserializePacketData(arrayBase.getData(), reader));
                    }
                } else {
                    for(int i = 0; i < (int) getGlobalValue(globalVariables, array.getBoundVariable()); i++) {
                        array.add(new PacketVariable(String.valueOf(i), array.getType(), getValue(reader, array.getType())));
                    }
                }

                out.add(array);
            }

            if(base instanceof PacketMatch) {
                PacketMatch matchBase = ((PacketMatch) base);

                PacketMatch match = new PacketMatch(matchBase);
                PacketBase matchData = matchBase.get((int) getGlobalValue(globalVariables, matchBase.getBoundVariable()));

                if(matchData != null) {
                    ArrayList<PacketBase> matchDataArray = matchData.getAsPacketMatchData().getData();
                    match.set(deserializePacketData(matchDataArray, reader));
                }

                out.add(match);
            }

            if(base instanceof PacketCondition) {
                PacketCondition conditionBase = ((PacketCondition) base);

                PacketCondition condition = new PacketCondition(conditionBase);
                if(condition.getValueToCompare() instanceof Boolean) {
                    if(getGlobalValue(globalVariables, conditionBase.getConditionVariable()) == condition.getValueToCompare()) {
                        condition.setData(deserializePacketData(conditionBase.getData(), reader));
                    }
                } else {
                    int conditionVariableValue = (int) getGlobalValue(globalVariables, condition.getConditionVariable());
                    int valueToCompare = (int) condition.getValueToCompare();
                    switch(condition.getConditionType()) {
                        case "==":
                            if(conditionVariableValue == valueToCompare) condition.setData(deserializePacketData(conditionBase.getData(), reader));
                            break;
                        case "<=":
                            if(conditionVariableValue <= valueToCompare) condition.setData(deserializePacketData(conditionBase.getData(), reader));
                            break;
                        case ">=":
                            if(conditionVariableValue >= valueToCompare) condition.setData(deserializePacketData(conditionBase.getData(), reader));
                            break;
                        case "<":
                            if(conditionVariableValue < valueToCompare) condition.setData(deserializePacketData(conditionBase.getData(), reader));
                            break;
                        case ">":
                            if(conditionVariableValue > valueToCompare) condition.setData(deserializePacketData(conditionBase.getData(), reader));
                            break;
                    }
                }

                out.add(condition);
            }
        }

        return out;
    }

    public Object getGlobalValue(ArrayList<PacketVariable> list, String globalName) {
        for(PacketVariable base : list) {
            if(base.getName().equals(globalName)) return base.getValue();
        }

        throw new IllegalStateException("Global \"" + globalName + "\" not found!");
    }

    private Object getValue(DataReader reader, String type) {
        switch(type) {
            case "boolean": return reader.readBoolean();
            case "angle":
            case "byte": return reader.readByte() & 0xff;
            case "short": return reader.readShort() & 0xffff;
            case "int": return reader.readInt();
            case "long": return reader.readLong();
            case "float": return reader.readFloat();
            case "double": return reader.readDouble();
            case "chat":
            case "identifier":
            case "string": return reader.readString(reader.readVarInt());
            case "varint": return reader.readVarInt();
            case "varlong": return reader.readVarLong();
            case "nbt": return reader.readNBT();
            case "position": return reader.readPosition();
            case "uuid": return reader.readUUID();
            // todo:
            //  metadata
            //  slot
            default:
                System.out.println("getValue: unknown type \"" + type + "\"");
                break;
        }

        return null;
    }

    public Packet serializePacket(ArrayList<PacketBase> packetData) {
        DataWriter writer = new DataWriter();

        serializeArray(packetData, writer);

        return new Packet(writer.getData(), packetID);
    }

    private void serializeArray(ArrayList<PacketBase> packetData, DataWriter writer) {
        for(PacketBase base : packetData) {
            if(base instanceof PacketVariable) {
                writeValue(writer, ((PacketVariable) base).getValue(), ((PacketVariable) base).getType());
            }

            if(base instanceof PacketArray) {
                serializeArray(((PacketArray) base).getData(), writer);
            }

            if(base instanceof PacketMatch) {
                serializeArray(((PacketMatch) base).getData(), writer);
            }

            if(base instanceof PacketCondition) {
                serializeArray(((PacketCondition) base).getData(), writer);
            }
        }
    }

    private void writeValue(DataWriter writer, Object object, String type) {
        switch(type) {
            case "boolean": writer.writeBoolean((Boolean) object); break;
            case "angle":
            case "byte": writer.writeByte((Integer) object); break;
            case "short": writer.writeShort((Integer) object); break;
            case "int": writer.writeInt((Integer) object); break;
            case "long": writer.writeLong((Long) object); break;
            case "float": writer.writeFloat((Float) object); break;
            case "double": writer.writeDouble((Double) object); break;
            case "chat":
            case "identifier":
            case "string":
                writer.writeVarInt(((String) object).length());
                writer.writeString((String) object);
                break;
            case "varint": writer.writeVarInt((Integer) object); break;
            case "varlong": writer.writeVarLong((Long) object); break;
            case "nbt": writer.writeNBT((NBTCompound) object); break;
            case "position": writer.writePosition((Position) object); break;
            case "uuid": writer.writeUUID((UUID) object); break;
            // todo:
            //  metadata
            //  slot
        }
    }

    private ArrayList<PacketBase> readProperties() {
        FileReader fileReader = null;
        BufferedReader reader = null;

        try {
            fileReader = new FileReader(OS.getBobFolder() + "resources/assets/protocols/" + Main.getInstance().getReplayData().getProtocolVersion() + ".bob");
            reader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Cannot find protocol version " + Main.getInstance().getReplayData().getProtocolVersion() + "! (that version might not be supported)");
            System.exit(-1);
        }

        ArrayList<PacketBase> out = null;
        try {
            String line;
            while((line = reader.readLine()) != null) {
                if(line.isEmpty() || line.startsWith("//")) continue;

                if(line.matches("^[0-9A-F]{2}: ?\\{") && line.startsWith(String.format("%1$02X", packetID))) {
                    out = readUntil(reader, "}");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    private ArrayList<PacketBase> readUntil(BufferedReader reader, String endCharacter) {
        ArrayList<PacketBase> out = new ArrayList<>();

        try {
            String line;
            while((line = reader.readLine()) != null) {
                line = line.split("//")[0].trim();
                if(line.isEmpty()) continue;
                if(line.contains(endCharacter)) return out;

                if(line.matches("\\d+ => \\{")) {
                    out.add(new PacketMatchData(line.split(" ")[0], readUntil(reader, "}")));
                }

                if(startWithVariableType(line)) {
                    String name;
                    if(line.contains("[")) { // array
                        name = line.split("\"")[3];
                        String boundVariable = line.split("\"")[1];
                        out.add(new PacketArray(name, line.split("\\[")[0], boundVariable));
                    } else { // normal variable
                        name = line.split("\"")[1];
                        PacketVariable packetVariable = new PacketVariable(name, line.split(" ")[0]);
                        packetVariable.setGlobal(line.contains("global"));
                        out.add(packetVariable);
                    }

                    continue;
                }

                if(startWithActionType(line)) {
                    String actionType = line.split(" ")[0];
                    String boundVariable = line.split("\"")[1];

                    switch(actionType) {
                        case "match":
                            PacketMatch packetMatch = new PacketMatch(boundVariable);
                            packetMatch.set(readUntil(reader, "}"));
                            out.add(packetMatch);
                            break;
                        case "if":
                            PacketCondition packetCondition;
                            String condition = line.split("\"")[2].trim().split(" ")[0];
                            if(condition.matches("[=<>]{1,2}")) {
                                int valueToCompare = Integer.parseInt(line.split("\"")[2].trim().split(" ")[1]);
                                packetCondition = new PacketCondition(boundVariable, condition, valueToCompare, readUntil(reader, "}"));
                            } else {
                                packetCondition = new PacketCondition(boundVariable, "==", !line.contains("not"), readUntil(reader, "}"));
                            }

                            out.add(packetCondition);
                            break;
                        default:
                            PacketArray packetArray = new PacketArray(line.split("\"")[3], null, boundVariable);
                            packetArray.set(readUntil(reader, "]"));
                            out.add(packetArray);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    private boolean startWithVariableType(String line) {
        for(String var : variableTypes) {
            if(line.startsWith(var)) return true;
        }

        return false;
    }

    private boolean startWithActionType(String line) {
        for(String var : actionTypes) {
            if(line.startsWith(var)) return true;
        }

        return false;
    }
}
