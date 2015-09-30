package timetableapp.Gui;

import controlP5.ControlP5;
import java.util.Map;
import processing.core.PApplet;
import timetableapp.util.AppState;

public class DataView {

    private boolean ishidden;
    private AppState state = AppState.getInstance();
    private ControlP5 cp5;
    private PApplet app = state.getApp();
    private int btnheight;

    public DataView(Map<String, ?> properties) {
        btnheight = (Integer) properties.get("btnheight");
        ishidden = true;
        cp5 = new ControlP5(app);
    }

    public void hide() {
        ishidden = true;
        cp5.getAll().stream().forEach(ci -> ci.hide());
        app.background(state.getBackgroundcolor());
    }

    public void show() {
        ishidden = false;
        cp5.getAll().stream().forEach(ci -> ci.show());
        app.background(state.getBackgroundcolor());
        draw();
    }

    private void draw() {
        if (ishidden == false) {
            app.rect(20, 50, app.width - 40, app.height - 200);
        }
    }
}
