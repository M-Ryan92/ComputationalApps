package timetableapp.gui.drawHelper;

import controlP5.ControllerInterface;
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
import timetableapp.util.AppProperties;
import timetableapp.util.state.AppState;

public class DrawBuildingVis {

    private PApplet app;
    private PImage enteranceIcon, elevatorIcon, classRoomIcon, classRoomUnavailableIcon;
    private int scale = 7;
    private List<Node> nodes;
    private int boundaryX1, boundaryX2, boundaryY1, boundaryY2;
    private int width, height;

    private int floorYHeight;
    private int y = 25;
    private int spacing = 15;
    private Map<Character, List<ClassRoom>> groupedOnLetter;

    private int fittingEtage;
    private int startetage = 0;
    private int maxPages = 0;
    private int currentPage = 0;
    private int maxEtages = 0;

    public void floorsUp() {
        if (currentPage + 1 <= maxPages) {
            currentPage++;
            startetage = fittingEtage * currentPage;
        }
    }

    public void floorsDown() {
        if (currentPage != 0) {
            currentPage--;
            startetage = fittingEtage * currentPage;
        }
    }

    public void checkBtnState(ControllerInterface ctrl1, ControllerInterface ctrl2) {
        if (currentPage == 0) {
            ctrl1.hide();
        } else {
            ctrl1.show();
        }
        if (currentPage == maxPages) {
            ctrl2.hide();
        } else {
            ctrl2.show();
        }
    }

    public String getEtageRange() {
        int endRange = currentPage > 0 ? startetage + (fittingEtage - 1) : (fittingEtage - 1);
        if (currentPage == maxPages) {
            endRange = maxEtages;
        }
        return startetage + "-" + endRange;
    }

