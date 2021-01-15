package fr.rader.bob;

import com.google.gson.Gson;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ReplayData {

    private Map<String, Object> metaData = new HashMap<>();

    private final File project;
    private File replay;

    private boolean alreadyHasReplay = false;

    public ReplayData(File project) {
        this.project = project;

        for(File file : project.listFiles()) {
            if(file.getName().endsWith(".mcpr")) {
                alreadyHasReplay = true;
                this.replay = file;
                break;
            }
        }

        if(!alreadyHasReplay) {
            this.replay = IO.openFilePrompt();
        }

        if(replay == null) {
            System.out.println("No Replay selected, stopping.");
            System.exit(0);
        }

        extractFiles();
        readMetaData();
    }

    public File getProject() {
        return this.project;
    }

    public File getReplay() {
        return this.replay;
    }

    public File getRecording() {
        return new File(project.getAbsolutePath() + "/files/recording.tmcpr");
    }

    public Object getMetaData(String key) {
        return metaData.get(key);
    }

    private void extractFiles() {
        if(!alreadyHasReplay) {
            try {
                Files.copy(this.replay.toPath(), new File(project.getAbsolutePath() + "/" + this.replay.getName()).toPath());
            } catch (Exception ignored) {}
        }

        try {
            ZipFile mcprFile = new ZipFile(this.replay);
            mcprFile.extractAll(project.getAbsolutePath() + "/files/");
        } catch (ZipException e) {
            e.printStackTrace();
        }

        if(!Main.getInstance().getSettings().hasBadlionSupport()
                && new File(project.getAbsolutePath() + "/files/badlion.json").exists()) {
            JOptionPane.showMessageDialog(null, "Badlion Replay detected, stopping Bob.");

            // clear last opened project and delete files
            Main main = Main.getInstance();
            main.getSettings().setProperty("lastProject", "");
            main.getSettings().saveSettings();
            main.getProjects().removeProject(project.getName());
            main.getProjects().saveProjects();

            System.exit(0);
        }
    }

    private void readMetaData() {
        try {
            FileReader fileReader = new FileReader(project.getAbsolutePath() + "/files/metaData.json");
            metaData = new Gson().fromJson(fileReader, Map.class);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
