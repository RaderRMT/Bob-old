package fr.rader.bob.packet;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.Main;
import fr.rader.bob.nbt.NBTCompound;
import fr.rader.bob.types.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class PacketReader {

    private LinkedHashMap<String, Object> properties;
    private LinkedHashMap<String, Object> tempProperties;

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
            "string",
            "chat",
            "identifier",
            "varint",
            "varlong",
            "nbt",
            "position",
            "uuid"
    };

    private int lineCounter = 0;
    private int packetID;

    public PacketReader(int packetID) {
        this.packetID = packetID;
        properties = new LinkedHashMap<>();

        readProperties(packetID);

        System.out.println(properties);
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

    public Packet deserializePacket(byte[] rawData) {
        DataReader reader = new DataReader(rawData);
        Packet packet = new Packet(packetID);
        tempProperties = new LinkedHashMap<>();

        packet.setProperties(readMap(properties, reader));

        tempProperties = null;
        return packet;
    }

    private LinkedHashMap<String, Object> readMap(LinkedHashMap<String, Object> properties, DataReader reader) {
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();

        for(Map.Entry<String, Object> property : properties.entrySet()) {
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
        if(isType(varName)) {
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
        InputStream inputStream = Main.class.getResourceAsStream("/1.16.4/packetData.bob");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        try {
            String line;
            while((line = reader.readLine()) != null) {
                lineCounter++;

                // remove comments
                line = line.split("//")[0].trim();

                // we continue if line is empty
                if(line.isEmpty()) continue;

                // if we're starting to read a packet data
                if(line.matches("[0-9A-F]+: ?\\{") && line.startsWith(String.format("%1$02X", packetID))) {
                    properties = readUntil(reader, "}");
                    break;
                }
            }

            reader.close();
            inputStreamReader.close();
            inputStream.close();
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

                // remove comments
                line = line.split("//")[0].trim();

                // we continue if line is empty
                if(line.isEmpty()) continue;

                if(line.contains(endCharacter)) return out;

                if(line.contains("*") || line.contains("_")) stop(line);

                if(startWithReservedWord(line)) {
                    String key;
                    if(line.contains("\"")) {
                        key = line.split("\"")[1];
                        if(!properties.containsKey(key)) stop(line);
                    } else {
                        key = line.substring(6, line.length() - 2);
                        if(!isType(key)) stop(line);
                    }

                    switch(line.split(" ")[0]) { // reserved word
                        case "array":
                            out.put("_array_" + key, readUntil(reader, "]"));
                            break;
                        case "match":
                            out.put("_match_" + key, readUntil(reader, "}"));
                            break;
                        case "if":
                            String name;

                            if(line.matches("if( not)? \"[\\w\\d ]+\" \\{")) {
                                String variable = line.split("\"")[1];
                                if(!properties.get(variable).equals("boolean")) stop(line);

                                name = "_if_" + variable + "_equals_" + ((line.contains("not") ? "false" : "true"));
                                out.put(name, readUntil(reader, "}"));
                            } else if(line.matches("if \"[\\w\\d ]+\" [=><]{1,2} \\d+ \\{")) {
                                String comparingType = line.split("if \"[\\w\\d ]+\"")[1].trim().split(" ")[0];
                                if(!isComparingTypeValid(comparingType)) stop(line);

                                String variable = line.split("\"")[1];
                                if(!properties.containsKey(variable)
                                        || !isNumber((String) properties.get(variable))) stop(line);

                                String number = line.split("if \"[\\w\\d ]+\" [=><]{1,2}")[1].trim().split(" ")[0];
                                name = "_if_" + variable + "_" + comparingType + "_" + number;
                                out.put(name, readUntil(reader, "}"));
                            } else stop(line);
                            break;
                        default:
                            stop(line);
                    }
                } else if(line.matches("\\d+ => \\{")) { // one line array
                    out.put(line.split(" ")[0], readUntil(reader, "}"));
                } else if(line.matches("\\w+\\[\"?[\\w\\d ]+\"?] => \"?[\\w\\d ]+\"?")) { // match conditions
                    String type = line.split("\\[")[0];
                    if(!isType(type)) stop(line);

                    String var;
                    String key;
                    switch(countChar(line, '"')) {
                        case 2:
                            var = line.split("\"")[1];
                            if(!properties.containsKey(var)) stop(line);

                            key = line.split("\"")[3];
                            out.put(key, "*array_" + type + "_" + var);
                            break;
                        case 4:
                            var = line.split("]")[0].split("\\[")[1];
                            if(!isType(var)) stop(line);

                            key = line.split("\"")[1];
                            out.put(key, "*array_" + type + "_" + var);
                            break;
                        default:
                            stop(line);
                    }
                } else {
                    if(startWithType(line)) {
                        String key = line.split("\"")[1];
                        out.put(key, line.split(" ")[0]);
                        properties.put(key, line.split(" ")[0]);
                    } else System.out.println("Skipping line " + lineCounter + " (bad): " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    private boolean startWithType(String line) {
        for(String type : types) {
            if(line.startsWith(type)) return true;
        }

        return false;
    }

    private boolean startWithReservedWord(String line) {
        for(String reservedWord : reservedWords) {
            if(line.startsWith(reservedWord)) return true;
        }

        return false;
    }

    private boolean isType(String string) {
        for(String type : types) {
            if(string.equals(type)) return true;
        }

        return false;
    }

    private int countChar(String str, char c) {
        int count = 0;

        for(int i=0; i < str.length(); i++) {
            if(str.charAt(i) == c) count++;
        }

        return count;
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
            case "chat":
            case "identifier":
            case "string": return reader.readString(reader.readVarInt());
            case "varint": return reader.readVarInt();
            case "varlong": return reader.readVarLong();
            case "nbt": return reader.readNBT();
            case "position": return reader.readPosition();
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
        }
    }

    private void stop(String line) {
        throw new IllegalStateException("Error at line " + lineCounter + ": " + line);
    }

    public LinkedHashMap<String, Object> getProperties() {
        return properties;
    }
}
