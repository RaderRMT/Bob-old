package fr.rader.bob;

import fr.rader.bob.guis.MainInterface;
import fr.rader.bob.guis.ProjectSelector;
import fr.rader.bob.packet.Packet;
import fr.rader.bob.packet.PacketReader;

import javax.swing.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Main {

    private static Main instance;

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

        ReplayData data = new ReplayData(project);
        new MainInterface(data).createWindow();

        //System.out.println(project.getAbsolutePath());
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

        /*PacketReader packetReader = new PacketReader(0x0c);

        DataWriter writer = new DataWriter();

        writer.writeUUID(UUID.randomUUID());
        writer.writeVarInt(2);
        writer.writeFloat(20f);

        System.out.println(Arrays.toString(writer.getData()));

        Packet packet = packetReader.deserializePacket(writer.getData());

        byte[] out = packetReader.serializePacket(packet);

        System.out.println(Arrays.toString(out));
        System.out.println("test: " + (Arrays.equals(out, writer.getData())));*/

        //new PacketReader(0x25);

        //start();
    }

    public BobSettings getSettings() {
        return settings;
    }

    public Projects getProjects() {
        return projects;
    }
}
