package timetableapp.gui.views;

import timetableapp.eventhandlers.NewFileSelectedHandler;
import timetableapp.gui.BaseView;
import timetableapp.gui.drawHelper.Draw;
import timetableapp.gui.drawHelper.DrawBuildingVis;
import timetableapp.models.DataManager;
import timetableapp.util.Properties;
import timetableapp.util.state.ViewStates;
import timetableapp.util.observer.StateObserver;

public final class MainView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    private DrawBuildingVis dbv;
    
    public MainView() {
        super();
        dbv = new DrawBuildingVis(app);
        getControllers().add(cp5
                .addButton(cp5, "selectFileBtn")
                .setColorBackground(Properties.buttonColor)
                .setPosition(20, app.height - Properties.buttonHeight - 20)
                .setSize(70, Properties.buttonHeight)
                .setLabel("Select File"));

        state.getNewFileSelectedStateObserver().addObserver(new StateObserver(new NewFileSelectedHandler()));

        state.getLoadingFileStateObserver().addObserver(new StateObserver(() -> {
            if (state.getLoadingFileState() == 1) {
                state.setSelectedViewState(ViewStates.LoadView);
            }
            return null;
        }));

        state.getFileLoadedStateObserver().addObserver(new StateObserver(() -> {
            state.setSelectedViewState(ViewStates.MainView);
            return null;
        }));

        getControllers().add(cp5
                .addButton(cp5, "viewData")
                .setColorBackground(Properties.buttonColor)
                .setPosition(20, app.height - (Properties.buttonHeight * 2) - 30)
                .setSize(70, Properties.buttonHeight)
                .setLabel("View Data")
                .hide());
        state.getFileLoadedStateObserver().addObserver(new StateObserver(() -> {
            if (state.getFileLoadedState() == 1) {
                getcontrollerByName("viewData").show();
            }
            return null;
        }));
    }

    @Override
    public void draw() {
        if (ishidden == false) {
            Draw.drawDisplay();

            if (state.getFileLoadedState() != 1) {
                Draw.drawDisplayMessage("no file selected");
            } else {
                //do some epic drawing magic =D
                dbv.draw(dm.getBl().get("WBH"));
            }
        }
    }

}
