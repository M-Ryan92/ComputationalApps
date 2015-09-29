package timetableapp.Gui;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import static controlP5.ControlP5.s;
import controlP5.Controller;
import controlP5.ControllerInterface;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.concurrent.Callable;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import processing.core.PApplet;
import static processing.core.PApplet.println;
import processing.data.Table;
import timetableapp.eventhandlers.NewFileSelectedHandler;
import timetableapp.util.AppState;
import timetableapp.util.observer.StateObserver;

public class GuiHelper {

    private AppState state = AppState.getInstance();
    private ControlP5 cp5;
    private JFileChooser fc;

    private int btnheight = 18;

    public GuiHelper() {
        cp5 = new ControlP5(state.getApp());
    }

    public void setup() {
        PApplet app = state.getApp();

        cp5.addButton(cp5, "selectFileBtn")
                .setPosition(10, app.height - btnheight - 10)
                .setSize(70, btnheight)
                .setLabel("Select File");

        cp5.addButton(cp5, "viewData")
                .setPosition(10, app.height - (btnheight * 2) - 20)
                .setSize(70, btnheight)
                .setLabel("View Data");

        state.getNewFileSelectedStateObserver().addObserver(new StateObserver(new NewFileSelectedHandler()));
    }

    public void reset(){
        cp5.getAll().stream().forEach(ci -> ci.hide());
        state.getApp().background(state.getBackgroundcolor());
    }
    public void controlEvent(ControlEvent evt) {
        Controller<?> controller = evt.getController();
        switch (controller.getName()) {
            case ("selectFileBtn"):
                fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("data files(txt, ics, csv, tsv, tab)",
                        new String[]{"txt", "ics", "csv", "tsv", "tab"}));
                fc.setAcceptAllFileFilterUsed(false);

                int fcResult = fc.showOpenDialog(null);
                if (fcResult == JFileChooser.APPROVE_OPTION) {
                    fcResult = -1;
                    state.setSelectedFile(fc.getSelectedFile());
                    state.setNewFileSelectedState(1);
                }
                break;
            case ("viewData"):
                if (state.getFileLoadedState() == 0) {
                    new Dialog(null, "No File Selected", Dialog.WARNING_MESSAGE);
                } else {
                    System.out.println("showmydataTable(pages)");
                    reset();
                }
                break;
        }

    }

}
