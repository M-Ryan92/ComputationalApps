package timetableapp.Gui;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import java.awt.Frame;
import javax.swing.JOptionPane;
import processing.core.PApplet;
import timetableapp.util.AppState;

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
        cp5.addButton(cp5, "viewData")
                .setPosition(10, app.height - (btnheight * 2) - 20)
                .setSize(70, btnheight)
                .setLabel("View Data");
    }

    public void controlEvent(ControlEvent evt) {
        Controller<?> controller = evt.getController();
        switch(controller.getName()){
        case("selectFileBtn"):
            app.selectInput("Select a file to process:", "fileSelected");
            break;
        case("viewData"):
            if(AppState.FILE_LOADED == 0){
                new Dialog(null,"No File Selected", Dialog.WARNING_MESSAGE);
            } else {
                //showmydataTable(pagina's)
                
            }
            break;
        }

    }

}
