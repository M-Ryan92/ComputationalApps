package timetableapp.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import processing.data.Table;
import processing.data.TableRow;
import timetableapp.models.table.TableModel;
import timetableapp.util.state.AppState;

public class DataManager {

    @Getter
    private TableModel tm;

    @Getter
    private Map<String, Building> bl;

    @Getter
    private List<Activity> activities;

    private Map<String, String> names;

    private static DataManager instance = new DataManager();

    public static DataManager getInstance() {
        return instance;
    }

    private DataManager() {
        names = AppState.getInstance().getBuildingNames();
    }

    public void createTable(Table data) {
        if (tm == null) {
            tm = new TableModel(data);
        }
    }

    public void createActivities(Table data) throws ParseException {
        if (activities == null) {
            activities = new ArrayList<>();
            SimpleDateFormat dp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (TableRow tr : data.rows()) {
                try {
                    Date startDate = dp.parse(tr.getString("Start date") + " " + tr.getString("Start time"));
                    Calendar start = Calendar.getInstance();
                    start.setTime(startDate);

                    Date endDate = dp.parse(tr.getString("End date") + " " + tr.getString("End time"));
                    Calendar end = Calendar.getInstance();
                    end.setTime(endDate);

                    String locations = tr.getString("Location");
                    if (locations.contains(",")) {
                        for (String location : locations.split(",")) {
                            addActivity(tr.getString("Activity"), tr.getString("Course"), start, end, location.split(" "));
                        }
                    } else {
                        addActivity(tr.getString("Activity"), tr.getString("Course"), start, end, locations.split(" "));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("");
                }
            }
        }
    }

    private void addActivity(String activity, String course, Calendar start, Calendar end, String[] classRoomInfo) {
        String clsroom = "";
        if (Arrays.asList(formatOne).contains(classRoomInfo[0])) {
            LocationProperties lp = new LocationProperties(classRoomInfo);

            clsroom = lp.letter + String.format("%02d", lp.number);

            activities.add(new Activity(activity, course, start, end, lp.building, lp.floor, clsroom));
        } else if (Arrays.asList(formatTwo).contains(classRoomInfo[0])) {
            LocationProperties lp = new LocationProperties(classRoomInfo);
            clsroom = String.format("%02d", lp.number);
            activities.add(new Activity(activity, course, start, end, lp.building, lp.floor, clsroom));
        }
    }

    public void createMap(Table data) {
        if (bl == null) {
            bl = new HashMap<>();

            for (TableRow r : data.rows()) {
                String locations = r.getString("Location");

                if (locations.contains(",")) {
                    for (String location : locations.split(",")) {
                        addLocation(location);
                    }
                } else {
                    addLocation(locations);
                }
            }
        }
    }

    private void addLocation(String location) {
        String[] classRoomInfo = location.trim().split(" ");
        String code = classRoomInfo[0];
        ClassRoom c;
        if (bl.containsKey(code)) {
            c = makeClassRoom(classRoomInfo);
            if (c != null) {
                bl.get(code).addClassRoom(c);
            }
        } else {
            Building b = new Building(code, names.get(code));
            c = makeClassRoom(classRoomInfo);
            if (c != null) {
                b.addClassRoom(c);
            }
            bl.put(code, b);
        }
    }
    private String[] formatOne = AppState.getInstance().getFormatOne();
    private String[] formatTwo = AppState.getInstance().getFormatTwo();

    private class LocationProperties {

        int floor, number, capacity;
        char letter;
        String building;

        public LocationProperties(String[] classRoomInfo) {
            building = classRoomInfo[0];
            if (Arrays.asList(formatOne).contains(classRoomInfo[0])) {
                floor = Integer.valueOf(classRoomInfo[1].subSequence(0, 2).toString());
                letter = classRoomInfo[1].charAt(2);
                number = Integer.valueOf(classRoomInfo[1].subSequence(3, 5).toString());
                capacity = Integer.valueOf(classRoomInfo[classRoomInfo.length - 1].substring(1, classRoomInfo[classRoomInfo.length - 1].length() - 1));

            } else if (Arrays.asList(formatTwo).contains(classRoomInfo[0])) {
                floor = Integer.valueOf(classRoomInfo[1].subSequence(0, 1).toString());
                number = Integer.valueOf(classRoomInfo[1].subSequence(2, 4).toString());
                capacity = Integer.valueOf(classRoomInfo[classRoomInfo.length - 1].substring(1, classRoomInfo[classRoomInfo.length - 1].length() - 1));
            }
        }

    }

    private ClassRoom makeClassRoom(String[] classRoomInfo) {
        ClassRoom classRoom = null;
        if (Arrays.asList(formatOne).contains(classRoomInfo[0])) {
            LocationProperties lp = new LocationProperties(classRoomInfo);
            classRoom = new ClassRoom(lp.floor, lp.letter, lp.number, lp.capacity);
        } else if (Arrays.asList(formatTwo).contains(classRoomInfo[0])) {
            LocationProperties lp = new LocationProperties(classRoomInfo);
            classRoom = new ClassRoom(lp.floor, lp.number, lp.capacity);
        }
        return classRoom;
    }

    public List<Activity> getActivitiesByCalendarDateAndBuilding(Calendar start, Calendar end, String building) {
        List<Activity> collect = activities.stream()
                .filter(a -> a.building.equals(building))
                .filter(a -> ((a.getStartDate().compareTo(start) <= 0 && a.getEndDate().compareTo(start) >= 0)
                        || (a.getStartDate().compareTo(end) <= 0 && a.getEndDate().compareTo(end) >= 0))
                        || (a.getStartDate().compareTo(start) >= 0 && a.getEndDate().compareTo(end) <= 0)
                        
                )
                .collect(Collectors.toList());
        return collect;
    }
}
