package fr.rader.bob;

import fr.rader.bob.nbt.tags.NBTBase;
import fr.rader.bob.nbt.tags.NBTCompound;
import fr.rader.bob.nbt.tags.NBTList;
import fr.rader.bob.nbt.tags.NBTString;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Projects {

    private static Projects instance;

    private List<String> projects;
    private File projectsFile;

    public static Projects getInstance() {
        return instance;
    }

    public Projects() {
        instance = this;
        projects = new ArrayList<>();
        projectsFile = new File(BobSettings.getWorkingDirectory() + "/projects/projects.nbt");

        if(!projectsFile.getParentFile().exists()) projectsFile.getParentFile().mkdirs();
        if(!projectsFile.exists()) saveProjects();

        readProjects();
    }

    public List<String> getProjectsNames() {
        return projects;
    }

    public List<File> getProjectsFiles() {
        List<File> out = new ArrayList<>();

        for(String name : projects) {
            out.add(new File(BobSettings.getWorkingDirectory() + "/projects/" + name));
        }

        return out;
    }

    public void removeProject(String projectName) {
        if(projects.contains(projectName)) {
            projects.remove(projectName);

            deleteDirectory(new File(BobSettings.getWorkingDirectory() + "/projects/" + projectName));
        } else {
            JOptionPane.showMessageDialog(null, "The project \"" + projectName + "\" does not exists");
        }
    }

    public void addProject(String projectName) {
        if(!projects.contains(projectName)) {
            projects.add(projectName);

            initFolders(projectName);
        } else {
            JOptionPane.showMessageDialog(null, "A project already exists with the name \"" + projectName + "\"");
        }
    }

    public void saveProjects() {
        NBTCompound tag = new NBTCompound("").addList("projects", new NBTList("projects", 0x08));

        for(String name : projects) {
            tag.getComponent("projects").getAsList().addString(name);
        }

        IO.writeBinaryFile(projectsFile, tag.toByteArray());
    }

    private void readProjects() {
        try {
            DataReader reader = new DataReader(Files.readAllBytes(projectsFile.toPath()));

            NBTList tag = reader.readNBT().getComponent("projects").getAsList();
            for(NBTBase base : tag.getComponents()) {
                if(base instanceof NBTString) {
                    addProject(base.getAsString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initFolders(String projectName) {
        String projectFolder = BobSettings.getWorkingDirectory() + "/projects/" + projectName;

        File file = new File(projectFolder);
        if(!file.exists()) file.mkdirs();
    }

    private void deleteDirectory(File directory) {
        if(directory.exists()) {
            if(!directory.isDirectory()) return;

            File[] files = directory.listFiles();
            if(files != null) {
                for(File file : files) {
                    if(file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }

        directory.delete();
    }
}
