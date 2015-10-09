package timetableapp.gui;

import controlP5.ControlEvent;
import controlP5.Controller;
import processing.event.KeyEvent;
import timetableapp.gui.views.DataView;
import timetableapp.gui.views.LoadView;
import timetableapp.gui.views.MainView;
import timetableapp.util.AppProperties;
import timetableapp.util.state.AppState;
import timetableapp.util.state.ViewStates;

public class GuiHelper {

    private AppState state = AppState.getInstance();
    private int btnheight = 24;

    private MainView mv;
    private DataView dv;
    private LoadView lv;

    public GuiHelper() {
        mv = new MainView();
        dv = new DataView();
        lv = new LoadView();
    }

    public void draw() {
        switch (state.getSelectedViewState()) {
            case (ViewStates.MainView):
                state.getApp().background(AppProperties.backgroundColor);
                mv.draw();
                break;
            case (ViewStates.DataView):
                state.getApp().background(AppProperties.backgroundColor);
                dv.draw();
                break;
            case (ViewStates.LoadView):
                lv.draw();
                break;
        }

    }

    public void keyPressed(KeyEvent evt) {
        int key = evt.getKeyCode();
        switch (state.getSelectedViewState()) {
            case (ViewStates.DataView):
                if (key == 10) {
                    dv.handleEnter();
                }
                break;
        }
    }

    public void controlEvent(ControlEvent evt) {
        mv.controlEvent(evt);
        dv.controlEvent(evt);
        Controller<?> controller = evt.getController();
        
        switch (controller.getName()) {
            case ("viewData"):

                if (state.getFileLoadedState() == 0) {
                    new Dialog(null, "No File Selected", Dialog.WARNING_MESSAGE);
                } else {
                    state.setSelectedViewState(ViewStates.DataView);
                    mv.hide();
                    dv.show();
                    dv.setPageBtnState();
                    dv.setColPageBtnState();
                }

                break;
            case ("BackToMainView"):
                state.setSelectedViewState(ViewStates.MainView);
                mv.show();
                dv.resetTable();
                dv.hide();
                break;
        }

    }

}
