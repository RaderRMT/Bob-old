package fr.rader.bob.packet;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.Main;
import fr.rader.bob.nbt.NBTCompound;

import java.io.*;
import java.util.*;

public class PacketReader {

    private final int packetID;

    private LinkedHashMap<String, Object> properties;
    private LinkedHashMap<String, Object> tempProperties;

    private int lineCounter = 0;

    private final String[] reservedWords = {
            "array",
            "match",
            "if"
    };

    private final String[] types = {
            "boolean",
            "angle",
            "byte",
            "short",
            "int",
            "long",
            "float",
            "double",
            //"string",
            //"chat",
            //"identifier",
            "varint",
            "varlong",
            "nbt",
            //"position",
            "uuid"
    };

    public PacketReader(int packetID) {
        this.packetID = packetID;
        properties = new LinkedHashMap<>();

        readProperties(packetID);

        System.out.println(properties);
    }

    public Packet deserializePacket(byte[] rawData) {
        DataReader reader = new DataReader(rawData);
        Packet packet = new Packet(packetID);
        tempProperties = new LinkedHashMap<>();

        // read the properties and build the packet based on the properties
        packet.setProperties(readMap(properties, reader));

        tempProperties = null;

        return packet;
    }

    public byte[] serializePacket(Packet packet) {
        DataWriter writer = new DataWriter();

        serializeMap(packet.getProperties(), writer);

        return writer.getData();
    }

    private void serializeMap(LinkedHashMap<String, Object> packetProperties, DataWriter writer) {
        for(Map.Entry<String, Object> property : packetProperties.entrySet()) {
            if(property.getValue() instanceof LinkedHashMap) {
                serializeMap((LinkedHashMap<String, Object>) property.getValue(), writer);
            } else if(property.getValue() instanceof Object[]) {
                writeArray(property.getKey(), (Object[]) (property.getValue()), writer);
            } else {
                writeValue(writer, property.getValue(), getDataType(property.getKey(), properties));
            }
        }
    }

    private void writeArray(String key, Object[] array, DataWriter writer) {
        String newKey = key.substring(1).split("_")[0];
        for(Object value : array) {
            writeValue(writer, value, newKey);
        }
    }

    private LinkedHashMap<String, Object> readMap(LinkedHashMap<String, Object> packetProperties, DataReader reader) {
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();

        for(Map.Entry<String, Object> property : packetProperties.entrySet()) {
            if(property.getValue() instanceof LinkedHashMap) {
                String key = property.getKey();
                switch(key.split("_")[1]) {
                    case "array":
                        LinkedHashMap<String, Object> arrayData = new LinkedHashMap<>();
                        int count = (int) tempProperties.get(key.substring(7));

                        for(int i = 0; i < count; i++) {
                            arrayData.put(String.valueOf(i), readMap(((LinkedHashMap<String, Object>) property.getValue()), reader));
                        }

                        out.put(key, arrayData);
                        break;
                    case "match":
                        String matchKey = key.substring(7);
                        LinkedHashMap<String, Object> matchMap = (LinkedHashMap<String, Object>) property.getValue();
                        out.put(key, readMap((LinkedHashMap<String, Object>) matchMap.get(String.valueOf(tempProperties.get(matchKey))), reader));
                        break;
                    case "if":
                        readIf(out, key, (LinkedHashMap<String, Object>) property.getValue(), reader);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + key);
                }
            } else if(property.getValue() instanceof String) {
                String value = (String) property.getValue();
                if(value.startsWith("*")) {
                    String type = value.split("_")[1];
                    String var = value.split("_")[2];

                    out.put(property.getKey(), fillArray(type, var, reader));
                } else {
                    Object object = getData(value, reader);

                    tempProperties.put(property.getKey(), object);
                    out.put(property.getKey(), object);
                }
            }
        }

