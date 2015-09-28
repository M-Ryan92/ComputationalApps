package timetableapp.Gui;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import processing.core.PApplet;

public class GuiHelper {

    private PApplet app;
    private ControlP5 cp5;
    private int btnheight = 18;

    public GuiHelper(PApplet app) {
        this.app = app;
        cp5 = new ControlP5(app);
    }

    public void setup() {
        cp5.addButton(cp5, "selectFileBtn")
                .setPosition(10, app.height - btnheight - 10)
                .setSize(70, btnheight)
                .setLabel("Select File");

    }

    public void controlEvent(ControlEvent evt) {
        Controller<?> controller = evt.getController();
        if ("selectFileBtn".equals(controller.getName())) {
            app.selectInput("Select a file to process:", "fileSelected");
        }

    }

}
