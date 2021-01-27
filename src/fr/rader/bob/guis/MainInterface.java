package fr.rader.bob.guis;

import fr.rader.bob.DataWriter;
import fr.rader.bob.IO;
import fr.rader.bob.Main;
import fr.rader.bob.ReplayData;
import fr.rader.bob.nbt.tags.NBTCompound;
import fr.rader.bob.packet.Packet;
import fr.rader.bob.packet.PacketReader;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.LinkedHashMap;

public class MainInterface {

    private static MainInterface instance;

    public static MainInterface getInstance() {
        return instance;
    }

    private final ReplayData replayData;

    public JFrame frame;
    public JPanel panel;
    public JSplitPane verticalSplit;
    public JSplitPane horizontalSplit;
    public JTree dataTree;

    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private File preferences;

    public Canvas engineCanvas = new Canvas();

    public MainInterface() {
        instance = this;
        this.replayData = Main.getInstance().getReplayData();

        preferences = new File(replayData.getProject().getAbsolutePath() + "/preferences.nbt");
        NBTCompound nbt;
        if(!preferences.exists()) {
            nbt = new NBTCompound("")
                    .addInt("horizontalSplitDividerLocation", 930)
                    .addInt("verticalSplitDividerLocation", 530);

            IO.writeBinaryFile(preferences, nbt.toByteArray());
        } else {
            nbt = IO.readNBTFile(preferences);
        }

        horizontalSplit.setDividerLocation(nbt.getComponent("horizontalSplitDividerLocation").getAsInt());
        verticalSplit.setDividerLocation(nbt.getComponent("verticalSplitDividerLocation").getAsInt());

        root = new DefaultMutableTreeNode("Packet");
        treeModel = new DefaultTreeModel(root);
        dataTree = new JTree(treeModel);
        //dataTree.setCellRenderer(new EditorTreeCellRenderer());

        // minimum size for canvas (240p here)
        engineCanvas.setMinimumSize(new Dimension(426, 240));

        horizontalSplit.setRightComponent(dataTree);
        horizontalSplit.setLeftComponent(engineCanvas);
    }

    public void showPacketData(Packet packet, PacketReader packetReader) {
        root.removeAllChildren();

        root.add(buildTree(packetReader.readPacket(packet), packetReader, null));

        treeModel.reload();
    }

    /*public void showPacketData(LinkedHashMap<String, Object> packetData) {
        root.removeAllChildren();

        root.add(buildTree(packetData, null));

        //root.setUserObject("Packet " + String.format("%1$02X", packet.getPacketID()));

        treeModel.reload();
    }*/

    private DefaultMutableTreeNode buildTree(LinkedHashMap<String, Object> packetDataMap, PacketReader packetReader, String parent) {
        DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode();

        if(parent != null) currentNode.setUserObject(parent);

        /*for(Map.Entry<String, Object> packetElement : packetDataMap.entrySet()) {
            String elementName = packetElement.getKey();

            if(elementName.startsWith("_")) {
                String[] splittedElementName = elementName.split("_");
                switch(splittedElementName[1]) {
                    case "array":
                        String arrayName = null;
                        if(splittedElementName.length == 4) {
                            arrayName = splittedElementName[3];
                        }
                        break;
                    case "match":
                        break;
                    case "if":
                        break;
                }
            } else {
                Cell cell = new Cell(elementName, packetReader.getDataTypeOf(elementName));
                System.out.println(cell.getName());
            }
        }*/

        /*for(Map.Entry<String, Object> element : map.entrySet()) {
            if(element.getValue() instanceof LinkedHashMap) {
                String name = element.getKey();

                currentNode.add(buildTree((LinkedHashMap<String, Object>) element.getValue(), name));
            } else if(element.getValue() instanceof Object[]) {
                currentNode.add(readArray((Object[]) element.getValue(), element.getKey()));
            } else {
                currentNode.add(new DefaultMutableTreeNode(element.getKey()));
            }
        }*/

        return currentNode;
    }

    private DefaultMutableTreeNode readArray(Object[] array, String parent) {
        DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(parent);

        for(Object object : array) currentNode.add(new DefaultMutableTreeNode(object));

        return currentNode;
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
                ((JFrame) e.getComponent()).dispose();
            }
        });

        // set window size, and put it in the middle of the screen
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        frame.setJMenuBar(addMenuBar());

        frame.setVisible(true);
    }

    public void savePreferences() {
        NBTCompound nbt = IO.readNBTFile(preferences);

        nbt.getComponent("horizontalSplitDividerLocation").getAsNBTInt().setValue(horizontalSplit.getDividerLocation());
        nbt.getComponent("verticalSplitDividerLocation").getAsNBTInt().setValue(verticalSplit.getDividerLocation());

        IO.writeBinaryFile(preferences, nbt.toByteArray());
    }

    private JMenuBar addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        MenuBarListener listener = new MenuBarListener();

        JMenuItem closeProject = new JMenuItem("Close Project");
        JMenuItem test = new JMenuItem("test");
        closeProject.addActionListener(listener);
        test.addActionListener(listener);

        fileMenu.add(closeProject);
        fileMenu.add(test);

        menuBar.add(fileMenu);

        return menuBar;
    }
}

class EditorTreeCellRenderer implements TreeCellRenderer {

    private JLabel label;

    public EditorTreeCellRenderer() {
        label = new JLabel();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        System.out.println(o);

        label.setIcon(null);
        label.setText("" + value);

        return label;
    }
}

class MenuBarListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Main main = Main.getInstance();

        switch(((JMenuItem) e.getSource()).getText()) {
            case "Close Project":
                // remove last project
                main.getSettings().setProperty("lastProject", "");
                main.getSettings().saveSettings();

                // close window
                MainInterface.getInstance().savePreferences();
                MainInterface.getInstance().frame.dispose();

                // recall the start method
                main.start();
                break;
            case "test":
                DataWriter writer = new DataWriter();
                writer.writeVarInt(2);

                writer.writeVarInt(1);
                writer.writeVarInt(2);
                writer.writeVarInt(3);
                writer.writeVarInt(4);
                writer.writeVarInt(5);
                writer.writeVarInt(6);

                Packet packet = new Packet(writer.getData(), 0x06);
                PacketReader packetReader = new PacketReader(packet.getPacketID());

                MainInterface.getInstance().showPacketData(packet, packetReader);
                break;
        }
    }
}