package timetableapp.gui.drawHelper;

import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import timetableapp.models.ClassRoom;

public class Node {

    @Setter
    @Getter
    private int x, y, width, height, floor;
    @Setter
    @Getter
    private String type;
    @Setter
    @Getter
    private ClassRoom cr;

    public Node(int x, int y, int width, int height) {
        this.x = x - (width / 2);
        this.y = y - (height / 2);
        this.width = width;
        this.height = height;
    }

    boolean containsMouse(PApplet app, String type) {
        if(this.type.equals(type)){
            return (app.sq(this.x + (this.width / 2) - app.mouseX) + app.sq(this.y + (this.height / 2) - app.mouseY)) < app.sq(this.width / 2);
        } else {
            return false;
        }
    }

}
