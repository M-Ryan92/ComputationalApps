package timetableapp.gui.drawHelper;

import controlP5.ControllerInterface;
import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import processing.core.PApplet;
import processing.core.PImage;
import timetableapp.gui.Dialog;
import timetableapp.models.Activity;
import timetableapp.models.Building;
import timetableapp.models.ClassRoom;
import timetableapp.util.AppProperties;
import timetableapp.util.state.AppState;

public class DrawBuildingVis {

    //base draw properties
    private PApplet app;
    private PImage enteranceIcon, elevatorIcon, classRoomIcon, classRoomUnavailableIcon;
    private int scale = 7;
    private List<Node> nodes;
    private int displayWidth, displayHeight, boundaryX1, boundaryX2, boundaryY1,
            boundaryY2;
    private int centerX, centerY;
    private int spacing = 70;

    //floor properties
    private int floorHeight = 150;

    //pagination
    private int nrOfEtageFits = 0;
    private boolean isKnownEtageFits = false;
    private int startetage = 0;
    private int maxPages = 0;
    private int currentPage = 0;
    private int maxEtages = 0;

    public DrawBuildingVis(PApplet app) {
        this.app = app;
        displayWidth = AppState.getInstance().getDisplayPanelWidth();
        displayHeight = AppState.getInstance().getDisplayPanelHeight();

        boundaryX1 = AppProperties.displayPanelXOffset + 20;
        boundaryY1 = AppProperties.displayPanelYOffset + 20;
        boundaryX2 = displayWidth - spacing;
        boundaryY2 = displayHeight - spacing;

        centerX = (boundaryX2 / 2) + boundaryX1 + 20;
        centerY = (boundaryY2 / 2) + boundaryY1;

        nodes = new ArrayList<>();

        try {
            elevatorIcon = loadIcon("images/elevatoricon.png");
            enteranceIcon = loadIcon("images/deuricon.png");
            classRoomIcon = loadIcon("images/klasicon.png");
            classRoomUnavailableIcon = loadIcon("images/klasclosedicon.png");
        } catch (IOException ex) {
            new Dialog().fatalErrorDialog("error occured app closes now =C");
        }
    }

    private PImage loadIcon(String path) throws IOException {
        PImage img = app.loadImage(getClass().getResource(path).getFile());
        if (img == null) {
            img = app.loadImage(getClass().getResource(path).openConnection().getURL().toString());
        }
        return img;
    }

    public void reset() {
        isKnownEtageFits = false;
        startetage = 0;
        maxPages = 0;
        currentPage = 0;
        nrOfEtageFits = 0;
        maxEtages = 0;
        nodes = new ArrayList<>();
    }

    public void floorsUp() {
        if (currentPage + 1 <= maxPages) {
            currentPage++;
            startetage = nrOfEtageFits * currentPage;
            nodes = new ArrayList<>();
        }
    }

    public void floorsDown() {
        if (currentPage != 0) {
            currentPage--;
            startetage = nrOfEtageFits * currentPage;
            nodes = new ArrayList<>();
        }
    }

