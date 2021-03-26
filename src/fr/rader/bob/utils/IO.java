package fr.rader.bob.utils;

import fr.rader.bob.nbt.tags.NBTCompound;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class IO {

    private static final int BUFFER_SIZE = 4096;

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

    public static void writeNBTFile(File destination, NBTCompound compound) {
        DataWriter writer = new DataWriter();
        compound.writeNBT(writer);

        writeFile(destination, writer.getInputStream());
    }

    public static void writeFile(File destination, InputStream inputStream) {
        try {
            FileOutputStream outputStream = new FileOutputStream(destination);

            int length;
            byte[] buffer = new byte[1024];
            while((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
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

    /**
     * Move a folder or a file to a new destination
     * @param source File/Folder to move
     * @param destination Destination for the File/Folder
     * @return true if the file was moved<br>false otherwise
     */
    public static boolean move(File source, File destination) {
        if(source == null || destination == null) return false;
        if(!destination.exists() && source.isDirectory()) destination.mkdirs();

        if(source.isDirectory()) {
            for(File file : source.listFiles()) {
                move(file, new File(destination.getAbsolutePath() + "/" + file.getParentFile().getName() + "/" + file.getName()));
            }
        } else {
            source.renameTo(destination);
        }

        return true;
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

    public static void unzip(File zipFile, String destination) {
        File destDir = new File(destination);
        if(!destDir.exists()) destDir.mkdir();

        try {
            ZipInputStream inputStream = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry entry = inputStream.getNextEntry();
            while(entry != null) {
                String filePath = destination + "/" + entry.getName();

                if(!entry.isDirectory()) {
                    extractFile(inputStream, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }

                inputStream.closeEntry();
                entry = inputStream.getNextEntry();
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractFile(ZipInputStream inputStream, String filePath) throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));

        int read;
        byte[] bytesIn = new byte[BUFFER_SIZE];
        while((read = inputStream.read(bytesIn)) != -1) {
            outputStream.write(bytesIn, 0, read);
        }

        outputStream.close();
    }
}
