package fr.rader.bob;

import com.google.gson.Gson;
import fr.rader.bob.utils.DataReader;
import fr.rader.bob.utils.IO;
import fr.rader.bob.utils.OS;
import fr.rader.bob.utils.ReplayZip;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ReplayData {

    private Map<String, Object> metaData = new HashMap<>();

    private ReplayZip replayZip;
    private File mcprFile;
    private File project;

    public ReplayData(File project) {
        this.project = project;

        boolean alreadyHasReplay = false;
        for(File file : project.listFiles()) {
            if(file.getName().endsWith(".mcpr")) {
                alreadyHasReplay = true;
                mcprFile = file;
            }
        }

        if(!alreadyHasReplay) {
            mcprFile = IO.openFilePrompt("Replay File", OS.getMinecraftFolder() + "replay_recordings/", "mcpr");
        }

        if(mcprFile == null) {
            System.out.println("No Replay selected, stopping.");
            System.exit(0);
        }

        File oldMcprFile = new File(project.getAbsolutePath() + "/" + this.mcprFile.getName());
        if(!alreadyHasReplay) {
            try {
                Files.copy(this.mcprFile.toPath(), oldMcprFile.toPath());
            } catch (Exception ignored) {}

            mcprFile = oldMcprFile;
        }

        replayZip = new ReplayZip(mcprFile);
        readMetaData();
        checkBadlion();
    }

    private void readMetaData() {
        try {
            DataReader reader = new DataReader(replayZip.getEntry("metaData.json"));

            String readerMetaData = null;
            try {
                readerMetaData = reader.readString(reader.getLength());
            } catch (IOException e) {
                e.printStackTrace();
            }

            reader.close();

            if(readerMetaData == null) return;
            metaData = new Gson().fromJson(readerMetaData, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkBadlion() {
        if(replayZip.hasEntry("badlion.json")) stopBob();
        if(!replayZip.hasEntry("mods.json")) stopBob();
        if(!replayZip.hasEntry("recording.tmcpr.crc32")) stopBob();

        if(!metaData.containsKey("serverName")) stopBob();
        if(!((String) metaData.get("generator")).startsWith("ReplayMod")) stopBob();
    }

    private void stopBob() {
        JOptionPane.showMessageDialog(null, "Badlion Replay detected, stopping Bob.\nPlease use the official ReplayMod.");

        // clear last opened project and delete files
        Main main = Main.getInstance();
        main.getSettings().setProperty("lastProject", "");
        main.getSettings().saveSettings();
        main.getProjects().removeProject(project.getName());
        main.getProjects().saveProjects();

        System.exit(0);
    }

    public File getProject() {
        return project;
    }

    public File getMcprFile() {
        return mcprFile;
    }

    public Object getMetaData(String key) {
        return metaData.get(key);
    }

    public int getProtocolVersion() {
        return (int) ((double) metaData.get("protocol"));
    }

    public ReplayZip getReplayZip() {
        return replayZip;
    }
}
