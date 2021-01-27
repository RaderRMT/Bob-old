package fr.rader.bob.guis;

import fr.rader.bob.Main;
import fr.rader.bob.Projects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectSelector {

    private JDialog dialog;

    public JPanel mainPanel;
    public JList<String> projectsList;
    public JButton newProjectButton;
    public JButton openProjectButton;
    public JButton deleteProjectButton;

    private static ProjectSelector instance;

    private String project;

    public static ProjectSelector getInstance() {
        return instance;
    }

    public ProjectSelector() {
        instance = this;

        ProjectListener projectListener = new ProjectListener();

        deleteProjectButton.addActionListener(projectListener);
        openProjectButton.addActionListener(projectListener);
        newProjectButton.addActionListener(projectListener);

        updateList();
    }

    public String createWindow() {
        dialog = new JDialog(null, "Bob Project Selector", Dialog.ModalityType.DOCUMENT_MODAL);

        dialog.setContentPane(this.mainPanel);
        dialog.setSize(400, 300);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        dialog.dispose();

        return project;
    }

    public void updateList() {
        projectsList.setListData(Projects.getInstance().getProjectsNames().toArray(new String[0]));
    }

    public void setProject(String project) {
        this.project = project;
    }

    public JDialog getDialog() {
        return dialog;
    }
}

class ProjectListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ProjectSelector selector = ProjectSelector.getInstance();
        Projects projects = Projects.getInstance();
        Main main = Main.getInstance();

        switch(((JButton) e.getSource()).getText()) {
            case "New Project":
                String name = JOptionPane.showInputDialog(selector.getDialog(), "Project name:");

                if(name != null && !name.isEmpty() && name.length() <= 0xffff) {
                    projects.addProject(name);
                    projects.saveProjects();
                    selector.updateList();
                }
                break;
            case "Open Project":
                if(projects.getProjectsFiles().size() > 0) {
                    String selectedProject = selector.projectsList.getSelectedValue();

                    if(selectedProject == null) return;

                    selector.setProject(selectedProject);
                    selector.getDialog().dispose();
                }
                break;
            case "Delete Project":
                if(projects.getProjectsFiles().size() > 0) {
                    String selectedProject = selector.projectsList.getSelectedValue();

                    if(selectedProject == null) return;

                    if(selectedProject.equals(main.getSettings().getProperty("lastProject"))) {
                        main.getSettings().setProperty("lastProject", "");
                        main.getSettings().saveSettings();
                    }

                    projects.removeProject(selectedProject);
                    projects.saveProjects();
                    selector.updateList();
                }
                break;
        }
    }
}
