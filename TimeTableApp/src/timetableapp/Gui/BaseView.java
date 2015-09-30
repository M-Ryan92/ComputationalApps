package timetableapp.Gui;

import controlP5.ControlP5;
import controlP5.ControllerInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import processing.core.PApplet;
import timetableapp.util.AppState;

public abstract class BaseView {

    protected AppState state = AppState.getInstance();
    protected ControlP5 cp5 = state.getCp5();
    protected PApplet app = state.getApp();
    protected Map<String, ?> properties;
    protected boolean ishidden;
    @Getter
    private List<ControllerInterface> controllers;

    public BaseView(Map<String, ?> properties) {
        this.properties = properties;
        controllers = new ArrayList<>();
        checkProperties();
    }

    public void hide() {
        ishidden = true;
        controllers.stream().forEach(ci -> ci.hide());
        app.background(state.getBackgroundcolor());
    }

    public void show() {
        ishidden = false;
        app.background(state.getBackgroundcolor());
        controllers.stream().forEach(ci -> {
            ci.show();
        });
        state.setSelectedViewState(1);
        draw();
    }

    protected abstract void checkProperties();

    protected abstract void draw();
}
