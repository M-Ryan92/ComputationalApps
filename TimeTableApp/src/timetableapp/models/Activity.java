package timetableapp.models;

import java.sql.Time;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Activity {

    //what
    String activity;
    String course;
    
    //when
    int weekNr;
    String day;
    Date startDate;
    Date endDate;
    Date startTime;
    Date endTime;
    Time duration;
    
    public Activity(String activity, String course, Date startDay, Date endDay) {
        this.activity = activity;
        this.course = course;
        this.startDate = startDay;
        this.endDate = endDay;
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        day = new DateFormatSymbols(Locale.ENGLISH).getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
        weekNr = cal.get(Calendar.WEEK_OF_YEAR);
    }
    
}