        return out;
    }

    private void readIf(LinkedHashMap<String, Object> out, String key, LinkedHashMap<String, Object> property, DataReader reader) {
        String var = key.split("_")[2];
        String condition = key.split("_")[3];
        String matches = key.split("_")[4];

        if(isNumber((String) properties.get(var))) {
            long value = Long.parseLong(String.valueOf(tempProperties.get(var)));
            long toMatch = Long.parseLong(matches);

            switch(condition) {
                case "==":
                    if(value == toMatch) out.put(key, readMap(property, reader));
                    break;
                case "<":
                    if(value < toMatch) out.put(key, readMap(property, reader));
                    break;
                case ">":
                    if(value > toMatch) out.put(key, readMap(property, reader));
                    break;
                case "<=":
                    if(value <= toMatch) out.put(key, readMap(property, reader));
                    break;
                case ">=":
                    if(value >= toMatch) out.put(key, readMap(property, reader));
                    break;
            }
        } else if(properties.get(var).equals("boolean")) {
            if(Boolean.parseBoolean(matches) == (boolean) tempProperties.get(var)) out.put(key, readMap(property, reader));
        }
    }

    private Object[] fillArray(String type, String varName, DataReader reader) {
        int length;
        if(isVariableType(varName)) {
            length = (int) getData(varName, reader);
        } else {
            length = (int) tempProperties.get(varName);
        }

        Object[] out = new Object[length];
        for(int i = 0; i < length; i++) {
            out[i] = getData(type, reader);
        }

        return out;
    }

    private void readProperties(int packetID) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/1.16.4/packetData.bob")));

        try {
            String line;
            while((line = reader.readLine()) != null) {
                lineCounter++;

                // we remove comments here
                line = line.split("//")[0].trim();

                // we skip the current line if it's empty
                if(line.isEmpty()) continue;

                // we go to the line that contains the correct packet data
                if(line.matches("[0-9A-F]+: ?\\{") && line.startsWith(String.format("%1$02X", packetID))) {
                    properties = readUntil(reader, "}");
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LinkedHashMap<String, Object> readUntil(BufferedReader reader, String endCharacter) {
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();

        try {
            String line;
            while((line = reader.readLine()) != null) {
                lineCounter++;

                // we remove comments and trim the line
                line = line.split("//")[0].trim();

                // we skip the current line if it's empty
                if(line.isEmpty()) continue;

                // we return if we met the end character
                if(line.contains(endCharacter)) return out;

                if(line.contains("*") || line.contains("_")) throw new IllegalStateException("Line " + lineCounter + " contains illegal characters (* or _), remove them and try again. Line: " + line);

                if(startWithReservedWord(line)) {
                    String key;
                    if(line.contains("\"")) {
                        key = line.split("\"")[1];
                        if(!properties.containsKey(key)) {
                            System.out.println("[Error]: \"" + key + "\" does not exist! Line " + lineCounter + ": " + line);
                            System.exit(-1);
                        }
                    } else {
                        key = line.substring(6, line.length() - 2);
                        if(!isVariableType(key)) {
                            System.out.println("[Error]: \"" + key + "\" is not a variable type! Line " + lineCounter + ": " + line);
                            System.exit(-1);
                        }
                    }

                    switch(getReservedWord(line)) {
                        case "array":
                            out.put("_array_" + key, readUntil(reader, "]"));
                            break;
                        case "match":
                            out.put("_match_" + key, readUntil(reader, "}"));
                            break;
                        case "if":
                            parseIf(line, out, reader);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected reserved word " + getReservedWord(line) + " at line " + lineCounter + ": " + line);
                    }
                } else if(line.matches("\\d+ => \\{")) {
                    out.put(line.split(" ")[0], readUntil(reader, "}"));
                } else if(line.matches("\\w+\\[\"?[\\w\\d ]+\"?] => \"?[\\w\\d ]+\"?")) {
                    parseArray(line, out);
                } else {
                    if(startWithType(line)) {
                        String key = line.split("\"")[1];

                        out.put(key, line.split(" ")[0]);
                        properties.put(key, line.split(" ")[0]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    private void parseArray(String line, LinkedHashMap<String, Object> parentList) {
        int chars = countChar(line, '"');
        String type = line.split("\\[")[0];
        if(!isVariableType(type)) throw new IllegalStateException("Type is not a valid type! Line " + lineCounter + ": " + line);

        if(chars == 4) {
            String var = line.split("\"")[1];
            if(!properties.containsKey(var)) throw new IllegalStateException("Variable " + var + " is not declared! Line " + lineCounter + ": " + line);

            String key = line.split("\"")[3];
            parentList.put(key, "*array_" + type + "_" + var);
        } else if(chars == 2) {
            String var = line.split("]")[0].split("\\[")[1];
            if(!isVariableType(var)) throw new IllegalStateException("Type (for length) is not a valid type! Line " + lineCounter + ": " + line);

            String key = line.split("\"")[1];
            parentList.put(key, "*array_" + type + "_" + var);
        } else throw new IllegalStateException("Invalid quotes at line " + lineCounter + ": " + line);
    }

    private void parseIf(String line, LinkedHashMap<String, Object> parentList, BufferedReader reader) {
        String name = "_if";

        if(line.matches("if( not)? \"[\\w\\d ]+\" \\{")) {
            String variable = line.split("\"")[1];
            if(!properties.get(variable).equals("boolean")) throw new IllegalStateException(variable + " must be a boolean! Line " + lineCounter + ": " + line);

            name += "_" + variable + "_equals_" + ((line.contains("not") ? "false" : "true"));
            parentList.put(name, readUntil(reader, "}"));
        } else if(line.matches("if \"[\\w\\d ]+\" [=><]{1,2} \\d+ \\{")) {
            String comparingType = line.split("if \"[\\w\\d ]+\"")[1].trim().split(" ")[0];
            if(!isComparingTypeValid(comparingType)) throw new IllegalStateException("Illegal comparing " + comparingType + " at line " + lineCounter + ": " + line);

            String variable = line.split("\"")[1];
            if(!properties.containsKey(variable)) throw new IllegalStateException("Variable " + variable + " is not declared! Line " + lineCounter + ": " + line);
            if(!isNumber((String) properties.get(variable))) throw new IllegalStateException("Variable " + variable + " must be a number! Line " + lineCounter + ": " + line);

            String number = line.split("if \"[\\w\\d ]+\" [=><]{1,2}")[1].trim().split(" ")[0];

            name += "_" + variable + "_" + comparingType + "_" + number;
            parentList.put(name, readUntil(reader, "}"));
        } else throw new IllegalStateException("Malformed condition at line " + lineCounter + ": " + line);
    }

    private boolean isNumber(String type) {
        return type.equals("int")
                || type.equals("angle")
                || type.equals("byte")
                || type.equals("short")
                || type.equals("long")
                || type.equals("float")
                || type.equals("double")
                || type.equals("varint")
                || type.equals("varlong");
    }

    private boolean isComparingTypeValid(String comparingType) {
        return comparingType.equals("==")
                || comparingType.equals("<")
                || comparingType.equals(">")
                || comparingType.equals("<=")
                || comparingType.equals(">=");
    }

    private int countChar(String str, char c) {
        int count = 0;

        for(int i=0; i < str.length(); i++) {
            if(str.charAt(i) == c) count++;
        }

        return count;
    }

    private boolean startWithReservedWord(String line) {
        for(String reservedWord : reservedWords) {
            if(line.startsWith(reservedWord)) return true;
        }

        return false;
    }

    private boolean startWithType(String line) {
        for(String type : types) {
            if(line.startsWith(type)) return true;
        }

        return false;
    }

    private String getReservedWord(String line) {
        return line.split(" ")[0];
    }

    private boolean isReservedWord(String word) {
        for(String reservedWord : reservedWords) {
            if(reservedWord.equals(word)) return true;
        }

        return false;
    }

    private boolean isVariableType(String word) {
        for(String type : types) {
            if(type.equals(word)) return true;
        }

        return false;
    }

    private String getDataType(String key, LinkedHashMap<String, Object> list) {
        if(list.containsKey(key)) return (String) list.get(key);

        for(Map.Entry<String, Object> property : list.entrySet()) {
            if(property.getValue() instanceof LinkedHashMap) {
                String out = getDataType(key, (LinkedHashMap<String, Object>) property.getValue());
                if(out != null) return out;
            }
        }

        return null;
    }

    private Object getData(String type, DataReader reader) {
        switch(type) {
            case "boolean": return reader.readBoolean();
            case "angle":
            case "byte": return reader.readByte();
            case "short": return reader.readShort();
            case "int": return reader.readInt();
            case "long": return reader.readLong();
            case "float": return reader.readFloat();
            case "double": return reader.readDouble();
            //case "string": return reader.readString(reader.readShort());
            //case "chat": return reader.readJSON();
            //case "identifier": return reader.readString(reader.readShort());
            case "varint": return reader.readVarInt();
            case "varlong": return reader.readVarLong();
            case "nbt": return reader.readNBT();
            //case "position": return reader.readPosition();
            case "uuid": return reader.readUUID();
        }

        return null;
    }

    private void writeValue(DataWriter writer, Object object, String type) {
        switch(type) {
            case "boolean": writer.writeBoolean((Boolean) object);
            case "angle":
            case "byte": writer.writeByte((Integer) object); break;
            case "short": writer.writeShort((Integer) object); break;
            case "int": writer.writeInt((Integer) object); break;
            case "long": writer.writeLong((Long) object); break;
            case "float": writer.writeFloat((Float) object); break;
            case "double": writer.writeDouble((Double) object); break;
            //case "string": writer.writeString(writer.writeShort()); break;
            //case "chat": writer.writeJSON(); break;
            //case "identifier": writer.writeString(writer.writeShort()); break;
            case "varint": writer.writeVarInt((Integer) object); break;
            case "varlong": writer.writeVarLong((Long) object); break;
            case "nbt": writer.writeNBT((NBTCompound) object); break;
            //case "position": writer.writePosition(); break;
            case "uuid": writer.writeUUID((UUID) object); break;
        }
    }
}
