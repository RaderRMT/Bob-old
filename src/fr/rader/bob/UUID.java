package fr.rader.bob;

public class UUID {

    private byte[] rawUUID;
    private int uuidVersion;

    public UUID(byte[] uuid) {
        rawUUID = uuid;
        uuidVersion = (uuid[4] & 0x30) >> 4;
    }

    public byte[] getRawUUID() {
        return rawUUID;
    }

    public void setRawUUID(byte[] rawUUID) {
        this.rawUUID = rawUUID;
    }

    public int getUUIDVersion() {
        return uuidVersion;
    }

    public void setUUIDVersion(int uuidVersion) {
        this.uuidVersion = uuidVersion;
    }
}