    public DrawBuildingVis(PApplet app) {
        this.app = app;

        width = AppState.getInstance().getDisplayPanelWidth();
        height = AppState.getInstance().getDisplayPanelHeight();

        boundaryX1 = AppProperties.displayPanelXOffset + 20;
        boundaryY1 = AppProperties.displayPanelYOffset + 20;
        boundaryX2 = width - boundaryX1;
        boundaryY2 = height - boundaryY1;

        try {
            elevatorIcon = loadIcon("images/elevatoricon.png");
            enteranceIcon = loadIcon("images/deuricon.png");
            classRoomIcon = loadIcon("images/klasicon.png");
            classRoomUnavailableIcon = loadIcon("images/klasclosedicon.png");
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
        Node n;

        if (cr.isAvailable()) {
            n = new Node(x, y, classRoomIcon.width / scale, classRoomIcon.height / scale);
        } else {
            n = new Node(x, y, classRoomUnavailableIcon.width / scale, classRoomUnavailableIcon.height / scale);
        }

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
                app.fill(AppProperties.displayColor);
                app.rect(n.x, n.y - 22, app.textWidth(text), 19);
                app.stroke(AppProperties.strokeColor);
                app.fill(255);

                app.text(text, n.x + (app.textWidth(text) / 2), n.y - 10);
                break;
            case "classroom":
                if (n.cr.isAvailable()) {
                    app.image(classRoomIcon, n.x, n.y, n.width, n.height);
                } else {
                    app.image(classRoomUnavailableIcon, n.x, n.y, n.width, n.height);
                }
                app.text(n.cr.floorLocation(), n.x + (n.width/2), n.y + (n.height/2) + 3);
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
        app.stroke(AppProperties.strokeColor);
    }

    private void initCoreBuilding(Building building) {
        fittingEtage = 0;
        maxEtages = building.getFloorCount();
        floorYHeight = ((boundaryY2) / maxEtages) + 120;
        if (currentPage == 0) {
            makeEnteranceNode(0, -(y - 60));
        }

        for (int floor : building.getFloorList().keySet()) {
            if ((floor - startetage) >= 0 && boundaryY2 - (floorYHeight * (floor - startetage + 1)) > (boundaryX1 - y)) {
                makeElevatorNode(0, -(floorYHeight * (floor - startetage)) - y, floor);
                fittingEtage++;
            }
        }
        if (maxPages == 0) {
            maxPages = maxEtages / fittingEtage;
        }
        if (currentPage == 0) {
            drawConector(nodes.get(0), nodes.get(1));
        }
    }

    private void initFloor(Building building, int floor) {
        List<List<ClassRoom>> devidedLists;

        Collection<ClassRoom> rooms = building.getFloorList().get(floor).values();
        Optional<Node> foundElevator = nodes.stream().filter(n -> n.floor == floor && "elevator".equals(n.type)).findFirst();
        groupedOnLetter = rooms.stream().collect(Collectors.groupingBy(s -> s.getLetter()));
        Character[] keys = groupedOnLetter.keySet().toArray(new Character[groupedOnLetter.size()]);

        if (keys.length < 2) {
            devidedLists = devideLists(keys, groupedOnLetter.get(keys[0]), new ArrayList<>());
        } else {
            devidedLists = devideLists(keys, groupedOnLetter.get(keys[0]), groupedOnLetter.get(keys[1]));
        }
        drawRooms(devidedLists.get(0), floor, foundElevator, true);
        drawRooms(devidedLists.get(1), floor, foundElevator, false);

    }

    private List<List<ClassRoom>> devideLists(Character[] keys, List<ClassRoom> left, List<ClassRoom> right) {
        int giveRight, giveLeft, dif;
        List<ClassRoom> leftover = new ArrayList();
        List<ClassRoom> subList;

        if (keys.length >= 3) {
            for (int k = 2; k < keys.length; k++) {
                leftover.addAll(groupedOnLetter.get(keys[k]));
            }
        }

        if (left.size() + (leftover.size() / 2) > right.size() + (leftover.size() / 2)) {
            right.addAll(leftover);
            dif = left.size() - right.size();
            subList = left.subList(left.size() - (dif / 2), left.size());
            right.addAll(subList);
            subList = left.subList(0, left.size() - (dif / 2));
            left = subList;
        } else if (left.size() + (leftover.size() / 2) < (right.size() + (leftover.size() / 2))) {
            left.addAll(leftover);
            dif = right.size() - left.size();
            subList = right.subList(right.size() - (dif / 2), right.size());
            left.addAll(subList);
            subList = right.subList(0, right.size() - (dif / 2));
            right = subList;
        } else {
            dif = right.size() - left.size();
            left.addAll(leftover.subList(0, leftover.size() - (dif / 2)));
            right.addAll(leftover.subList(leftover.size() - (dif / 2), leftover.size()));
        }

        List<List<ClassRoom>> results = new ArrayList();
        results.add(left);
        results.add(right);

        return results;
    }

    private void drawRooms(List<ClassRoom> locations, int floor, Optional<Node> foundElevator, boolean isLeft) {

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
            if (currentItem >= locations.size() - 1) {
                x = counter;
            }
            if (currentItem < itemsPerRow) {
                makeClassRoomNode(-x, -(y + (0 * height) + (floorYHeight * (floor - startetage))), floor, cr);
                x += counter;
            } else {
                makeClassRoomNode(-x, -(y + (1 * height) + (floorYHeight * (floor - startetage))), floor, cr);
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

            if (x < AppProperties.displayPanelXOffset) {
                row++;
            }
        }
        if (floor != 0) {
            drawConector(nodes.get(nodes.size() - 1), foundElevator.get());
        } else {
            drawConector(nodes.get(nodes.size() - 1), foundElevator.orElse(nodes.get(0)));
        }

    }

    public void draw(Building building) {
        Draw.drawDisplay();

        app.translate((app.width / 2), boundaryY2);
        initCoreBuilding(building);
        for (int floor : building.getFloorList().keySet()) {
            if ((floor - startetage) >= 0 && floor < (fittingEtage + startetage)) {
                initFloor(building, floor);
            }
        }

        //draw al the connectors for the elevators
        Object[] nArr = nodes.stream().filter(n -> n.type == "elevator").toArray();
        for (int item = 0; item < nArr.length; item++) {
            if (item + 1 < nArr.length) {
                drawConector((Node) nArr[item], (Node) nArr[item + 1]);
            }
        }

        if (currentPage != maxPages) {
            Node n = (Node) nArr[nArr.length - 1];
            drawConector(n, new Node(n.x, -(boundaryY2 - 1), 44, 44));
        }
        if (currentPage > 0) {
            Node n = (Node) nArr[0];
            drawConector(n, new Node(n.x, boundaryY1 - 5, 44, 44));
        }

        //draw all the nodes on screen and clear node list
        nodes.stream().forEach(n -> drawNode(n));
        nodes = new ArrayList<>();
        app.translate(-(app.width / 2), -boundaryY2);

        app.noStroke();
        app.fill(AppProperties.displayColor);
        app.rect( (app.width / 2) - 8, boundaryY1 + 19 , 16, -30);
        app.stroke(AppProperties.strokeColor);
        app.fill(255);
        
        AppState.getInstance().setFontSize(30);
        
        app.text(building.getName()+ " " + building.getCode(), app.width / 2, boundaryY1 + 15);
        AppState.getInstance().setFontSize();        
    }
}
