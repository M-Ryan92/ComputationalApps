package timetableapp.Gui;

import java.util.Map;
import timetableapp.eventhandlers.NewFileSelectedHandler;
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
                .setLabel("Select File"));

        state.getNewFileSelectedStateObserver().addObserver(new StateObserver(new NewFileSelectedHandler()));
        
        getControllers().add(cp5
                .addButton(cp5, "viewData")
                .setPosition(20, app.height - (btnheight * 2) - 30)
                .setSize(70, btnheight)
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
        if (ishidden == false) {
            app.rect(20, 20, app.width - 40, app.height - 170);
        }
    }

}
