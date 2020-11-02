package fr.rader.bob;

public class UUID {

    private byte[] rawUUID;
    private int uuidVer;

    public UUID(byte[] uuid) {
        rawUUID = uuid;
        uuidVer = (uuid[4] & 0x30) >> 4;
    }

    public byte[] getRawUUID() {
        return rawUUID;
    }

    public void setRawUUID(byte[] rawUUID) {
        this.rawUUID = rawUUID;
    }

    public int getUuidVer() {
        return uuidVer;
    }

    public void setUuidVer(int uuidVer) {
        this.uuidVer = uuidVer;
    }
}
