package fr.rader.bob;

import fr.rader.bob.guis.MainInterface;
import fr.rader.bob.guis.ProjectSelector;
import fr.rader.bob.packet.Packet;
import fr.rader.bob.packet.reader.PacketArray;
import fr.rader.bob.packet.reader.PacketReader;
import fr.rader.bob.utils.DataReader;
import fr.rader.bob.utils.DataWriter;
import fr.rader.bob.utils.IO;
import fr.rader.bob.utils.OS;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class Main {

    private static Main instance;

    private ReplayData replayData;
    private BobSettings settings;
    private Projects projects;

    private String accessToken;

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
        //MainInterface mainInterface = new MainInterface();
        //mainInterface.createWindow();

        try {
            DataReader reader = new DataReader(Files.readAllBytes(replayData.getRecording().toPath()));

            reader.readInt();
            int len = reader.readInt();
            reader.readVarInt();
            reader.readFollowingBytes(len - 1);

            do {
                int timestamp = reader.readInt();
                int size = reader.readInt();
                int packetID = reader.readVarInt();

                if(packetID == 0x38) {
                    PacketReader packetReader = new PacketReader(0x38);
                    Packet packet = new Packet(reader.readFollowingBytes(size - 1), 0x38);
                    Packet newPacket = packetReader.serializePacket(packetReader.deserializePacket(packet));

                    System.out.println(Arrays.equals(packet.getRawData(), newPacket.getRawData()));
                    return;
                } else {
                    reader.addOffset(size - 1);
                }
            } while(reader.getOffset() != reader.getDataLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            try {
                File file = IO.openFilePrompt("Resources ZIP", null, "zip");
                if(file == null) System.exit(-1);

                ZipFile resourcesZip = new ZipFile(file);
                resourcesZip.extractAll(OS.getBobFolder() + "resources/");
            } catch (ZipException e) {
                e.printStackTrace();
            }
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
