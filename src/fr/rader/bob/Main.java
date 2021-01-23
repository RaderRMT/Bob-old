package fr.rader.bob;

import fr.rader.bob.guis.MainInterface;
import fr.rader.bob.guis.ProjectSelector;

import javax.swing.*;
import java.io.*;

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

        /*DataWriter writer = new DataWriter();
        writer.writeInt(2);

        writer.writeInt(1);
        writer.writeInt(2);
        writer.writeInt(3);
        writer.writeInt(4);
        writer.writeInt(5);
        writer.writeInt(6);
        writer.writeInt(7);
        writer.writeInt(8);

        Packet packet = new Packet(writer.getData(), 0x69);
        PacketReader packetReader = new PacketReader(packet.getPacketID());
        packetReader.readPacket(packet);
        byte[] rawData = packetReader.serializePacket(packet);

        System.out.println(Arrays.equals(rawData, writer.getData()));*/

        /*List<Packet> packetList = new ArrayList<>();
        HashMap<Integer, PacketReader> readerHashMap = new HashMap<>();

        int packetCounter = 1;

        DataWriter writer = new DataWriter();
        try {
            DataReader reader = new DataReader(Files.readAllBytes(replayData.getRecording().toPath()));

            writer.writeInt(reader.readInt());
            int len = reader.readInt();
            writer.writeInt(len);
            writer.writeByteArray(reader.readFollowingBytes(len));

            do {
                packetCounter++;
                int timestamp = reader.readInt();
                int size = reader.readInt();
                int packetID = reader.readVarInt();

                if(!readerHashMap.containsKey(packetID)) readerHashMap.put(packetID, new PacketReader(packetID));

                Packet packet = new Packet(reader.readFollowingBytes(size - 1), packetID);
                packet.setTimestamp(timestamp);

                packetList.add(packet);
            } while(reader.getOffset() != reader.getDataLength());

            System.out.println(packetCounter + ", " + packetList.size());

            packetCounter = 0;
            for(Packet packet : packetList) {
                packetCounter++;

                //System.out.println(packet);

                writer.writeInt(packet.getTimestamp());
                writer.writeInt(packet.getRawData().length + 1);
                writer.writeVarInt(packet.getPacketID());
                writer.writeByteArray(packet.getRawData());
            }

            System.out.println(Arrays.equals(writer.getData(), Files.readAllBytes(replayData.getRecording().toPath())));

            OutputStream outputStream = new FileOutputStream("C:/Users/marti/Desktop/test.bin");

            outputStream.write(writer.getData());

            outputStream.flush();
            outputStream.close();
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

        start();
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
