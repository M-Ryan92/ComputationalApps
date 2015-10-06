package timetableapp.gui.drawHelper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;
import processing.core.PImage;
import timetableapp.models.Building;
import timetableapp.models.ClassRoom;
import timetableapp.util.Properties;
import timetableapp.util.state.AppState;

public class DrawBuildingVis {

    private PApplet app;
    private PImage enteranceIcon, elevatorIcon, classRoomIcon;
    private int scale = 6;
    private List<Node> nodes;
    private int boundaryX1, boundaryX2, boundaryY1, boundaryY2;
    private int width, height;

    public DrawBuildingVis(PApplet app) {
        this.app = app;

        width = AppState.getInstance().getDisplayPanelWidth();
        height = AppState.getInstance().getDisplayPanelHeight();

        boundaryX1 = Properties.displayPanelXOffset + 20;
        boundaryY1 = Properties.displayPanelYOffset + 20;
        boundaryX2 = width - boundaryX1;
        boundaryY2 = height - boundaryY1;

        elevatorIcon = app.loadImage(getClass().getResource("../../resources/images/elevatoricon.png").getFile());
        enteranceIcon = app.loadImage(getClass().getResource("../../resources/images/deuricon.png").getFile());
        classRoomIcon = app.loadImage(getClass().getResource("../../resources/images/klasicon.png").getFile());
        nodes = new ArrayList<>();
    }

    private void makeEnteranceNode(int x, int y) {
        Node n = new Node(x, y, enteranceIcon.width / scale, enteranceIcon.height / scale);
        centerDrawing(n);
        n.type = "enterance";
        n.floor = 0;
        nodes.add(n);
    }

    private void makeElevatorNode(int x, int y, int floor) {
        Node n = new Node(x, y, elevatorIcon.width / scale, elevatorIcon.height / scale);
        centerDrawing(n);
        n.type = "elevator";
        n.floor = floor;
        nodes.add(n);
    }

    private void makeClassRoomNode(int x, int y, int floor) {
        Node n = new Node(x, y, classRoomIcon.width / scale, classRoomIcon.height / scale);
        centerDrawing(n);
        n.type = "classroom";
        n.floor = floor;
        nodes.add(n);
        spacing += 15;
    }

    private void centerDrawing(Node n) {
        n.x = n.x - (n.width / 2);
        n.y = n.y - (n.height / 2);
    }

    private void drawNode(Node n) {
        switch (n.type) {
            case "enterance":
                app.image(enteranceIcon, n.x, n.y, n.width, n.height);
                break;
            case "elevator":
                app.image(elevatorIcon, n.x, n.y, n.width, n.height);
                String text = "etage  " + n.floor;
                app.text(text, n.x + (app.textWidth(text) / 5), n.y);
                break;
            case "classroom":
                app.image(classRoomIcon, n.x, n.y, n.width, n.height);
                break;
        }
    }

    private class Node {

        public int x, y;
        public int width, height;
        public String type;
        public int floor;

        public Node(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    private void drawConector(Node one, Node two) {
        app.stroke(Color.decode("#a6e63c").getRGB());
        app.strokeWeight(5);

        app.line(one.x + (one.width / 2), one.y + (one.height / 2), two.x + (two.width / 2), two.y + (two.height / 2));

        app.strokeWeight(1);
        app.stroke(Properties.strokeColor);
    }

    private int floorYHeight;
    private int y = 25;

    private void initCoreBuilding(Building building) {
        //create enterance and elevator nodes

        makeEnteranceNode(-60, -(y - 50));
        floorYHeight = ((boundaryY2) / building.getEtageCount()) + 115;
        for (int etage : building.getFloorList().keySet()) {
            if (boundaryY2 - (floorYHeight * etage) - y > boundaryX1) {
                makeElevatorNode(0, -(floorYHeight * etage) - y, etage);
            }
        }
    }

    private void initBuildingFloorClassRooms(int floor, Map<String, ClassRoom> classrooms) {
        String partsString = "";
        for (Iterator<Map.Entry<String, ClassRoom>> iterator = classrooms.entrySet().iterator(); iterator.hasNext();) {
            String sub = iterator.next().getKey().substring(0, 1);
            if (!partsString.contains(sub)) {
                partsString += sub;
            }
        }
        char[] tochars = partsString.toCharArray();

        int x = -120;
        int y = this.y;
        for (char c : tochars) {
            //filter the map and parse these in the view
            for (ClassRoom cr : classrooms.values()) {
                if (!Character.toString(c).equals(Character.toString(cr.getLetter()))) {
                    break;
                }
//                makeClassRoomNode(x, -y, floor);
                break;
            }
            break;
        }

    }
    private int spacing = 15;

    private void testdrawRooms(int floor){
        int h = 50 + 5;
        
        for (int i = 0; i < 3; i++) {
            makeClassRoomNode(- 50 - spacing, -(y + (i * h) + (floorYHeight * floor)), 0);
            makeClassRoomNode(-100 - spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(-150 - spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(-200 - spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(-250 - spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(-300 - spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(-350 - spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(-400 - spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(-450 - spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(-500 - spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            spacing = 15;
        }
        for (int i = 0; i < 3; i++) {
            makeClassRoomNode( 50 + spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(100 + spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(150 + spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(200 + spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(250 + spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(300 + spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(350 + spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(400 + spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(450 + spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            makeClassRoomNode(500 + spacing, -(y + (i * h)+ (floorYHeight * floor)), 0);
            spacing = 15;
        }    
    }
    
    public void draw(Building building) {
        Draw.drawDisplay();
        
        app.translate((app.width / 2), boundaryY2);
        initCoreBuilding(building);
        initBuildingFloorClassRooms(0, building.getFloorList().get(0));
        testdrawRooms(0);
        testdrawRooms(1);
        testdrawRooms(2);
        testdrawRooms(3);
        // connect the entrance to the elevator
        drawConector(nodes.get(0), nodes.get(1));
        //draw al the connectors for the elevators
        Object[] nArr = nodes.stream().filter(n -> n.type == "elevator").toArray();
        for (int item = 0; item < nArr.length; item++) {
            if (item + 1 < nArr.length) {
                drawConector((Node) nArr[item], (Node) nArr[item + 1]);

            }
        }

        if(nArr.length < building.getFloorList().size()){
            Node n = (Node) nArr[nArr.length -1];
            drawConector(n, new Node(n.x, -(boundaryY2 - 1), 44, 44));
        }
        
        //draw all the nodes on screen and clear node list
        nodes.stream().forEach(n -> drawNode(n));
        nodes = new ArrayList<>();
        app.translate(-(app.width / 2), -boundaryY2);
    }
}
