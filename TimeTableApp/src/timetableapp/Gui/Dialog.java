package timetableapp.Gui;

import java.awt.Frame;
import javax.swing.JOptionPane;
import lombok.Getter;

public class Dialog {

    public final static int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    public final static int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public final static int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;

    private @Getter
    int DialogOutput;

    public Dialog(String message, int type) {
        JOptionPane.showMessageDialog(new Frame(), message, null, type);
        
    }

    public Dialog(String title, String message, int type) {
        JOptionPane.showMessageDialog(new Frame(), message, title, type);
    }

    public Dialog() {
    }

    public void fatalErrorDialog(String message){
        JOptionPane.showMessageDialog(new Frame(), message, null, Dialog.ERROR_MESSAGE);
        System.exit(0);
    }
}