    public String getEtageRange() {
        int endRange = currentPage > 0 ? startetage + (nrOfEtageFits - 1) : (nrOfEtageFits - 1);
        if (currentPage == maxPages) {
            endRange = maxEtages;
        }
        return startetage + "-" + endRange;
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

    private void connectingNodes() {
        if (maxEtages > 0 && currentPage == 0) {
            drawConector(nodes.get(0), nodes.get(1));
        }

        List<Node> elevators = nodes.stream().filter(n -> "elevator".equals(n.getType())).collect(Collectors.toList());
        Map<Integer, List<Node>> roomsEachFloor = nodes.stream().filter(n -> "classroom".equals(n.getType())).collect(Collectors.groupingBy((n) -> n.getFloor()));
        if (elevators.size() > 0) {
            for (int item = 0; item < elevators.size(); item++) {
                boolean isleft = true;
                if (item + 1 < elevators.size()) {
                    drawConector(elevators.get(item), elevators.get(item + 1));
                }
                // ((currentPage==0 && item == 0) || currentPage >=1) &&
                if (elevators.get(item) != null && roomsEachFloor.get(elevators.get(item).getFloor()) != null) {
                    List<Node> rooms = roomsEachFloor.get(elevators.get(item).getFloor());
                    int roomCount = 0;
                    for (int i = 0; i < rooms.size(); i++) {
                        Node room = rooms.get(i);

                        if (i == 0) {
                            drawConector(elevators.get(item), room);
                        }
                        if (i + 1 < rooms.size()) {
                            if (isleft == true) {
                                if (rooms.get(i + 1).getX() <= centerX - 70) {
                                    drawConector(room, rooms.get(i + 1));
                                } else {
                                    isleft = false;
                                    drawConector(elevators.get(item), room);
                                    drawConector(elevators.get(item), rooms.get(i + 1));
                                }
                            }
                            if (isleft == false) {
                                if (rooms.get(i + 1).getX() >= centerX + 70) {
                                    drawConector(room, rooms.get(i + 1));
                                }
                            }
                        }
                        if (i + 1 == rooms.size()) {
                            drawConector(elevators.get(item), room);
                            if (i - 1 >= 0 && rooms.get(i - 1).getX() >= centerX + 40) {
                                drawConector(room, rooms.get(i - 1));
                            }
                        }
                    }
                }
            }
        }

        if (currentPage != maxPages && elevators.size() > 1) {
            Node n = elevators.get(elevators.size() - 2);
            drawConector(n, new Node(n.getX() + (n.getWidth() / 2), boundaryY1 - 17, 0, 0));
        }
        if (currentPage > 0 && elevators.size() >= 1) {
            Node n = elevators.get(elevators.size() - 1);
            drawConector(n, new Node(n.getX() + (n.getWidth() / 2), boundaryY2 + spacing + 17, 0, 0));
        }

    }

    public void draw(Building building) {
        Draw.drawDisplay();
        initializeNodes(building);
        connectingNodes();

        nodes.forEach(n -> drawNode(n));
        nodes.forEach(n -> {
            if (n.containsMouse(app, "classroom")) {
                String text = "  in use: 24:00 - 24:00 ";
                int y = 30;
                app.fill(Color.decode("#6EADC0").getRGB());
                if (!n.getCr().getActivities().isEmpty()) {
                    app.rect(n.getX() + n.getWidth(), n.getY() + n.getHeight() , app.textWidth(text) + 10, (n.getCr().getActivities().size() * 30) + y);
                } else {
                    app.rect(n.getX() + n.getWidth(), n.getY() + n.getHeight(), app.textWidth(text) + 10, y);
                }
                app.fill(255);

                if (!n.getCr().getActivities().isEmpty()) {
                    for (Activity a : n.getCr().getActivities()) {
                        text = "  in use: "
                                + DateFormat.getInstance().format(a.getStartDate().getTime()).substring(8) + " - "
                                + DateFormat.getInstance().format(a.getEndDate().getTime()).substring(8);
                        app.text(text,
                                n.getX() + n.getWidth() + (app.textWidth(text) / 2), n.getY() + n.getHeight() + y);
                        y += 30;
                    }
                }
            }
        });

        //building lable
        app.noStroke();
        app.fill(AppProperties.displayColor);
        app.rect((app.width / 2) - 20, boundaryY1 - 10, 30, 30);
        app.stroke(AppProperties.strokeColor);
        app.fill(255);

        AppState.getInstance().setFontSize(30);
        app.text(building.getName() + " " + building.getCode(), app.width / 2, boundaryY1 + 15);
        AppState.getInstance().setFontSize();
    }

    private void initializeNodes(Building building) {
        if (nodes.isEmpty()) {
            maxEtages = building.getFloorCount();

            if (currentPage == 0) {
                makeEnteranceNode();
            }

            if (maxEtages >= 1) {
                for (int floor = startetage; floor <= maxEtages; floor++) {
                    if (boundaryY2 - (floorHeight * (floor - startetage + 1)) > (boundaryY1 - 10)) {
                        makeElevatorNode(centerX, boundaryY2 - (floorHeight * (floor - startetage)), floor);
                        if (isKnownEtageFits == false) {
                            nrOfEtageFits++;
                        }
                        if (floor != 0) {
                            initializefloorNodes(building, floor);
                        }

                    }
                }
                isKnownEtageFits = true;
                if (maxPages == 0) {
                    maxPages = maxEtages / nrOfEtageFits;
                }

            }
        }
    }

    Map<Character, List<ClassRoom>> groupedOnLetter;

    private void initializefloorNodes(Building building, int floor) {
        List<List<ClassRoom>> devidedLists;
        Collection<ClassRoom> rooms = null;
        if (building.getFloorList().get(floor) != null) {
            rooms = building.getFloorList().get(floor).values();
        }
        if (rooms != null) {
            groupedOnLetter = rooms.stream().collect(Collectors.groupingBy(s -> s.getLetter()));

            Character[] keys = groupedOnLetter.keySet().toArray(new Character[groupedOnLetter.size()]);

            if (keys.length == 1) {
                devidedLists = devideLists(keys, groupedOnLetter.get(keys[0]), new ArrayList<>());
            } else {
                devidedLists = devideLists(keys, groupedOnLetter.get(keys[0]), groupedOnLetter.get(keys[1]));
            }

            Node n = nodes.get(nodes.size() - 1);
            initializeRooms(devidedLists.get(0), floor, n, true);
            initializeRooms(devidedLists.get(1), floor, n, false);
        }
    }

    private void initializeRooms(List<ClassRoom> locations, int floor, Node n, boolean isLeft) {
        int x, counter;
        int currentItem = 0;
        int itemsPerRow = (locations.size() / 2);

        if (isLeft) {
            x = centerX - 70;
            counter = -70;
        } else {
            x = centerX + 70;
            counter = 70;
        }

        for (ClassRoom cr : locations) {
            if (currentItem >= locations.size() - 1) {
                x = centerX + counter;
            }
            if (currentItem < itemsPerRow) {
                makeClassRoomNode(x, (boundaryY2 - (floorHeight * (floor - startetage))), floor, cr);
                x += counter;
            } else {
                makeClassRoomNode(x, (boundaryY2 - 55 - (floorHeight * (floor - startetage))), floor, cr);
                x -= counter;
            }

            currentItem++;
        }
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

    private void makeEnteranceNode() {
        Node n = new Node(centerX, boundaryY2 + spacing - 10, enteranceIcon.width / scale, enteranceIcon.height / scale);
        n.setType("enterance");
        n.setFloor(0);
        if (!nodeExists(n)) {
            nodes.add(n);
        }
    }

    private void makeElevatorNode(int x, int y, int floor) {
        Node n = new Node(x, y, elevatorIcon.width / scale, elevatorIcon.height / scale);
        n.setType("elevator");
        n.setFloor(floor);
        if (!nodeExists(n)) {
            nodes.add(n);
        }
    }

    private void makeClassRoomNode(int x, int y, int floor, ClassRoom cr) {
        Node n;
        if (cr.isAvailable()) {
            n = new Node(x, y, classRoomIcon.width / scale, classRoomIcon.height / scale);
        } else {
            n = new Node(x, y, classRoomUnavailableIcon.width / scale, classRoomUnavailableIcon.height / scale);
        }

        n.setCr(cr);
        n.setType("classroom");
        n.setFloor(floor);
        if (!nodeExists(n)) {
            nodes.add(n);
        }
    }

    private boolean nodeExists(Node n) {
        for (Node o : nodes) {
            if (o.getFloor() == n.getFloor() && o.getType().equals(n.getType()) && o.getX() == n.getX() && o.getY() == n.getY()) {
                return true;
            }
        }
        return false;
    }

    private void drawNode(Node n) {
        switch (n.getType()) {
            case "enterance":
                app.image(enteranceIcon, n.getX(), n.getY(), n.getWidth(), n.getHeight());
                break;
            case "elevator":
                app.image(elevatorIcon, n.getX(), n.getY(), n.getWidth(), n.getHeight());
                String text = "etage " + n.getFloor();

                app.noStroke();
                app.fill(AppProperties.displayColor);
                app.rect(n.getX() - 6, n.getY() - 22, app.textWidth(text), 19);
                app.stroke(AppProperties.strokeColor);
                app.fill(255);

                app.text(text, n.getX() + (app.textWidth(text) / 2) - 6, n.getY() - 10);
                break;
            case "classroom":
                if (n.getCr().isAvailable()) {
                    app.image(classRoomIcon, n.getX(), n.getY(), n.getWidth(), n.getHeight());
                } else {
                    app.image(classRoomUnavailableIcon, n.getX(), n.getY(), n.getWidth(), n.getHeight());
                }
                app.text(n.getCr().floorLocation(), n.getX() + (n.getWidth() / 2), n.getY() + (n.getHeight() / 2) + 3);
                break;
        }
    }

    private void drawConector(Node one, Node two) {
        app.stroke(Color.decode("#a6e63c").getRGB());
        app.strokeWeight(5);

        app.line(one.getX() + (one.getWidth() / 2), one.getY() + (one.getHeight() / 2), two.getX() + (two.getWidth() / 2), two.getY() + (two.getHeight() / 2));

        app.strokeWeight(1);
        app.stroke(AppProperties.strokeColor);
    }
}
