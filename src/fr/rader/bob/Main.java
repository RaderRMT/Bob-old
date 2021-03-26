package fr.rader.bob;

import fr.rader.bob.guis.MainInterface;
import fr.rader.bob.guis.ProjectSelector;
import fr.rader.bob.utils.IO;
import fr.rader.bob.utils.OS;

import javax.swing.*;
import java.io.File;

public class Main {

    private static Main instance;

    private ReplayData replayData;
    private BobSettings settings;
    private Projects projects;

    public static Main getInstance() {
        return instance;
    }

    public void start() {
        settings = new BobSettings();
        projects = new Projects();

        String projectName = settings.getProperty("lastProject");
        if(!projects.getProjectsNames().contains(projectName)) {
            settings.setProperty("lastProject", "");
            settings.saveSettings();
            projectName = "";
        }

        if(projectName.isEmpty()) {
            ProjectSelector selector = new ProjectSelector();
            projectName = selector.createWindow();

            // we quit the program if the file is null (if we closed the window)
            if(projectName == null) return;

            settings.setProperty("lastProject", projectName);
            settings.saveSettings();
        }

        File project = new File(BobSettings.getWorkingDirectory() + "/projects/" + projectName);

        replayData = new ReplayData(project);
        MainInterface mainInterface = new MainInterface();
        mainInterface.createWindow();

        /*try {
            DataReader reader = new DataReader(replayData.getReplayZip().getEntry("recording.tmcpr"));
            DataWriter writer = new DataWriter();

            while(reader.getLength() != 0) {
                int timestamp = reader.readInt();
                int size = reader.readInt();
                int packetID = reader.readVarInt();

                writer.writeInt(timestamp);

                if(packetID == 0x09) {
                    PacketReader packetReader = new PacketReader(0x09);
                    Packet packet = new Packet(reader.readFollowingBytes(size - 1), 0x09);

                    ArrayList<PacketBase> bases = packetReader.deserializePacket(packet);
                    NBTEditor editor = new NBTEditor();
                    editor.invokeEditor(((NBTCompound) bases.get(2).getAsPacketVariable().getValue()));

                    bases.get(2).getAsPacketVariable().setValue(editor.getSerializedTree());

                    packet = packetReader.serializePacket(bases);

                    writer.writeInt(packet.getRawData().length + 1);
                    writer.writeVarInt(packetID);
                    writer.writeByteArray(packet.getRawData());
                } else {
                    writer.writeInt(size);
                    writer.writeVarInt(packetID);
                    writer.writeByteArray(reader.readFollowingBytes(size - 1));
                }
            }

            replayData.getReplayZip().open();
            replayData.getReplayZip().addFile(writer.getInputStream(), "recording.tmcpr");
            replayData.getReplayZip().close();

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        instance = this;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException e) {
            e.printStackTrace();
        }

        verifyRequiredFiles();

        System.setProperty("org.lwjgl.librarypath", OS.getBobFolder() + "resources/natives/" + OS.getOS());

        start();
    }

    private void verifyRequiredFiles() {
        if(!new File(OS.getBobFolder() + "resources/").exists()) {
            File file = IO.openFilePrompt("Resources ZIP", null, "zip");
            if(file == null) System.exit(-1);

            IO.unzip(file, OS.getBobFolder() + "resources/");
        }
    }

    public BobSettings getSettings() {
        return settings;
    }

    public Projects getProjects() {
        return projects;
    }

    public ReplayData getReplayData() {
        return replayData;
    }
}
