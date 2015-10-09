package timetableapp;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.event.KeyEvent;
import timetableapp.gui.GuiHelper;
import timetableapp.util.Properties;
import timetableapp.util.state.AppState;

public class BaseApplication extends PApplet {

    private GuiHelper guiHelper;

    @Override
    public void settings() {
        height = displayHeight;
        width = displayWidth;
        size(width, height);
    }

    @Override
    public void setup() {
        AppState state = AppState.getInstance();
        
        state.setApp(this);
        state.setCp5(new ControlP5(this));
        state.setFontSize();
        
        frameRate(60);
        guiHelper = new GuiHelper();
        background(Properties.backgroundColor);
    }

    int start = 0;

    @Override
    public void draw() {
        stroke(Properties.strokeColor);
        guiHelper.draw();
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        guiHelper.keyPressed(evt);
    }

    public void controlEvent(ControlEvent evt) {
        guiHelper.controlEvent(evt);
    }
}
