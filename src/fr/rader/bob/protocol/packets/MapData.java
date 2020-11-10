package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class MapData implements Packet {

    private byte packetID;
    private int timestamp;
    private int size;

    private int mapID;
    private int scale;
    private boolean trackingPosition;
    private boolean locked;
    private int iconCount;
    private Icon[] icons;
    private int columns;
    private int rows;
    private int x;
    private int z;
    private int length;
    private byte[] data;

    public MapData(byte id, int timestamp, int size, byte[] rawData) {
        this.packetID = id;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        mapID = reader.readVarInt();
        scale = reader.readByte();
        trackingPosition = reader.readBoolean();
        locked = reader.readBoolean();
        iconCount = reader.readByte();

        icons = new Icon[iconCount];
        for(int i = 0; i < iconCount; i++) {
            int type = reader.readVarInt();
            int x = reader.readByte();
            int z = reader.readByte();
            int direction = reader.readByte();
            boolean hasDisplayName = reader.readBoolean();

            String displayName = null;
            if(hasDisplayName)
                displayName = reader.readChat();

            icons[i] = new Icon(type, x, z, direction, hasDisplayName, displayName);
        }

        columns = reader.readByte();
        if(columns > 0) {
            rows = reader.readByte();
            x = reader.readByte();
            z = reader.readByte();
            length = reader.readByte();
            data = reader.readFollowingBytes(length);
        }
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeInt(packetID);

        writer.writeVarInt(mapID);
        writer.writeByte(scale);
        writer.writeBoolean(trackingPosition);
        writer.writeBoolean(locked);
        writer.writeVarInt(iconCount);

        for(Icon icon : icons) writer.writeByteArray(icon.toBytes());

        writer.writeByte(columns);
        if(columns > 0) {
            writer.writeByte(rows);
            writer.writeByte(x);
            writer.writeByte(z);
            writer.writeByte(length);
            writer.writeByteArray(data);
        }

        return writer.getData();
    }

    @Override
    public int getLength() {
        return writePacket().length;
    }

    @Override
    public byte getPacketID() {
        return packetID;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public static class Icon {

        private int type;
        private int x;
        private int z;
        private int direction;
        private boolean hasDisplayName;
        private String displayName;

        public Icon(int type, int x, int z, int direction, boolean hasDisplayName, String displayName) {
            this.type = type;
            this.x = x;
            this.z = z;
            this.direction = direction;
            this.hasDisplayName = hasDisplayName;
            this.displayName = displayName;
        }

        public byte[] toBytes() {
            DataWriter writer = new DataWriter();

            writer.writeVarInt(type);
            writer.writeByte(x);
            writer.writeByte(z);
            writer.writeByte(direction);
            writer.writeBoolean(hasDisplayName);
            if(hasDisplayName) writer.writeChat(displayName);

            return writer.getData();
        }

        public int getType() {
            return type;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        public int getDirection() {
            return direction;
        }

        public boolean isHasDisplayName() {
            return hasDisplayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
