package fr.rader.bob;

import fr.rader.bob.nbt.NBTBase;
import fr.rader.bob.nbt.NBTCompound;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class BobSettings {

    private final static Map<String, String> settings = new HashMap<>();

    private final File settingsFile;

    private boolean badlionSupport = false;

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

        // read settings from config file
        try {
            NBTCompound nbt = new DataReader(Files.readAllBytes(settingsFile.toPath())).readNBT();

            for(NBTBase base : nbt.getComponents()) {
                settings.put(base.getName(), base.getAsString());
            }

            if(settings.containsKey("badlionSupport")) {
                badlionSupport = canSupportBadlion(settings.get("badlionSupport").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean canSupportBadlion(byte[] inputBytes) {
        BitReader bitReader = new BitReader(inputBytes);
        String out = "";

        boolean start = true;
        int rleCounter = 0;
        int offset = 0;
        byte bitPair;
        while(offset != bitReader.getDataLength() * 4) {
            bitPair = bitReader.readBits(2);
            if(bitPair < 0) return false;

            if(start) out += (bitPair == 0) ? "1" : "0";

            if(bitPair == 0) {
                if(rleCounter == 0 && !start) out += "00";
                rleCounter++;
            } else if(bitPair <= 3) {
                if(rleCounter != 0) {
                    out += rleNormal(rleCounter);
                    rleCounter = 0;
                }

                out += ((bitPair <= 1) ? "0" : "") + Integer.toBinaryString(bitPair);
            }

            start = false;
            offset++;
        }

        if(rleCounter != 0) out += rleNormal(rleCounter);

        return out.equals(Long.toBinaryString(6134818607710629960L));
    }

    public boolean hasBadlionSupport() {
        return badlionSupport;
    }

    private String rleNormal(int rleCounter) {
        rleCounter++;
        String number = Integer.toBinaryString(rleCounter);
        int biggestPower = 0;
        for(int i = 0; i < number.length(); i++) {
            if(number.charAt(i) == '1') {
                biggestPower = (int) Math.pow(2, number.length() - i - 1);
                break;
            }
        }

        int v = rleCounter - biggestPower;
        int l = biggestPower - 2;

        int val = (l << number.length() - 1) | v;
        return ((val <= 1) ? "0" : "") + Integer.toBinaryString(val);
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

        IO.writeBinaryFile(settingsFile, nbt.toByteArray());
    }

    private void createConfig() {
        NBTCompound settings = new NBTCompound("")
                .addString("workingDirectory", OS.getBobFolder().replace("\\", "/"))
                .addString("lastProject", "");

        IO.writeBinaryFile(settingsFile, settings.toByteArray());
    }
}
