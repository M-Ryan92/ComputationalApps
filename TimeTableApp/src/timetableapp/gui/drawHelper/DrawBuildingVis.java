package timetableapp.gui.drawHelper;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import processing.core.PApplet;
import processing.core.PImage;
import timetableapp.gui.Dialog;
import timetableapp.models.Building;
import timetableapp.models.ClassRoom;
import timetableapp.util.Properties;
import timetableapp.util.state.AppState;

public class DrawBuildingVis {

    private PApplet app;
    private PImage enteranceIcon, elevatorIcon, classRoomIcon;
    private int scale = 7;
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

        try {
            elevatorIcon = loadIcon("images/elevatoricon.png");
            enteranceIcon = loadIcon("images/deuricon.png");
            classRoomIcon = loadIcon("images/klasicon.png");
        } catch (IOException ex) {
            new Dialog().fatalErrorDialog("error occured app closes now =C");
        }

        nodes = new ArrayList<>();
    }

    private PImage loadIcon(String path) throws IOException {
        PImage img = app.loadImage(getClass().getResource(path).getFile());
        if (img == null) {
            img = app.loadImage(getClass().getResource(path).openConnection().getURL().toString());
        }
        return img;
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

    private void makeClassRoomNode(int x, int y, int floor, ClassRoom cr) {
        Node n = new Node(x, y, classRoomIcon.width / scale, classRoomIcon.height / scale);
        centerDrawing(n);
        n.cr = cr;
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
                String text = "etage " + n.floor;

                app.noStroke();
                app.fill(Properties.displayColor);
                app.rect(n.x, n.y - 22, app.textWidth(text), 19);
                app.fill(255);
                app.stroke(Properties.strokeColor);

                app.text(text, n.x + (app.textWidth(text) / 2), n.y - 10);
                break;
            case "classroom":
                app.image(classRoomIcon, n.x, n.y, n.width, n.height);
                app.text(n.cr.floorLocation(), n.x, n.y);
                break;
        }
    }

    private class Node {

        public int x, y;
        public int width, height;
        public String type;
        public int floor;
        public ClassRoom cr;

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
    private int spacing = 15;
    private Map<Character, List<ClassRoom>> groupedOnLetter;
    private int etage;

    private void initCoreBuilding(Building building) {
        //create enterance and elevator nodes
        etage = 0;

        makeEnteranceNode(0, -(y - 60));
        floorYHeight = ((boundaryY2) / building.getEtageCount()) + 120;
        for (int floor : building.getFloorList().keySet()) {
            if (boundaryY2 - (floorYHeight * (floor + 1)) > (boundaryX1 - y)) {
                makeElevatorNode(0, -(floorYHeight * floor) - y, floor);
                etage++;
            }
        }
        System.out.println("");
    }

    private void initFloor(Building building, int floor) {
        Collection<ClassRoom> rooms = building.getFloorList().get(floor).values();
        Optional<Node> foundElevator = nodes.stream().filter(n -> n.floor == floor && "elevator".equals(n.type)).findFirst();

        groupedOnLetter = rooms.stream().collect(Collectors.groupingBy(s -> s.getLetter()));
        Character[] keys = groupedOnLetter.keySet().toArray(new Character[groupedOnLetter.size()]);

        int letter = 0;
        for (Character c : keys) {
            if (letter < 2) {
                drawRooms(c, floor, foundElevator, true);
                drawConector(nodes.get(nodes.size() - 1), foundElevator.orElse(nodes.get(0)));
                letter++;
            }
        }
    }

    private void drawRooms(Character key, int floor, Optional<Node> foundElevator, boolean isLeft) {
        List<ClassRoom> locations = groupedOnLetter.get(key);
        if (Character.isLetter(key) && Character.compare(key, 'B') == 0) {
            isLeft = false;
        }
        int height = 50 + 5;
        int row = 0;
        int itemsPerRow = (locations.size() / 2);
        int currentItem = 0;
        int x, counter;
        if (isLeft) {
            x = 70;
            counter = 70;
        } else {
            x = -70;
            counter = -70;
        }

        for (ClassRoom cr : locations) {
            if (currentItem < itemsPerRow) {
                makeClassRoomNode(-x, -(y + (0 * height) + (floorYHeight * floor)), floor, cr);
                x += counter;
            } else {
                makeClassRoomNode(-x, -(y + (1 * height) + (floorYHeight * floor)), floor, cr);
                x -= counter;
            }

            if (currentItem >= 1) {
                drawConector(nodes.get(nodes.size() - 2), nodes.get(nodes.size() - 1));
            } else {
                drawConector(foundElevator.orElse(nodes.get(0)), nodes.get(nodes.size() - 1));
                if (floor == 0) {
                    drawConector(nodes.get(0), nodes.get(nodes.size() - 1));
                }
            }
            currentItem++;

            if (x < Properties.displayPanelXOffset) {
                row++;
            }
        }
    }

    public void draw(Building building) {
        Draw.drawDisplay();

        app.translate((app.width / 2), boundaryY2);
        initCoreBuilding(building);
        for (int floor : building.getFloorList().keySet()) {
            if (floor < etage) {
                initFloor(building, floor);
            }
        }

        // connect the entrance to the elevator
        drawConector(nodes.get(0), nodes.get(1));
        //draw al the connectors for the elevators
        Object[] nArr = nodes.stream().filter(n -> n.type == "elevator").toArray();
        for (int item = 0; item < nArr.length; item++) {
            if (item + 1 < nArr.length) {
                drawConector((Node) nArr[item], (Node) nArr[item + 1]);

            }
        }

        if (nArr.length < building.getFloorList().size()) {
            Node n = (Node) nArr[nArr.length - 1];
            drawConector(n, new Node(n.x, -(boundaryY2 - 1), 44, 44));
        }

        //draw all the nodes on screen and clear node list
        nodes.stream().forEach(n -> drawNode(n));
        nodes = new ArrayList<>();
        app.translate(-(app.width / 2), -boundaryY2);
    }
}
