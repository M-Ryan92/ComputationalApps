package timetableapp.Gui;

import controlP5.ControlP5;
import java.util.Map;
import processing.core.PApplet;
import timetableapp.eventhandlers.NewFileSelectedHandler;
import timetableapp.util.AppState;
import timetableapp.util.observer.StateObserver;

public abstract class BaseView {

    protected AppState state = AppState.getInstance();
    protected ControlP5 cp5;
    protected PApplet app = state.getApp();
    protected Map<String, ?> properties;

    public BaseView(Map<String, ?> properties) {
        this.properties = properties;
        checkProperties();
        cp5 = new ControlP5(app);
        
    }

    public void hide() {
        cp5.getAll().stream().forEach(ci -> ci.hide());
        app.background(state.getBackgroundcolor());
    }

    public void show() {
        cp5.getAll().stream().forEach(ci -> ci.show());
        app.background(state.getBackgroundcolor());
    }
    
    protected abstract void checkProperties();
}
