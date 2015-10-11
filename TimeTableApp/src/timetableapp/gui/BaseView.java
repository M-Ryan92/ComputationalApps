package timetableapp.gui;

import controlP5.ControlP5;
import controlP5.ControllerInterface;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import processing.core.PApplet;
import timetableapp.util.AppProperties;
import timetableapp.util.state.AppState;

public abstract class BaseView {

    protected AppState state = AppState.getInstance();
    protected ControlP5 cp5 = state.getCp5();
    protected PApplet app = state.getApp();
    protected boolean ishidden;
    @Getter
    private List<ControllerInterface> controllers;

    public ControllerInterface getcontrollerByName(String name) {
        for (ControllerInterface c : controllers) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public BaseView() {
        controllers = new ArrayList<>();
    }

    public void hide() {
        ishidden = true;
        controllers.stream().forEach(ci -> ci.hide());
        app.background(AppProperties.backgroundColor);
    }

    public void showNoControlls() {
        ishidden = false;
        app.background(AppProperties.backgroundColor);
        draw();
    }

    public void show() {
        ishidden = false;
        app.background(AppProperties.backgroundColor);
        controllers.stream().forEach(ci -> {
            ci.show();
        });
        draw();
    }

    public abstract void draw();
}
