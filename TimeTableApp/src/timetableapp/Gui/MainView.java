package timetableapp.Gui;

import java.util.Map;
import processing.core.PApplet;
import timetableapp.eventhandlers.NewFileSelectedHandler;
import timetableapp.util.AppState;
import timetableapp.util.observer.StateObserver;

public final class MainView extends BaseView {

    private int btnheight;

    public MainView(Map<String, ?> properties) {
        super(properties);
        btnheight = (Integer) properties.get("btnheight");

        getControllers().add(cp5
                .addButton(cp5, "selectFileBtn")
                .setPosition(20, app.height - btnheight - 20)
                .setSize(70, btnheight)
                .setColorBackground(AppState.buttonColor)
                .setLabel("Select File"));

        state.getNewFileSelectedStateObserver().addObserver(new StateObserver(new NewFileSelectedHandler()));

        state.getLoadingFileStateObserver().addObserver(new StateObserver(() -> {
            if (state.getLoadingFileState() == 1) {
                state.setSelectedViewState(-1);
            }
            return null;
        }));

        state.getFileLoadedStateObserver().addObserver(new StateObserver(() -> {
            state.setSelectedViewState(0);
            return null;
        }));

        getControllers().add(cp5
                .addButton(cp5, "viewData")
                .setPosition(20, app.height - (btnheight * 2) - 30)
                .setSize(70, btnheight)
                .setColorBackground(AppState.buttonColor)
                .setLabel("View Data"));
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
