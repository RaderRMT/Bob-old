package fr.rader.bob.packets;

import fr.rader.bob.DataReader;
import fr.rader.bob.Packet;
import fr.rader.bob.PacketHeader;

public class P0DServerDifficulty extends Packet {

    private int difficulty;
    private boolean difficultyLocked;

    public P0DServerDifficulty(PacketHeader header, byte[] rawData) {
        super(header, rawData);

        readData(rawData);
    }

    private void readData(byte[] rawData) {
        DataReader reader = new DataReader(rawData);
        reader.startAt(1);

        difficulty = reader.readByte();
        difficultyLocked = reader.readBoolean();
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isDifficultyLocked() {
        return difficultyLocked;
    }

    public void setDifficultyLocked(boolean difficultyLocked) {
        this.difficultyLocked = difficultyLocked;
    }
}
