package fr.rader.bob.protocol.versions;

import fr.rader.bob.protocol.Packet;
import fr.rader.bob.protocol.packets.*;

public class Version754 {

    public static Packet getPacket(byte id, int timestamp, int size, byte[] data) {
        switch(id) {
            case 0x00:
                return new SpawnEntity(id, timestamp, size, data);
            case 0x01:
                return new SpawnExperienceOrb(id, timestamp, size, data);
            case 0x02:
                return new SpawnLivingEntity(id, timestamp, size, data);
            case 0x03:
                return new SpawnPainting(id, timestamp, size, data);
            case 0x04:
                return new SpawnPlayer(id, timestamp, size, data);
            case 0x05:
                return new EntityAnimation(id, timestamp, size, data);
            case 0x06:
                return new Statistics(id, timestamp, size, data);
            case 0x07:
                return new AcknowledgePlayerDigging(id, timestamp, size, data);
            case 0x08:
                return new BlockBreakAnimation(id, timestamp, size, data);
            case 0x09:
                return new BlockEntityData(id, timestamp, size, data);
            case 0x0a:
                return new BlockAction(id, timestamp, size, data);
            case 0x0b:
                return new BlockChange(id, timestamp, size, data);
            case 0x0c:
                return new BossBar(id, timestamp, size, data);
            case 0x0d:
                return new ServerDifficulty(id, timestamp, size, data);
            case 0x0e:
                return new ChatMessage(id, timestamp, size, data);
            case 0x0f:
                return new TabComplete(id, timestamp, size, data);
            case 0x10:
                return new DeclareCommands(id, timestamp, size, data);
            case 0x11:
                return new WindowConfirmation(id, timestamp, size, data);
            case 0x12:
                return new CloseWindow(id, timestamp, size, data);
            case 0x13:
                return new WindowItems(id, timestamp, size, data);
            case 0x14:
                return new WindowProperty(id, timestamp, size, data);
            case 0x15:
                return new SetSlot(id, timestamp, size, data);
            case 0x16:
                return new SetCooldown(id, timestamp, size, data);
            case 0x17:
                return new PluginMessage(id, timestamp, size, data);
            case 0x18:
                return new NamedSoundEffect(id, timestamp, size, data);
            case 0x19:
                return new Disconnect(id, timestamp, size, data);
            case 0x1a:
                return new EntityStatus(id, timestamp, size, data);
            case 0x1b:
                return new Explosion(id, timestamp, size, data);
            case 0x1c:
                return new UnloadChunk(id, timestamp, size, data);
            case 0x1d:
                return new ChangeGameState(id, timestamp, size, data);
            case 0x1e:
                return new OpenHorseWindow(id, timestamp, size, data);
            case 0x1f:
                return new KeepAlive(id, timestamp, size, data);
            case 0x20:
                return new ChunkData(id, timestamp, size, data);
        }

        return null;
    }
}
