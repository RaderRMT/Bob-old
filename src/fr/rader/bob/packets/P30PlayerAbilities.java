package fr.rader.bob.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.Packet;
import fr.rader.bob.PacketHeader;

public class P30PlayerAbilities extends Packet {

    private int flags;
    private float flyingSpeed;
    private float fovModifier;

    public P30PlayerAbilities(PacketHeader header, byte[] rawData) {
        super(header, rawData);

        readData(rawData);
    }

    private void readData(byte[] rawData) {
        DataReader reader = new DataReader(rawData);
        reader.startAt(1);

        flags = reader.readByte();
        flyingSpeed = reader.readFloat();
        fovModifier = reader.readFloat();
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public float getFlyingSpeed() {
        return flyingSpeed;
    }

    public void setFlyingSpeed(float flyingSpeed) {
        this.flyingSpeed = flyingSpeed;
    }

    public float getFovModifier() {
        return fovModifier;
    }

    public void setFovModifier(float fovModifier) {
        this.fovModifier = fovModifier;
    }
}
