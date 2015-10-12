package timetableapp.gui.drawHelper;

import lombok.Getter;
import lombok.Setter;
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

}
