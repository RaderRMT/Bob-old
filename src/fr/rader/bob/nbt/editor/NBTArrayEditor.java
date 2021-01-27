package fr.rader.bob.nbt.editor;

import fr.rader.bob.nbt.tags.NBTBase;
import fr.rader.bob.nbt.tags.NBTByteArray;
import fr.rader.bob.nbt.tags.NBTIntArray;
import fr.rader.bob.nbt.tags.NBTLongArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NBTArrayEditor {

    private JPanel panel;
    private JTextArea arrayTextArea;
    private JButton saveButton;
    private JButton cancelButton;

    private NBTBase returnArray;

    private static NBTArrayEditor instance;

    private JDialog dialog;

    public static NBTArrayEditor getInstance() {
        return instance;
    }

    public NBTArrayEditor() {
        instance = this;
    }

    public void invokeEditor(NBTBase array) {
        returnArray = array;

        dialog = new JDialog(null, "Edit Value...", Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setContentPane(panel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        if(array instanceof NBTByteArray) {
            for(byte value : array.getAsByteArray()) arrayTextArea.append(value + "\n");
        } else if(array instanceof NBTIntArray) {
            for(int value : array.getAsIntArray()) arrayTextArea.append(value + "\n");
        } else if(array instanceof NBTLongArray) {
            for(long value : array.getAsLongArray()) arrayTextArea.append(value + "\n");
        }

        ArrayEditorListener arrayEditorListener = new ArrayEditorListener();
        saveButton.addActionListener(arrayEditorListener);
        cancelButton.addActionListener(arrayEditorListener);

        dialog.setSize(640, 460);
        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);
    }

    public void closeDialog() {
        dialog.dispose();
    }

    public String getTextAreaText() {
        return arrayTextArea.getText();
    }

    public NBTBase getReturnArray() {
        return returnArray;
    }

    public void setReturnArray(NBTBase returnArray) {
        this.returnArray = returnArray;
    }

    public byte[] getByteArrayData() {
        if(returnArray == null) return null;
        return returnArray.getAsByteArray();
    }

    public int[] getIntArrayData() {
        if(returnArray == null) return null;
        return returnArray.getAsIntArray();
    }

    public long[] getLongArrayData() {
        if(returnArray == null) return null;
        return returnArray.getAsLongArray();
    }
}

class ArrayEditorListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        NBTArrayEditor editor = NBTArrayEditor.getInstance();

        switch(((JButton) e.getSource()).getText()) {
            case "Save":
                if(editor.getReturnArray() instanceof NBTByteArray) {
                    NBTByteArray byteArray = editor.getReturnArray().getAsNBTByteArray();
                    byte[] data = new byte[editor.getTextAreaText().split("\n").length];
                    for(int i = 0; i < data.length; i++) {
                        try {
                            data[i] = Byte.parseByte(editor.getTextAreaText().split("\n")[i]);
                        } catch (NumberFormatException numberFormatException) {
                            JOptionPane.showMessageDialog(null, numberFormatException.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    byteArray.setValue(data);
                } else if(editor.getReturnArray() instanceof NBTIntArray) {
                    NBTIntArray intArray = editor.getReturnArray().getAsNBTIntArray();
                    int[] data = new int[editor.getTextAreaText().split("\n").length];
                    for(int i = 0; i < data.length; i++) {
                        try {
                            data[i] = Integer.parseInt(editor.getTextAreaText().split("\n")[i]);
                        } catch (NumberFormatException numberFormatException) {
                            JOptionPane.showMessageDialog(null, numberFormatException.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    intArray.setValue(data);
                } else if(editor.getReturnArray() instanceof NBTLongArray) {
                    NBTLongArray longArray = editor.getReturnArray().getAsNBTLongArray();
                    long[] data = new long[editor.getTextAreaText().split("\n").length];
                    for(int i = 0; i < data.length; i++) {
                        try {
                            data[i] = Long.parseLong(editor.getTextAreaText().split("\n")[i]);
                        } catch (NumberFormatException numberFormatException) {
                            JOptionPane.showMessageDialog(null, numberFormatException.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    longArray.setValue(data);
                }

                editor.closeDialog();
                break;
            case "Cancel":
                editor.setReturnArray(null);
                editor.closeDialog();
                break;
        }
    }
}
