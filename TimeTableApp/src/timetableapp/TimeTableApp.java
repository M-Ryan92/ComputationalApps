package timetableapp;

import processing.core.PApplet;

public class TimeTableApp {

    public static void main(String[] args) {
        PApplet app = new BaseApplication();
                
        app.main(new String[]{
            //"--present", // makes the app appear in fullscreen mode
            app.getClass().getName()
        });
    }

}
