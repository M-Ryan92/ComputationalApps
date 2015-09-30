package timetableapp.Gui;

import controlP5.ControlP5;
import java.util.Map;
import processing.core.PApplet;
import timetableapp.util.AppState;

public abstract class BaseView {

    protected AppState state = AppState.getInstance();
    protected ControlP5 cp5;
    protected PApplet app = state.getApp();
    protected Map<String, ?> properties;
    protected boolean ishidden;

    public BaseView(Map<String, ?> properties) {
        this.properties = properties;
        checkProperties();
        cp5 = new ControlP5(app);

    }

    public void hide() {
        ishidden = true;
        cp5.getAll().stream().forEach(ci -> ci.hide());
        app.background(state.getBackgroundcolor());
    }

    public void show() {
        ishidden = false;
        app.background(state.getBackgroundcolor());
        cp5.getAll().stream().forEach(ci -> ci.show());
        state.setSelectedViewState(1);
        draw();
    }

    protected abstract void checkProperties();
    protected abstract void draw();
}
