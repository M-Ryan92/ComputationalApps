package timetableapp.Gui;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import timetableapp.util.AppState;

public class GuiHelper {

    private AppState state = AppState.getInstance();
    private ControlP5 cp5;
    private JFileChooser fc;
    private int btnheight = 24;
    
    private MainView mv;
    private DataView dv;

    public GuiHelper() {
        //maybe work away in the AppState ????
        Map<String, Object> properties = new HashMap<>();
        properties.put("btnheight", (Object) btnheight);

        cp5 = new ControlP5(state.getApp());
        mv = new MainView(properties);
        dv = new DataView(properties);
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
                    mv.hide();
                    dv.show();
                }
                break;
        }

    }

}
