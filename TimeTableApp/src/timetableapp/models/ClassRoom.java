package timetableapp.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class ClassRoom {

    @Getter
    private int floor;
    @Getter
    private char letter;
    @Getter
    private int number;
    @Getter
    private int capacity;

    @Getter
    private List<Activity> activities;
    
    //less geeft aan dat het lokaal niet beschikbaar is
    //private Les les;
    //on false the class will be red
    //on true it will be green
    @Getter
    @Setter
    private boolean isAvailable;

    public ClassRoom(int floor, char letter, int number, int capacity) {
        this.floor = floor;
        this.letter = letter;
        this.number = number;
        this.capacity = capacity;
        isAvailable = true;
        activities = new ArrayList<>();
    }

    public ClassRoom(int floor, int number, int capacity) {
        this.floor = floor;
        this.number = number;
        this.capacity = capacity;
        isAvailable = true;
    }

    public String floorLocation() {
        if(Character.isLetter(letter)){
            return letter + String.format("%02d", number);
        } else {
            return String.format("%02d", number);
        }
        
    }

}
