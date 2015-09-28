package timetableapp;

import controlP5.ControlEvent;
import processing.core.PApplet;

public class BaseApplication extends PApplet {

    GuiHelper guiHelper;

    @Override
    public void settings() {
        height = displayHeight - 200;
        width = displayWidth - 500;
        size(width, height);
    }

    @Override
    public void setup() {
        frameRate(60);
        guiHelper = new GuiHelper(this);
        guiHelper.setup();
    }

    @Override
    public void draw() {

    }
    public void controlEvent(ControlEvent evt) {
    guiHelper.controlEvent(evt);
    }
}

