package timetableapp.models;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import lombok.Getter;

public class Building {

    @Getter
    private String code;
    @Getter
    private String naam;
    @Getter
    private int etageCount;
    @Getter
    private Map<Integer, Map<String, ClassRoom>> floorList;

    public Building(String code) {
        this.code = code;
        floorList = new HashMap<>();
    }

    public void addClassRoom(ClassRoom classRoom) {
        if (floorList.containsKey(classRoom.getFloor()) && !floorList.get(classRoom.getFloor()).containsKey(classRoom.floorLocation())) {
            floorList.get(classRoom.getFloor()).put(classRoom.floorLocation(), classRoom);

        } else if (!floorList.containsKey(classRoom.getFloor())) {
            Map<String, ClassRoom> crl = new TreeMap<>();
            crl.put(classRoom.floorLocation(), classRoom);
            floorList.put(classRoom.getFloor(), crl);

            if (etageCount < classRoom.getFloor()) {
                etageCount = classRoom.getFloor();
            }

        }

    }
}
