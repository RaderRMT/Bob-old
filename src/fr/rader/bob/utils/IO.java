package fr.rader.bob.utils;

import fr.rader.bob.nbt.tags.NBTCompound;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class IO {

    public static File openFilePrompt(String description, String path, String... extensions) {
        JFileChooser fileChooser = new JFileChooser(path);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                for(String extension : extensions) {
                    if(file.getName().endsWith(extension)) return true;
                }

                return file.isDirectory();
            }

            @Override
            public String getDescription() {
                String finalDescription = description + " (";

                for(int i = 0; i < extensions.length; i++) {
                    finalDescription += "*." + extensions[i] + ((i < extensions.length - 1) ? ", " : ")");
                }

                return finalDescription;
            }
        });

        int option = fileChooser.showOpenDialog(null);

        if(option == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    public static void writeBinaryFile(String destination, byte[] data) {
        writeBinaryFile(new File(destination), data);
    }

    public static void writeBinaryFile(File destination, byte[] data) {
        try {
            FileOutputStream outputStream = new FileOutputStream(destination);

            outputStream.write(data);

            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NBTCompound readNBTFile(File file) {
        try {
            return new DataReader(Files.readAllBytes(file.toPath())).readNBT();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void move(File source, File destination) {
        if(!destination.exists() && source.isDirectory()) destination.mkdirs();

        if(source.isDirectory()) {
            for(File file : source.listFiles()) {
                move(file, new File(destination.getAbsolutePath() + "/" + file.getParentFile().getName() + "/" + file.getName()));
            }
        } else {
            source.renameTo(destination);
        }
    }

    public static void deleteDirectory(File directory) {
        if(directory.exists() && directory.isDirectory()) {
            for(File file : directory.listFiles()) {
                if(file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }

        directory.delete();
    }
}
