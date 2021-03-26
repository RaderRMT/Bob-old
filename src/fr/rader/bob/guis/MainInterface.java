package fr.rader.bob.guis;

import fr.rader.bob.packet.reader.*;
import fr.rader.bob.utils.DataWriter;
import fr.rader.bob.utils.IO;
import fr.rader.bob.Main;
import fr.rader.bob.ReplayData;
import fr.rader.bob.engine.Engine;
import fr.rader.bob.guis.editor.PacketCell;
import fr.rader.bob.nbt.tags.NBTCompound;
import fr.rader.bob.packet.Packet;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainInterface {

    private static MainInterface instance;
    public static MainInterface getInstance() {
        return instance;
    }

    private final ReplayData replayData;
    private final File preferences;

    public JFrame frame;
    public JPanel panel;
    public JSplitPane verticalSplit;
    public JSplitPane horizontalSplit;
    public JTree dataTree;

    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private PacketReader packetReader;

    private Canvas engineCanvas;

    public MainInterface() {
        instance = this;
        this.replayData = Main.getInstance().getReplayData();

        preferences = new File(replayData.getProject().getAbsolutePath() + "/preferences.nbt");
        NBTCompound nbt;
        if(!preferences.exists()) {
            nbt = new NBTCompound("")
                    .addInt("hsdl", 930)
                    .addInt("vsdl", 530);

            IO.writeNBTFile(preferences, nbt);
        } else {
            nbt = IO.readNBTFile(preferences);
        }

        horizontalSplit.setDividerLocation(nbt.getComponent("hsdl").getAsInt());
        horizontalSplit.setEnabled(false);
        verticalSplit.setDividerLocation(nbt.getComponent("vsdl").getAsInt());

        root = new DefaultMutableTreeNode("Packet");
        treeModel = new DefaultTreeModel(root);

        PacketCellRenderer cellRenderer = new PacketCellRenderer();
        dataTree = new JTree(treeModel);
        dataTree.setCellRenderer(cellRenderer);

        engineCanvas = new Canvas();

        // minimum size for canvas (240p here)
        engineCanvas.setMinimumSize(new Dimension(320, 240));
        engineCanvas.setIgnoreRepaint(true);

        horizontalSplit.setRightComponent(dataTree);
        horizontalSplit.setLeftComponent(engineCanvas);
    }

    public void showPacketData(Packet packet) {
        root.removeAllChildren();

        System.out.println(packetReader.deserializePacket(packet));

        treeModel.setRoot(buildTree(packetReader.deserializePacket(packet), "Packet 0x" + Integer.toHexString(packet.getPacketID())));

        treeModel.reload();
    }

    private PacketCell buildTree(ArrayList<PacketBase> packetData, String cellName) {
        PacketCell currentCell = new PacketCell(cellName);
        currentCell.setAllowsChildren(true);

        for(PacketBase base : packetData) {
            if(base instanceof PacketMatch) {
                currentCell.add(buildTree(((PacketMatch) base).getData(), base.getName()));
            } else if(base instanceof PacketArray) {
                currentCell.add(buildTree(((PacketArray) base).getData(), base.getName()));
            } else if(base instanceof PacketCondition) {
                currentCell.add(buildTree(((PacketCondition) base).getData(), base.getName()));
            } else {
                PacketCell cell = new PacketCell(base.getName() + ": " + base.getAsPacketVariable().getValue());
                cell.setAllowsChildren(false);
                currentCell.add(cell);
            }
        }

        return currentCell;
    }

    public void createWindow() {
        frame = new JFrame("Bob - Made by Rader");
        frame.setContentPane(panel);

        // hide the window and execute stuff before getting rid of the window
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                savePreferences();
                Engine.setRunning(false);
                frame.dispose();
            }
        });

        // set window size, and put it in the middle of the screen
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        frame.setJMenuBar(addMenuBar());

        frame.setVisible(true);
        Engine.startEngine(engineCanvas, horizontalSplit.getDividerLocation(), verticalSplit.getDividerLocation());
    }

    public void savePreferences() {
        NBTCompound nbt = IO.readNBTFile(preferences);

        // hsdl = Horizontal Split Divider Location
        // vsdl = Vertical Split Divider Location
        nbt.getComponent("hsdl").getAsNBTInt().setValue(horizontalSplit.getDividerLocation());
        nbt.getComponent("vsdl").getAsNBTInt().setValue(verticalSplit.getDividerLocation());

        IO.writeNBTFile(preferences, nbt);
    }

    private JMenuBar addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        MenuBarListener listener = new MenuBarListener();

        //JMenuItem closeProject = new JMenuItem("Close Project");
        JMenuItem openProjectFolder = new JMenuItem("Open Project Folder");
        JMenuItem test = new JMenuItem("test");
        //closeProject.addActionListener(listener);
        openProjectFolder.addActionListener(listener);
        test.addActionListener(listener);

        //fileMenu.add(closeProject);
        fileMenu.add(openProjectFolder);
        fileMenu.add(test);

        menuBar.add(fileMenu);

        return menuBar;
    }

    public PacketReader getPacketReader() {
        return packetReader;
    }

    public void setPacketReader(PacketReader packetReader) {
        this.packetReader = packetReader;
    }
}

class PacketCellRenderer extends DefaultTreeCellRenderer {

    public PacketCellRenderer() {
        setBackgroundSelectionColor(new Color(0, 120, 215));
        setOpaque(true);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        this.selected = selected;
        this.hasFocus = hasFocus;

        if(selected) {
            this.setBackground(getBackgroundSelectionColor());
            setForeground(getTextSelectionColor());
        } else {
            this.setBackground(getBackgroundNonSelectionColor());
            setForeground(getTextNonSelectionColor());
        }

        if(value instanceof PacketCell) {
            PacketCell cell = (PacketCell) value;

            URL imageUrl = Main.class.getResource("/resources/images/" + cell.getIcon() + ".png");
            if(imageUrl != null) setIcon(new ImageIcon(imageUrl));

            if(cell.getAllowsChildren()) setText(cell.getName() + ", " + cell.getChildCount() + " entries");
            else setText(cell.getName());
        }

        return this;
    }
}

class MenuBarListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Main main = Main.getInstance();
        MainInterface mainInterface = MainInterface.getInstance();

        switch(((JMenuItem) e.getSource()).getText()) {
//            case "Close Project":
//                // remove last project
//                main.getSettings().setProperty("lastProject", "");
//                main.getSettings().saveSettings();
//
//                // close window
//                MainInterface.getInstance().savePreferences();
//                Engine.setRunning(false);
//                MainInterface.getInstance().frame.dispose();
//
//                // recall the start method
//                main.start();
//                break;
            case "Open Project Folder":
                try {
                    Runtime.getRuntime().exec("explorer.exe /root," + main.getReplayData().getProject());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
            case "test":
                DataWriter writer = new DataWriter();
                writer.writeInt(2); // test
                // test array (length of test)
                writer.writeByte(1); // 0
                writer.writeByte(2); // 1
                // test normal array
                writer.writeByte(3); // 0
                writer.writeByte(4); // 1
                writer.writeByte(5); // 0
                writer.writeByte(6); // 1
                // match test (there's nothing)
                // if test < 5
                writer.writeByte(5);// if byte

                Packet packet = new Packet(writer.getData(), 0x70);
                writer.closeStream();

                PacketReader packetReader = new PacketReader(0x70);
                mainInterface.setPacketReader(packetReader);
                mainInterface.showPacketData(packet);
                break;
        }
    }
}