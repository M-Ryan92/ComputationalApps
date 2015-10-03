package timetableapp.gui.views;

import processing.core.PApplet;
import timetableapp.eventhandlers.NewFileSelectedHandler;
import timetableapp.gui.BaseView;
import timetableapp.gui.drawHelper.Draw;
import timetableapp.util.AppState;
import timetableapp.util.ViewStates;
import timetableapp.util.observer.StateObserver;

public final class MainView extends BaseView {

    public MainView() {
        super();

        getControllers().add(cp5
                .addButton(cp5, "selectFileBtn")
                .setColorBackground(AppState.buttonColor)
                .setPosition(20, app.height - state.getButtonHeight()- 20)
                .setSize(70, state.getButtonHeight())
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
                .setColorBackground(AppState.buttonColor)
                .setPosition(20, app.height - (state.getButtonHeight() * 2) - 30)
                .setSize(70, state.getButtonHeight())
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
                state.setFont(26);
                app.textAlign(PApplet.CENTER);
                app.text("no file selected", state.getDisplayPanelWidth()/ 2, (state.getDisplayPanelHeight() / 2) - 70);
                state.setFont();
            }
        }
    }

}
