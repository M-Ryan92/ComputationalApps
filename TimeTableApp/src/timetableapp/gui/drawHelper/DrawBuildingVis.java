package timetableapp.gui.drawHelper;

import java.awt.Color;
import processing.core.PApplet;
import processing.core.PImage;
import timetableapp.models.Building;
import timetableapp.util.Properties;

public class DrawBuildingVis {

    private PApplet app;
    private PImage enteranceIcon, elevatorIcon;
    private int scale = 4;

    public DrawBuildingVis(PApplet app) {
        this.app = app;
        elevatorIcon = app.loadImage(getClass().getResource("../../resources/images/elevatoricon.png").getFile());
        enteranceIcon = app.loadImage(getClass().getResource("../../resources/images/deuricon.png").getFile());
    }

    private void drawEnterance(int x, int y) {
        app.image(enteranceIcon, x, y, enteranceIcon.width / scale, enteranceIcon.height / scale);
    }

    private void drawElevator(int x, int y) {
        app.image(elevatorIcon, x, y, elevatorIcon.width / scale, elevatorIcon.height / scale);
    }

    public void draw(Building building) {
        Draw.drawDisplay();

        //1. fist make the base of the building so lets make the fundation and the enterance might be handy ^^
        app.stroke(Color.decode("#a6e63c").getRGB());
        app.strokeWeight(10);
        app.line(40 + ((enteranceIcon.width/ scale) / 2), 40 + ((enteranceIcon.height/ scale) / 2), 40 + 10 + (enteranceIcon.width/ scale) + ( (elevatorIcon.width/ scale) / 2), 40 + 10 + (enteranceIcon.height/ scale) + ((elevatorIcon.height/ scale) / 2));
        app.strokeWeight(1);
        app.stroke(Properties.strokeColor);
        drawEnterance(40, 40);
        drawElevator(40 + 10 + (enteranceIcon.width/ scale), 40 + 10 + (enteranceIcon.height/ scale));

        //2. then draw the elevator and connect it to the enterance and connect the existing rooms to the enterance / elevator w/e u want
        //3. when the ground level is completely drawn draw a horizontal line to separate floors.
        //4. for the next floor 1st create a new elevator and connect the existing rooms to the elevator again 
        //5. after the next floor is complete draw a horizontal line to separate the floors
        //6. check if the previous floor was the last floor is no repeat step 4 else continue 
    }
}
