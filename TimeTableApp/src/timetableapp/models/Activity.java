package timetableapp.models;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Activity {

    //what
    private String activity;
    private String course;

    //when
    private int weekNr;
    private String day;
    private Date startDate;
    private Date endDate;
    private Date startTime;
    private Date endTime;
    private int[] duration = new int[3];

    public Activity(String activity, String course, Date startDay, Date endDay) {
        this.activity = activity;
        this.course = course;
        this.startDate = startDay;
        this.endDate = endDay;
        this.startTime = startDay;
        this.endTime = endDay;

        long diff = endTime.getTime() - startTime.getTime();
        duration[0] = (int) (diff / (24 * 60 * 60 * 1000));//days
        duration[1] = (int) (diff / (60 * 60 * 1000));//hours
        duration[2] = (int) (diff / (60 * 1000));//minutes

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        day = new DateFormatSymbols(Locale.ENGLISH).getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
        weekNr = cal.get(Calendar.WEEK_OF_YEAR);
    }

}
