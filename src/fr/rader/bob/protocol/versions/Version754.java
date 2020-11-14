package fr.rader.bob.protocol.versions;

import fr.rader.bob.protocol.Packet;
import fr.rader.bob.protocol.packets.*;

public class Version754 {

    public static Packet getPacket(int id, int timestamp, int size, byte[] data) {
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
            case 0x21:
                return new Effect(id, timestamp, size, data);
            case 0x22:
                return new Particle(id, timestamp, size, data);
            case 0x23:
                return new UpdateLight(id, timestamp, size, data);
            case 0x24:
                return new JoinGame(id, timestamp, size, data);
            case 0x25:
                return new MapData(id, timestamp, size, data);
            case 0x26:
                return new TradeList(id, timestamp, size, data);
            case 0x27:
                return new EntityPosition(id, timestamp, size, data);
            case 0x28:
                return new EntityPositionRotation(id, timestamp, size, data);
            case 0x29:
                return new EntityRotation(id, timestamp, size, data);
            case 0x2a:
                return new EntityMovement(id, timestamp, size, data);
            case 0x2b:
                return new VehicleMove(id, timestamp, size, data);
            case 0x2c:
                return new OpenBook(id, timestamp, size, data);
            case 0x2d:
                return new OpenWindow(id, timestamp, size, data);
            case 0x2e:
                return new OpenSignEditor(id, timestamp, size, data);
            case 0x2f:
                return new CraftRecipeResponse(id, timestamp, size, data);
            case 0x30:
                return new PlayerAbilities(id, timestamp, size, data);
            case 0x31:
                return new CombatEvent(id, timestamp, size, data);
            case 0x32:
                return new PlayerInfo(id, timestamp, size, data);
            case 0x33:
                return new FacePlayer(id, timestamp, size, data);
            case 0x34:
                return new PlayerPositionAndLook(id, timestamp, size, data);
            case 0x35:
                return new UnlockRecipes(id, timestamp, size, data);
            case 0x36:
                return new DestroyEntities(id, timestamp, size, data);
            case 0x37:
                return new RemoveEntityEffect(id, timestamp, size, data);
            case 0x38:
                return new ResourcePackSend(id, timestamp, size, data);
            case 0x39:
                return new Respawn(id, timestamp, size, data);
            case 0x3a:
                return new EntityHeadLook(id, timestamp, size, data);
            case 0x3b:
                return new MultiBlockChange(id, timestamp, size, data);
            case 0x3c:
                return new SelectAdvancementTab(id, timestamp, size, data);
            case 0x3d:
                return new WorldBorder(id, timestamp, size, data);
            case 0x3e:
                return new Camera(id, timestamp, size, data);
            case 0x3f:
                return new HeldItemChange(id, timestamp, size, data);
            case 0x40:
                return new UpdateViewPosition(id, timestamp, size, data);
            case 0x41:
                return new UpdateViewDistance(id, timestamp, size, data);
            case 0x42:
                return new SpawnPosition(id, timestamp, size, data);
            case 0x43:
                return new DisplayScoreboard(id, timestamp, size, data);
            case 0x44:
                return new EntityMetadata(id, timestamp, size, data);
            case 0x45:
                return new AttachEntity(id, timestamp, size, data);
            case 0x46:
                return new EntityVelocity(id, timestamp, size, data);
            case 0x47:
                return new EntityEquipment(id, timestamp, size, data);
            case 0x48:
                return new SetExperience(id, timestamp, size, data);
            case 0x49:
                return new UpdateHealth(id, timestamp, size, data);
            case 0x4a:
                return new ScoreboardObjective(id, timestamp, size, data);
            case 0x4b:
                return new SetPassengers(id, timestamp, size, data);
            case 0x4c:
                return new Teams(id, timestamp, size, data);
            case 0x4d:
                return new UpdateScore(id, timestamp, size, data);
            case 0x4e:
                return new TimeUpdate(id, timestamp, size, data);
            case 0x4f:
                return new Title(id, timestamp, size, data);
            case 0x50:
                return new EntitySoundEffect(id, timestamp, size, data);
            case 0x51:
                return new SoundEffect(id, timestamp, size, data);
            case 0x52:
                return new StopSound(id, timestamp, size, data);
            case 0x53:
                return new PlayerListHeaderFooter(id, timestamp, size, data);
            case 0x54:
                return new NBTQueryResponse(id, timestamp, size, data);
            case 0x55:
                return new CollectItem(id, timestamp, size, data);
            case 0x56:
                return new EntityTeleport(id, timestamp, size, data);
            case 0x57:
                return new Advancements(id, timestamp, size, data);
            case 0x58:
                return new EntityProperties(id, timestamp, size, data);
            case 0x59:
                return new EntityEffect(id, timestamp, size, data);
            case 0x5a:
                return new DeclareRecipes(id, timestamp, size, data);
            case 0x5b:
                return new Tags(id, timestamp, size, data);
        }

        return null;
    }
}
