package timetableapp.gui.views;

import controlP5.Button;
import lombok.Getter;
import timetableapp.eventhandlers.NewFileSelectedHandler;
import timetableapp.gui.BaseView;
import timetableapp.gui.drawHelper.Draw;
import timetableapp.gui.drawHelper.DrawBuildingVis;
import timetableapp.models.DataManager;
import timetableapp.util.Properties;
import timetableapp.util.observer.StateObserver;
import timetableapp.util.state.ViewStates;

public final class MainView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    @Getter
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

        getControllers().add(cp5
                .addButton(cp5, "viewData")
                .setColorBackground(Properties.buttonColor)
                .setPosition(20, app.height - (Properties.buttonHeight * 2) - 30)
                .setSize(70, Properties.buttonHeight)
                .setLabel("View Data")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "floorUp")
                .setColorBackground(Properties.buttonColor)
                .setPosition((app.width / 2) - 10, state.getDisplayPanelHeight() + Properties.buttonHeight)
                .setSize(20, Properties.buttonHeight)
                .setLabel(Character.toString('\uf062'))
                .hide());
        ((Button) getcontrollerByName("floorUp")).getCaptionLabel().setFont(state.getIconFont());

        getControllers().add(cp5
                .addButton(cp5, "floorDown")
                .setColorBackground(Properties.buttonColor)
                .setPosition((app.width / 2) - 10, state.getDisplayPanelHeight() + (Properties.buttonHeight * 3))
                .setSize(20, Properties.buttonHeight)
                .setLabel(Character.toString('\uf063'))
                .hide());
        ((Button) getcontrollerByName("floorDown")).getCaptionLabel().setFont(state.getIconFont());

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

        state.getFileLoadedStateObserver().addObserver(new StateObserver(() -> {
            if (state.getFileLoadedState() == 1) {
                getcontrollerByName("viewData").show();
                getcontrollerByName("floorUp").show();
                getcontrollerByName("floorDown").show();
            }
            return null;
        }));

    }

    public void checkState() {
        dbv.checkBtnState(getcontrollerByName("floorDown"), getcontrollerByName("floorUp"));
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
                checkState();
                app.text(dbv.getEtageRange(), (app.width / 2), state.getDisplayPanelHeight() + (Properties.buttonHeight * 3) - 8);
            }
        }
    }

}
