package fr.rader.bob.protocol;

import fr.rader.bob.protocol.versions.*;

public class Version {

    public static Packet getPacket(int id, int timestamp, int size, byte[] data, ProtocolVersion current) {
        if (ProtocolVersion.v1_7_6.equals(current)) {
            return Version5.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_8.equals(current)) {
            return Version47.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_9.equals(current)) {
            return Version107.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_9_1.equals(current)) {
            return Version108.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_9_2.equals(current)) {
            return Version109.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_9_3.equals(current)) {
            return Version110.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_10.equals(current)) {
            return Version210.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_11.equals(current)) {
            return Version315.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_11_1.equals(current)) {
            return Version316.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_12.equals(current)) {
            return Version335.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_12_1.equals(current)) {
            return Version338.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_12_2.equals(current)) {
            return Version340.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_14.equals(current)) {
            return Version477.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_14_1.equals(current)) {
            return Version480.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_14_2.equals(current)) {
            return Version485.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_14_3.equals(current)) {
            return Version490.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_14_4.equals(current)) {
            return Version498.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_15.equals(current)) {
            return Version573.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_15_1.equals(current)) {
            return Version575.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_15_2.equals(current)) {
            return Version578.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_16.equals(current)) {
            return Version735.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_16_1.equals(current)) {
            return Version736.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_16_2.equals(current)) {
            return Version751.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_16_3.equals(current)) {
            return Version753.getPacket(id, timestamp, size, data);
        } else if (ProtocolVersion.v1_16_4.equals(current)) {
            return Version754.getPacket(id, timestamp, size, data);
        }

        return null;
    }
}
