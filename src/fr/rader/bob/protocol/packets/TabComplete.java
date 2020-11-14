package fr.rader.bob.protocol.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.DataWriter;
import fr.rader.bob.protocol.Packet;

public class TabComplete implements Packet {

    private int packetID;
    private int timestamp;
    private int size;

    private int id;
    private int start;
    private int length;
    private int count;

    private Match[] matches;

    public TabComplete(int packetID, int timestamp, int size, byte[] rawData) {
        this.packetID = packetID;
        this.timestamp = timestamp;
        this.size = size;

        DataReader reader = new DataReader(rawData);

        id = reader.readVarInt();
        start = reader.readVarInt();
        length = reader.readVarInt();
        count = reader.readVarInt();

        matches = new Match[count];
        for(int i = 0; i < count; i++) {
            String match = reader.readString(reader.readVarInt());
            boolean hasTooltip = reader.readBoolean();
            String tooltip = null;

            if(hasTooltip) tooltip = reader.readChat();

            matches[i] = new Match(match, hasTooltip, tooltip);
        }
    }

    @Override
    public byte[] writePacket() {
        DataWriter writer = new DataWriter();

        writer.writeInt(timestamp);
        writer.writeInt(size);
        writer.writeVarInt(packetID);

        writer.writeVarInt(id);
        writer.writeVarInt(start);
        writer.writeVarInt(length);
        writer.writeVarInt(count);

        for(Match match : matches) {
            writer.writeVarInt(match.match.length());
            writer.writeString(match.match);
            writer.writeBoolean(match.hasTooltip);

            if(match.hasTooltip) writer.writeChat(match.tooltip);
        }

        return writer.getData();
    }

    @Override
    public int getLength() {
        return writePacket().length;
    }

    @Override
    public int getPacketID() {
        return packetID;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Match[] getMatches() {
        return matches;
    }

    public void setMatches(Match[] matches) {
        this.matches = matches;
    }

    private class Match {

        private String match;
        private boolean hasTooltip;
        private String tooltip;

        public Match(String match, boolean hasTooltip, String tooltip) {
            this.match = match;
            this.hasTooltip = hasTooltip;
            this.tooltip = tooltip;
        }
    }
}