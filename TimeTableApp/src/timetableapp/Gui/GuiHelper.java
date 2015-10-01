package timetableapp.Gui;

import controlP5.ControlEvent;
import controlP5.Controller;
import java.awt.TextField;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import processing.event.KeyEvent;
import timetableapp.util.AppState;
import timetableapp.util.ViewStates;

public class GuiHelper {

    private AppState state = AppState.getInstance();
    private JFileChooser fc;
    private int btnheight = 24;

    private MainView mv;
    private DataView dv;

    public GuiHelper() {
        //maybe work away in the AppState ????
        Map<String, Object> properties = new HashMap<>();
        properties.put("btnheight", (Object) btnheight);

        mv = new MainView(properties);
        dv = new DataView(properties);
    }

    public void draw() {
        switch (state.getSelectedViewState()) {
            case (ViewStates.MainView):
                state.getApp().background(state.getBackgroundcolor());
                mv.draw();
                break;
            case (ViewStates.DataView):
                state.getApp().background(state.getBackgroundcolor());
                dv.draw();
                break;
        }

    }

    public void keyPressed(KeyEvent evt) {
        int key = evt.getKeyCode();
        switch (state.getSelectedViewState()) {
            case (ViewStates.DataView):
                if(key == 10){
                    dv.handleEnter();
                }
                break;
        }
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
                    state.setSelectedViewState(ViewStates.DataView);
                    mv.hide();
                    dv.show();
                    dv.setBtnState();
                }

                break;
            case ("BackToMainView"):
                state.setSelectedViewState(ViewStates.MainView);
                mv.show();
                dv.setPage(0);
                dv.hide();
                break;
            case ("NextPage"):
                dv.pagePlus();
                break;
            case ("PreviousPage"):
                dv.pageMinus();
                break;
        }

    }

}
