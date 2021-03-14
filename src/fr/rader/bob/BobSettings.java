package fr.rader.bob;

import fr.rader.bob.nbt.tags.NBTBase;
import fr.rader.bob.nbt.tags.NBTCompound;
import fr.rader.bob.utils.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class BobSettings {

    private final static Map<String, String> settings = new HashMap<>();

    private final File settingsFile;

    public BobSettings() {
        this.settingsFile = new File(OS.getBobFolder() + "settings.nbt");

        try {
            if(settingsFile.isDirectory()) settingsFile.delete();
            if(!settingsFile.exists()) {
                settingsFile.getParentFile().mkdirs();
                settingsFile.createNewFile();

                createConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            NBTCompound nbt = new DataReader(Files.readAllBytes(settingsFile.toPath())).readNBT();

            for(NBTBase base : nbt.getComponents()) {
                settings.put(base.getName(), base.getAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createConfig() {
        NBTCompound settings = new NBTCompound("")
                .addString("workingDirectory", OS.getBobFolder().replace("\\", "/"))
                .addString("lastProject", "");

        DataWriter writer = new DataWriter();
        settings.writeNBT(writer);

        IO.writeFile(settingsFile, writer.getInputStream());
    }

    public static String getWorkingDirectory() {
        return settings.get("workingDirectory");
    }

    public void changeWorkingDirectory(String newDirectory) {
        File oldDir = new File(getProperty("workingDirectory") + "/projects/");
        File newDir = new File(newDirectory + "/projects/");

        if(!newDir.exists()) newDir.mkdirs();

        IO.move(oldDir, newDir.getParentFile());
        IO.deleteDirectory(oldDir);

        setProperty("workingDirectory", newDirectory);
        saveSettings();
    }

    public String getProperty(String key) {
        return settings.get(key);
    }

    public void setProperty(String key, String value) {
        settings.replace(key, value);
    }

    public void saveSettings() {
        NBTCompound nbt = new NBTCompound("");

        for(Map.Entry<String, String> value : settings.entrySet()) {
            nbt.addString(value.getKey(), value.getValue());
        }

        DataWriter writer = new DataWriter();
        nbt.writeNBT(writer);

        IO.writeFile(settingsFile, writer.getInputStream());
    }
}
