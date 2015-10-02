package timetableapp.Gui;

import java.util.Map;
import processing.core.PApplet;
import timetableapp.eventhandlers.NewFileSelectedHandler;
import timetableapp.util.AppState;
import timetableapp.util.ViewStates;
import timetableapp.util.observer.StateObserver;

public final class MainView extends BaseView {

    private int btnheight;

    public MainView(Map<String, ?> properties) {
        super(properties);
        btnheight = (Integer) properties.get("btnheight");

        getControllers().add(cp5
                .addButton(cp5, "selectFileBtn")
                .setColorBackground(AppState.buttonColor)
                .setPosition(20, app.height - btnheight - 20)
                .setSize(70, btnheight)
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
                .setPosition(20, app.height - (btnheight * 2) - 30)
                .setSize(70, btnheight)
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
    protected void checkProperties() {
        if (!properties.containsKey("btnheight")) {
            new Dialog().fatalErrorDialog("could not render application view, closing application");
        }
    }

    @Override
    protected void draw() {
        int w = app.width - 40;
        int h = app.height - 120;

        if (ishidden == false) {
            app.fill(AppState.displayColor);
            app.rect(20, 20, w, h);
            app.fill(AppState.textColor);

            if (state.getFileLoadedState() != 1) {
                state.setFont(26);
                app.textAlign(PApplet.CENTER);
                app.text("no file selected", w / 2, (h / 2) - 70);
                state.setFont();
            }
        }
    }

}
