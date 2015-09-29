package timetableapp.Gui;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import java.util.concurrent.Callable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import processing.core.PApplet;
import processing.data.Table;
import timetableapp.util.AppState;
import timetableapp.util.Parser;
import timetableapp.util.observer.StateObserver;

public class GuiHelper {

    private PApplet app;
    private ControlP5 cp5;
    private int btnheight = 18;
    private JFileChooser fc;
    private int fcResult = -1;
    private AppState state = AppState.getInstance();
    private Parser parser;
    private Table loadTable;

    private Callable newFileSelectedHandler = () -> {
        try {
            if (fcResult == JFileChooser.APPROVE_OPTION) {
                Parser parser = new Parser(fc.getSelectedFile(), app);

                fcResult = -1;
                state.getNewFileSelectedStateObserver().resetValue();
                state.setLoadingFileState(1);

                loadTable = parser.parse();
                
                for (String column : loadTable.getColumnTitles()) {
                    System.out.println(column);
                }
                return null;
            }
            throw new Exception("invalid file, application will close now");
        } catch (Exception e) {
            throw new Exception("could not load file properly, application will close now");
        }
    };

    public GuiHelper(PApplet app) {
        this.app = app;
        cp5 = new ControlP5(app);
    }

    public void setup() {
        cp5.addButton(cp5, "selectFileBtn")
                .setPosition(10, app.height - btnheight - 10)
                .setSize(70, btnheight)
                .setLabel("Select File");

        cp5.addButton(cp5, "viewData")
                .setPosition(10, app.height - (btnheight * 2) - 20)
                .setSize(70, btnheight)
                .setLabel("View Data");

        state.getNewFileSelectedStateObserver().addObserver(new StateObserver(newFileSelectedHandler));
    }

    public void controlEvent(ControlEvent evt) {
        Controller<?> controller = evt.getController();
        switch (controller.getName()) {
            case ("selectFileBtn"):
                fc = new JFileChooser();
                FileFilter filter1 = new FileNameExtensionFilter("data files(txt, ics, csv, tsv, tab)", new String[]{"txt", "ics", "csv", "tsv", "tab"});
                fc.setFileFilter(filter1);
                fc.setAcceptAllFileFilterUsed(false);
                fcResult = fc.showOpenDialog(null);
                state.setNewFileSelectedState(1);
                break;
            case ("viewData"):
                if (AppState.getInstance().getFileLoadedState() == 0) {
                    new Dialog(null, "No File Selected", Dialog.WARNING_MESSAGE);
                } else {
                    System.out.println("showmydataTable(pages)");
                }
                break;
        }

    }

}
