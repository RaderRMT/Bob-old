package fr.rader.bob.packet;

import fr.rader.bob.Main;
import fr.rader.bob.utils.OS;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class PacketReader {

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

    private final int packetID;

    private LinkedHashMap<String, Object> properties;
    private LinkedHashMap<String, Object> variables;
    private List<String> variablesList;

    public PacketReader(int packetID) {
        this.packetID = packetID;

        // holds all created variables
        variablesList = new ArrayList<>();
        properties = readProperties();

        // becomes a stack
        variablesList = null;

        //System.out.println(properties);
    }

    /*public LinkedHashMap<String, Object> deserializePacket(byte[] rawData) {
        variables = new LinkedHashMap<>();
        LinkedHashMap<String, Object> data = deserializeMap(properties, new DataReader(rawData));

        for(byte value : rawData) System.out.print(String.format("%1$02X", value) + " ");
        System.out.println("\n" + variables);

        variables = null;
        return data;
    }

    private LinkedHashMap<String, Object> deserializeMap(LinkedHashMap<String, Object> map, DataReader reader) {
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();

        for(Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getValue() instanceof PacketArray) {
                PacketArray array = ((PacketArray) entry.getValue());

                if(array.isInlineArray()) {

                } else {
                    out.put(entry.getKey(), deserializeMap(array.getArrayData(), reader));
                }
            } else if(entry.getValue() instanceof PacketMatch) {
                PacketMatch match = ((PacketMatch) entry.getValue());
            } else if(entry.getValue() instanceof PacketCondition) {
                PacketCondition condition = ((PacketCondition) entry.getValue());
            } else {
                String variable = entry.getKey();
                variables.put(variable, getData(getDataType(variable, properties), reader));
            }
        }

        return out;
    }*/

    /*public LinkedHashMap<String, Object> deserializePacket(byte[] rawData) {
        return deserializeMap(properties, new DataReader(rawData));
    }

    private LinkedHashMap<String, Object> deserializeMap(LinkedHashMap<String, Object> map, DataReader reader) {
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();

        for(Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getValue() instanceof PacketMatch) {
                variablesList.add(entry.getKey());
                System.out.println(entry.getKey());
                //deserializeMap((LinkedHashMap<String, Object>) entry.getValue(), reader);
                variablesList.remove(variablesList.size() - 1);
                continue;
            }

            if(entry.getValue() instanceof PacketCondition) {
                variablesList.add(entry.getKey());
                System.out.println(entry.getKey());
                //deserializeMap((LinkedHashMap<String, Object>) entry.getValue(), reader);
                variablesList.remove(variablesList.size() - 1);
                continue;
            }

            if(entry.getValue() instanceof PacketArray) {
                variablesList.add(entry.getKey());
                System.out.println(entry.getKey());
                //deserializeMap((LinkedHashMap<String, Object>) entry.getValue(), reader);
                variablesList.remove(variablesList.size() - 1);
                continue;
            }

            if(entry.getValue() instanceof LinkedHashMap) {
                variablesList.add(entry.getKey());
                System.out.println(entry.getKey());
                deserializeMap((LinkedHashMap<String, Object>) entry.getValue(), reader);
                variablesList.remove(variablesList.size() - 1);
                continue;
            }

            System.out.println(entry.getKey());

            //getData(properties, entry.getKey(), reader);

            //out.put(entry.getKey(), getData(getDataType(entry.getKey(), properties), reader));
        }

        return out;
    }

    private Object getData(LinkedHashMap<String, Object> properties, String key, DataReader reader) {

    }*/

    private LinkedHashMap<String, Object> readUntil(BufferedReader reader, String endCharacter) {
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();

        try {
            String line;
            while((line = reader.readLine()) != null) {
                line = line.split("//")[0].trim();
                if(line.isEmpty()) continue;
                if(line.contains(endCharacter)) return out;

                if(line.matches("\\d+ => \\{")) {
                    out.put(line.split(" ")[0], readUntil(reader, "}"));
                }

                if(startWithVariableType(line)) {
                    String name;
                    Object value;
                    if(line.contains("[")) {
                        name = line.split("\"")[3];
                        String boundVariable = line.split("\"")[1];
                        if(!variablesList.contains(boundVariable)) stop("Variable \"" + boundVariable + "\" does not exist!");
                        value = new PacketArray(line.split("\\[")[0], boundVariable);
                    } else {
                        name = line.split("\"")[1];
                        value = line.split(" ")[0];
                    }

                    out.put(name, value);
                    variablesList.add(name);
                }

                if(startWithActionType(line)) {
                    String actionType = line.split(" ")[0];

                    String boundVariable;
                    String name;
                    switch(actionType) {
                        case "match":
                            boundVariable = line.split("\"")[1];
                            if(!variablesList.contains(boundVariable)) stop("Variable \"" + boundVariable + "\" does not exist!");
                            name = "Matches " + boundVariable;

                            out.put(name, new PacketMatch(readUntil(reader, "}")));
                            variablesList.add(name);
                            break;
                        case "if":
                            String variable = line.split("\"")[1];
                            if(!variablesList.contains(variable)) stop("Variable \"" + variable + "\" does not exist!");

                            PacketCondition packetCondition = null;
                            String condition = line.split("\"")[2].trim().split(" ")[0];
                            if(condition.matches("[=<>]{1,2}")) {
                                if(isComparingTypeValid(condition)) {
                                    int valueToCompare = Integer.parseInt(line.split("\"")[2].trim().split(" ")[1]);
                                    packetCondition = new PacketCondition(readUntil(reader, "}"), variable, condition, valueToCompare);
                                }
                            } else {
                                packetCondition = new PacketCondition(readUntil(reader, "}"), variable, "==", !line.contains("not"));
                            }

                            name = "If " + packetCondition.getConditionVariable()
                                    + " " + packetCondition.getConditionType()
                                    + " " + packetCondition.getValueToCompare();

                            out.put(name, packetCondition);
                            variablesList.add(name);
                            break;
                        default: // array, ex: array["testing"] "test array" [ /* ... */ ]
                            boundVariable = line.split("\"")[1];
                            if(!variablesList.contains(boundVariable)) stop("Variable \"" + boundVariable + "\" does not exist!");

                            name = line.split("\"")[3];
                            PacketArray packetArray = new PacketArray(boundVariable, readUntil(reader, "]"));
                            out.put(name, packetArray);
                            variablesList.add(name);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    private LinkedHashMap<String, Object> readProperties() {
        FileReader fileReader = null;
        BufferedReader reader = null;

        try {
            fileReader = new FileReader(OS.getBobFolder() + "resources/assets/protocols/" + Main.getInstance().getReplayData().getProtocolVersion() + ".bob");
            reader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Cannot find protocol version " + Main.getInstance().getReplayData().getProtocolVersion() + "! (that version might not be supported)");
            System.exit(-1);
        }

        LinkedHashMap<String, Object> out = null;
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

    private void stop(String message) {
        System.out.println(message);
        System.exit(-1);
    }

    private String getDataType(String key, LinkedHashMap<String, Object> list) {
        if(list.containsKey(key)) return (String) list.get(key);

        for(Map.Entry<String, Object> property : list.entrySet()) {
            if(property.getValue() instanceof LinkedHashMap) {
                @SuppressWarnings("unchecked")
                String out = getDataType(key, (LinkedHashMap<String, Object>) property.getValue());
                if(out != null) return out;
            }
        }

        return null;
    }

    /*private Object getData(String type, DataReader reader) {
        switch(type) {
            case "boolean": return reader.readBoolean();
            case "angle":
            case "byte": return reader.readByte();
            case "short": return reader.readShort();
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
            default:
                System.out.println("getData: unknown type " + type); break;
        }

        return null;
    }*/

    private boolean isComparingTypeValid(String comparingType) {
        return comparingType.equals("==")
                || comparingType.equals("<")
                || comparingType.equals(">")
                || comparingType.equals("<=")
                || comparingType.equals(">=");
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
