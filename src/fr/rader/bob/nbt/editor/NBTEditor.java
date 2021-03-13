package fr.rader.bob.nbt.editor;

import fr.rader.bob.utils.OS;
import fr.rader.bob.nbt.tags.*;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NBTEditor {

    private final List<TreePath> expandedPaths = new ArrayList<>();

    private JPanel panel;
    private JToolBar toolbar;
    private JTree nbtTree;
    public JScrollPane scrollPane;

    private DefaultTreeModel model;
    private boolean reloading = false;

    private NBTCompound serializedTree;

    public NBTEditor() {
        toolbar.setFloatable(false);

        addButtons(toolbar);

        nbtTree.addTreeSelectionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            if(cell.getAllowsChildren()) {
                if(cell.getTagID() == 9) {
                    NBTList list = cell.getAssociatedNBT().getAsList();

                    toolbar.getComponent(3).setEnabled(false);
                    for(int i = 6; i < 18; i++) {
                        toolbar.getComponent(i).setEnabled(list.getTagID() == -1);
                    }

                    switch(list.getTagID()) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            toolbar.getComponent(5 + list.getTagID()).setEnabled(true);
                            break;
                        case 8:
                            toolbar.getComponent(15).setEnabled(true);
                            break;
                        case 9:
                            toolbar.getComponent(16).setEnabled(true);
                            break;
                        case 10:
                            toolbar.getComponent(17).setEnabled(true);
                            break;
                        case 11:
                            toolbar.getComponent(13).setEnabled(true);
                            break;
                        case 12:
                            toolbar.getComponent(14).setEnabled(true);
                            break;
                    }
                }

                if(cell.getTagID() == 10) {
                    for(int i = 6; i < 18; i++) {
                        toolbar.getComponent(i).setEnabled(true);
                    }

                    if(cell.isRoot()) {
                        toolbar.getComponent(2).setEnabled(false);
                        toolbar.getComponent(3).setEnabled(false);
                        toolbar.getComponent(4).setEnabled(false);
                    }
                }
            } else {
                for(int i = 6; i < 18; i++) {
                    toolbar.getComponent(i).setEnabled(false);
                }

                toolbar.getComponent(3).setEnabled(true);
            }

            if(!cell.isRoot()) {
                toolbar.getComponent(4).setEnabled(true);
                toolbar.getComponent(2).setEnabled(!(((NBTCell) cell.getParent()).getAssociatedNBT() instanceof NBTList));
            }
        });

        nbtTree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                if(!reloading) expandedPaths.add(event.getPath());
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                if(!reloading) expandedPaths.remove(event.getPath());
            }
        });

        nbtTree.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                int selRow = nbtTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = nbtTree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1 && e.getClickCount() == 2 && selPath != null) {
                    NBTCell selectedNode = (NBTCell) selPath.getLastPathComponent();
                    if(selectedNode.isLeaf()) {
                        if(selectedNode.invokeValueEditor()) reloadTree();
                    }
                }
            }
        });

        NBTCellRenderer cellRenderer = new NBTCellRenderer();
        nbtTree.setCellRenderer(cellRenderer);
    }

    private NBTCell buildTree(NBTCompound compound, int id) {
        NBTCell currentCell = new NBTCell(compound, id);
        currentCell.setAllowsChildren(true);

        for(NBTBase base : compound.getComponents()) {
            switch(base.getId()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 11:
                case 12:
                    NBTCell cell = new NBTCell(base, -1);
                    cell.setAllowsChildren(false);
                    currentCell.add(cell);
                    break;
                case 9:
                    currentCell.add(buildTreeList(base.getAsList()));
                    break;
                case 10:
                    currentCell.add(buildTree(base.getAsCompound(), -1));
                    break;
            }
        }

        return currentCell;
    }

    private NBTCell buildTreeList(NBTList list) {
        NBTCell currentCell = new NBTCell(list, -1);
        currentCell.setAllowsChildren(true);

        for(NBTBase base : list.getComponents()) {
            switch(list.getTagID()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 11:
                case 12:
                    base.setId(list.getTagID());
                    NBTCell cell = new NBTCell(base, -1);
                    cell.setAllowsChildren(false);
                    currentCell.add(cell);
                    break;
                case 9:
                    currentCell.add(buildTreeList(base.getAsList()));
                    break;
                case 10:
                    currentCell.add(buildTree(base.getAsCompound(), list.getTagID()));
                    break;
            }
        }

        return currentCell;
    }

    private void reloadTree() {
        model.reload();

        reloading = true;
        for(TreePath path : expandedPaths) {
            nbtTree.expandPath(path);
        }
        reloading = false;
    }

    public void invokeEditor(NBTCompound compound) {
        JDialog dialog = new JDialog(null, "NBT Editor", Dialog.ModalityType.DOCUMENT_MODAL);

        model = new DefaultTreeModel(buildTree(compound, -1));
        nbtTree.setModel(model);
        model.reload();

        dialog.setContentPane(panel);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                if(serializedTree == null) {
                    int result = JOptionPane.showConfirmDialog(null, "Do you want to save the NBT?", "Changes not saved", JOptionPane.YES_NO_CANCEL_OPTION);
                    if(result == JOptionPane.YES_OPTION) {
                        serializedTree = serializeCompound((NBTCell) model.getRoot(), false);
                    } else if(result == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                }

                dialog.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

        dialog.setSize(640, 460);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        dialog.dispose();
    }

    private void addButtons(JToolBar toolbar) {
        JButton button;

        button = makeButton("save", "Save");
        button.addActionListener(e -> serializedTree = serializeCompound((NBTCell) model.getRoot(), false));
        toolbar.add(button);

        toolbar.add(new JToolBar.Separator());

        button = makeButton("renameTag", "Rename NBT Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;
            if(cell.invokeNameEditor()) reloadTree();
        });
        toolbar.add(button);

        button = makeButton("editValue", "Edit NBT Tag Value");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;
            if(cell.invokeValueEditor()) reloadTree();
        });
        toolbar.add(button);

        button = makeButton("delete", "Delete NBT Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;
            if(cell.isRoot()) return;

            NBTCell parent = (NBTCell) cell.getParent();
            if(parent.getAssociatedNBT() instanceof NBTList) {
                NBTList list = parent.getAssociatedNBT().getAsList();
                list.removeComponentAt(parent.getIndex(cell));

                if(list.getComponents().length == 0) {
                    list.setTagID(-1);
                }
            } else if(parent.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = parent.getAssociatedNBT().getAsCompound();
                compound.removeComponentAt(parent.getIndex(cell));
            }

            model.removeNodeFromParent(cell);
            if(parent.getName().split(": ").length == 2) {
                parent.setName(parent.getName().split(": ")[0] + ": " + parent.getChildCount() + " entries");
            } else {
                parent.setName(parent.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        toolbar.add(new JToolBar.Separator());

        button = makeButton("byte", "Add Byte Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTByte element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(1);

                element = new NBTByte(0);

                list.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 1), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                element = new NBTByte(name, 0);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 1), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("short", "Add Short Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTShort element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(2);

                element = new NBTShort(0);

                list.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 2), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                element = new NBTShort(name, 0);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 2), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("int", "Add Int Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTInt element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(3);

                element = new NBTInt(0);

                list.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 3), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                element = new NBTInt(name, 0);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 3), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("long", "Add Long Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTLong element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(4);

                element = new NBTLong(0);

                list.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 4), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                element = new NBTLong(name, 0);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 4), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("float", "Add Float Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTFloat element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(5);

                element = new NBTFloat(0f);

                list.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 5), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                element = new NBTFloat(name, 0);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 5), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("double", "Add Double Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTDouble element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(6);

                element = new NBTDouble(0);

                list.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 6), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                element = new NBTDouble(name, 0);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 6), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("byteArray", "Add Byte Array Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTByteArray element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(7);

                int length = Integer.parseInt(JOptionPane.showInputDialog(null, null, "Create Tag (array length)", JOptionPane.PLAIN_MESSAGE));
                element = new NBTByteArray(new byte[length]);

                list.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 7), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                String preLength = JOptionPane.showInputDialog(null, null, "Create Tag (array length)", JOptionPane.PLAIN_MESSAGE);
                if(preLength == null) return;

                int length = Integer.parseInt(preLength);
                element = new NBTByteArray(name, new byte[length]);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 7), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("intArray", "Add Int Array Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTIntArray element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(11);

                int length = Integer.parseInt(JOptionPane.showInputDialog(null, null, "Create Tag (array length)", JOptionPane.PLAIN_MESSAGE));
                element = new NBTIntArray(new int[length]);

                list.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 11), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                String preLength = JOptionPane.showInputDialog(null, null, "Create Tag (array length)", JOptionPane.PLAIN_MESSAGE);
                if(preLength == null) return;

                int length = Integer.parseInt(preLength);
                element = new NBTIntArray(name, new int[length]);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 11), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("longArray", "Add Long Array Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTLongArray element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(12);

                int length = Integer.parseInt(JOptionPane.showInputDialog(null, null, "Create Tag (array length)", JOptionPane.PLAIN_MESSAGE));
                element = new NBTLongArray(new long[length]);

                list.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 12), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                String preLength = JOptionPane.showInputDialog(null, null, "Create Tag (array length)", JOptionPane.PLAIN_MESSAGE);
                if(preLength == null) return;

                int length = Integer.parseInt(preLength);
                element = new NBTLongArray(name, new long[length]);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 12), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("string", "Add String Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTString element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(8);

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                element = new NBTString(name);

                list.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 8), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                element = new NBTString(name, "");

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 8), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("list", "Add List Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTList element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(9);

                element = new NBTList(-1);

                list.addList(element);
                model.insertNodeInto(new NBTCell(element, 9), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                element = new NBTList(name, -1);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 9), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);

        button = makeButton("compound", "Add Compound Tag");
        button.addActionListener(e -> {
            NBTCell cell = (NBTCell) nbtTree.getLastSelectedPathComponent();
            if(cell == null) return;

            NBTCompound element;
            if(cell.getAssociatedNBT() instanceof NBTList) {
                NBTList list = cell.getAssociatedNBT().getAsList();
                list.setTagID(10);

                element = new NBTCompound();

                list.addCompound(element);
                model.insertNodeInto(new NBTCell(element, 10), cell, list.getComponents().length - 1);
            } else if(cell.getAssociatedNBT() instanceof NBTCompound) {
                NBTCompound compound = cell.getAssociatedNBT().getAsCompound();

                String name = JOptionPane.showInputDialog(null, null, "Create Tag (name)", JOptionPane.PLAIN_MESSAGE);
                if(name == null) return;
                element = new NBTCompound(name);

                compound.addComponent(element);
                model.insertNodeInto(new NBTCell(element, 10), cell, compound.getComponents().length - 1);
            }

            if(cell.getName().split(": ").length == 2) {
                cell.setName(cell.getName().split(": ")[0] + ": " + cell.getChildCount() + " entries");
            } else {
                cell.setName(cell.getChildCount() + " entries");
            }

            reloadTree();
        });
        toolbar.add(button);
    }

    private NBTList serializeList(NBTCell cell, boolean inList) {
        NBTList out = new NBTList(cell.getRealName(), cell.getAssociatedNBT().getAsList().getTagID());
        if(inList) out = new NBTList(cell.getAssociatedNBT().getAsList().getTagID());

        for(int i = 0; i < cell.getChildCount(); i++) {
            NBTCell currentCell = (NBTCell) cell.getChildAt(i);

            if(currentCell.getAssociatedNBT() instanceof NBTList) {
                out.addComponent(serializeList(currentCell, true));
            } else if(currentCell.getAssociatedNBT() instanceof NBTCompound) {
                out.addComponent(serializeCompound(currentCell, true));
            } else {
                out.addComponent(currentCell.getAssociatedNBT());
            }
        }

        return out;
    }

    private NBTCompound serializeCompound(NBTCell cell, boolean inList) {
        NBTCompound out = new NBTCompound(cell.getRealName());
        if(inList) out = new NBTCompound();

        for(int i = 0; i < cell.getChildCount(); i++) {
            NBTCell currentCell = (NBTCell) cell.getChildAt(i);

            if(currentCell.getAssociatedNBT() instanceof NBTList) {
                out.addComponent(serializeList(currentCell, false));
            } else if(currentCell.getAssociatedNBT() instanceof NBTCompound) {
                out.addComponent(serializeCompound(currentCell, false));
            } else {
                out.addComponent(currentCell.getAssociatedNBT());
            }
        }

        return out;
    }

    private JButton makeButton(String icon, String tooltip) {
        JButton button = new JButton();

        button.setToolTipText(tooltip);

        try {
            URL image = new File(OS.getBobFolder() + "resources/assets/nbt_editor_images/" + icon + ".png").toURI().toURL();
            button.setIcon(new ImageIcon(image, tooltip));
        } catch (MalformedURLException e) {
            button.setText(tooltip);
        }

        return button;
    }

    public NBTCompound getSerializedTree() {
        return serializedTree;
    }
}

class NBTCellRenderer extends DefaultTreeCellRenderer {

    public NBTCellRenderer() {
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

        if(value instanceof NBTCell) {
            NBTCell cell = (NBTCell) value;

            try {
                URL imageUrl = new File(OS.getBobFolder() + "resources/assets/nbt_editor_images/" + cell.getIconName() + ".png").toURI().toURL();
                setIcon(new ImageIcon(imageUrl));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            setText(cell.getName());
        }

        return this;
    }
}