package timetableapp.models;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import lombok.Getter;

public class Activity {

    //what
    @Getter
    private String activity;
    private String course;

    //when
    private int weekNr;
    private String day;
    @Getter
    private Calendar startDate;
    @Getter
    private Calendar endDate;
    private Date startTime;
    private Date endTime;
    private int[] duration = new int[3];

    //where
    @Getter
    String building;
    @Getter
    int floor;
    @Getter
    String classroom;

    private int MINSinMilis = 60 * 1000;
    private int HOURSinMilis = 60 * MINSinMilis;
    private int DAYinMilis = 24 * HOURSinMilis;

    public Activity(String activity, String course, Calendar start, Calendar end, String building, int floor, String classroom) {
        this.activity = activity;
        this.course = course;
        this.startDate = start;
        this.endDate = end;
        this.startTime = start.getTime();
        this.endTime = end.getTime();

        this.building = building;
        this.floor = floor;
        this.classroom = classroom;

        long diff = end.getTimeInMillis() - start.getTimeInMillis();
        duration[0] = (int) (diff / DAYinMilis);//days
        duration[1] = (int) ((diff % DAYinMilis) / HOURSinMilis);//hours
        duration[2] = (int) (((diff % DAYinMilis) % HOURSinMilis) / MINSinMilis);//minutes

        day = new DateFormatSymbols(Locale.ENGLISH).getWeekdays()[startDate.get(Calendar.DAY_OF_WEEK)];
        weekNr = startDate.get(Calendar.WEEK_OF_YEAR);
    }

}
