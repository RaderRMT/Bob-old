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
import java.util.*;

public class PacketReader {

    private final int packetID;

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

    private LinkedHashMap<String, Object> properties;
    private LinkedHashMap<String, Object> tempProperties;

    private int lineCounter = 0;

    public PacketReader(int packetID) {
        this.packetID = packetID;
        properties = new LinkedHashMap<>();

        readProperties();
    }

    public byte[] serializePacketMap(LinkedHashMap<String, Object> map) {
        DataWriter writer = new DataWriter();

        serializeMap(map, writer);

        return writer.getData();
    }

    private void serializeMap(LinkedHashMap<String, Object> map, DataWriter writer) {
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getValue() instanceof LinkedHashMap) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, Object> entryMap = (LinkedHashMap<String, Object>) entry.getValue();
                serializeMap(entryMap, writer);
                continue;
            }

            if(entry.getValue() instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> entryList = (List<Object>) entry.getValue();
                serializeList(entryList, writer, entry.getKey());
                continue;
            }

            writeValue(writer, entry.getValue(), getDataType(entry.getKey(), properties));
        }
    }

    private void serializeList(List<Object> entryList, DataWriter writer, String key) {
        for(Object value : entryList) {
            writeValue(writer, value, Objects.requireNonNull(getDataType(key, properties)));
        }
    }

    public LinkedHashMap<String, Object> readPacket(Packet packet) {
        DataReader reader = new DataReader(packet.getRawData());
        tempProperties = new LinkedHashMap<>();

        LinkedHashMap<String, Object> map = readMap(properties, reader);

        tempProperties = null;
        return map;
    }

    private LinkedHashMap<String, Object> readMap(LinkedHashMap<String, Object> packetProperties, DataReader reader) {
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();

        for(Map.Entry<String, Object> entry : packetProperties.entrySet()) {
            String variable = entry.getKey();

            if(entry.getValue() instanceof LinkedHashMap) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, Object> value = (LinkedHashMap<String, Object>) entry.getValue();

                String associatedVariable = variable.split("_")[2];
                switch(variable.split("_")[1]) {
                    case "array":
                        LinkedHashMap<String, Object> arrayData = new LinkedHashMap<>();

                        int length = (int) tempProperties.get(associatedVariable);
                        for(int i = 0; i < length; i++) {
                            arrayData.put(String.valueOf(i), readMap(value, reader));
                        }

                        out.put(variable, arrayData);
                        break;
                    case "match":
                        String index = String.valueOf(tempProperties.get(variable.split("_")[2]));
                        @SuppressWarnings("unchecked")
                        LinkedHashMap<String, Object> match = (LinkedHashMap<String, Object>) value.get(index);
                        if(match == null) break;

                        out.put(variable, readMap(match, reader));
                        break;
                    case "if":
                        String comparison = variable.split("_")[3];
                        String mustMatch = variable.split("_")[4];
                        if(getDataType(associatedVariable, properties).equals("boolean")) {
                            if((boolean) tempProperties.get(associatedVariable) == Boolean.parseBoolean(mustMatch)) {
                                out.put(variable, readMap(value, reader));
                            }
                        } else {
                            int variableValue = (int) tempProperties.get(associatedVariable);
                            int toMatch = Integer.parseInt(mustMatch);

                            switch(comparison) {
                                case "==":
                                    if(variableValue == toMatch) out.put(variable, readMap(value, reader));
                                    break;
                                case "<":
                                    if(variableValue < toMatch) out.put(variable, readMap(value, reader));
                                    break;
                                case ">":
                                    if(variableValue > toMatch) out.put(variable, readMap(value, reader));
                                    break;
                                case "<=":
                                    if(variableValue <= toMatch) out.put(variable, readMap(value, reader));
                                    break;
                                case ">=":
                                    if(variableValue >= toMatch) out.put(variable, readMap(value, reader));
                                    break;
                            }
                        }

                        break;
                }

                continue;
            }

            if(variable.startsWith("_array")) {
                String associatedVariable = variable.split("_")[2];
                String type = (String) entry.getValue();

                List<Object> arrayData = new ArrayList<>();
                int length = (int) tempProperties.get(associatedVariable);
                for(int i = 0; i < length; i++) {
                    arrayData.add(getData(type, reader));
                }

                out.put(variable, arrayData);
                continue;
            }

            String type = (String) entry.getValue();
            Object value = getData(type, reader);

            out.put(variable, value);
            tempProperties.put(variable, value);
        }

        return out;
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

    private void readProperties() {
        InputStream inputStream = Main.class.getResourceAsStream("/" + Main.getInstance().getReplayData().getMetaData("mcversion") + "/packetData.bob");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        try {
            String line;
            while((line = reader.readLine()) != null) {
                lineCounter++;

                if(line.startsWith("//")) continue;
                if(line.isEmpty()) continue;

                if(line.matches("^[0-9A-F]{2}: ?\\{") && line.startsWith(String.format("%1$02X", packetID))) {
                    properties = readUntil(reader, "}");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
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

                line = line.trim();
                if(line.startsWith("//")) continue;
                if(line.contains("//")) line = line.split("//")[0].trim();
                if(line.isEmpty()) continue;
                if(line.contains(endCharacter)) return out;

                if(startWithReservedWord(line)) {
                    String associatedVariable = line.split("\"")[1];

                    switch(line.split(" ")[0]) {
                        case "match":
                            out.put("_match_" + associatedVariable, readMatch(reader));
                            break;
                        case "array":
                            out.put("_array_" + associatedVariable, readUntil(reader, "]"));
                            break;
                        case "if":
                            String name;
                            if(line.matches("if( not)? \"[\\w\\d ]+\" \\{")) {
                                String variable = line.split("\"")[1];
                                if(!properties.containsKey(variable)) stop("No variable named \"" + variable + "\" exist!");
                                if(!properties.get(variable).equals("boolean")) stop("\"" + variable + "\"'s type is not 'boolean' ('" + properties.get(variable) + "' instead)");

                                name = "_if_" + variable + "_equals_" + ((line.contains("not") ? "false" : "true"));
                                out.put(name, readUntil(reader, "}"));
                                break;
                            }

                            if(line.matches("if \"[\\w\\d ]+\" [=><]{1,2} \\d+ \\{")) {
                                String comparingType = line.split("if \"[\\w\\d ]+\"")[1].trim().split(" ")[0];
                                if(!isComparingTypeValid(comparingType)) stop(comparingType + " is not a valid comparing type!");

                                String variable = line.split("\"")[1];
                                if(!properties.containsKey(variable)) stop("No variable named \"" + variable + "\" exist!");
                                if(!isNumber((String) properties.get(variable))) stop("\"" + variable + "\" is not a number ('" + properties.get(variable) + "' instead)");

                                String number = line.split("if \"[\\w\\d ]+\" [=><]{1,2}")[1].trim().split(" ")[0];
                                name = "_if_" + variable + "_" + comparingType + "_" + number;
                                out.put(name, readUntil(reader, "}"));
                                break;
                            }

                            stop("Could not parse 'if': " + line);
                            break;
                    }

                    continue;
                }

                if(line.matches("\\w+\\[\"[\\w\\d ]+\"] => \"[\\w\\d ]+\"")) {
                    String type = line.split("\\[")[0];
                    String associatedVariable = line.split("\"")[1];
                    String name = line.split("\"")[3];

                    out.put("_array_" + associatedVariable + "_" + name, type);
                    continue;
                }

                if(startWithType(line)) {
                    String variable = line.split("\"")[1];
                    out.put(variable, line.split(" ")[0]);
                    properties.put(variable, line.split(" ")[0]);
                }

                //System.out.println("Skipping bad line at line " + lineCounter + ": " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    private LinkedHashMap<String, Object> readMatch(BufferedReader reader) {
        LinkedHashMap<String, Object> out = new LinkedHashMap<>();

        try {
            String line;
            while((line = reader.readLine()) != null) {
                lineCounter++;

                if(line.contains("}")) return out;

                line = line.trim();
                if(line.startsWith("//")) continue;
                if(line.contains("//")) line = line.split("//")[0].trim();
                if(line.isEmpty()) continue;

                if(line.matches("\\d+ => \\{")) {
                    out.put(line.split(" ")[0], readUntil(reader, "}"));
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
            default:
                System.out.println("getData: unknown type " + type); break;
        }

        return null;
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
            default:
                System.out.println("writeValue: unknown type " + type); break;
        }
    }

    private void stop(String message) {
        System.out.println("Error at line " + lineCounter + ": " + message);
        System.exit(-1);
    }
}
