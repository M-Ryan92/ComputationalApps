package timetableapp.models;

import processing.data.TableRow;

public class DataRow {

    private String activity;
    private String course;
    private String startweek;
    private String startday;
    private String startdate;
    private String starttime;
    private String endday;
    private String enddate;
    private String endtime;
    private String duration;
    private String activitytype;
    private String staffmember;
    private String location;
    private String groups;
    private String department;
    private String size;
    private String notes;

    public DataRow(TableRow row) {
        activity = row.getString("Activity");
        course = row.getString("Course");
        startweek = row.getString("Start week (ISO)");
        startday = row.getString("Start day");
        startdate = row.getString("Start date");
        starttime = row.getString("Start time");
        endday = row.getString("End day");
        enddate = row.getString("End date");
        endtime = row.getString("End time");
        duration = row.getString("Duration");
        activitytype = row.getString("Activity type");
        staffmember = row.getString("Staff member");
        location = row.getString("Location");
        groups = row.getString("Groups");
        department = row.getString("Department");
        size = row.getString("Size");
        notes = row.getString("Notes");
    }

    public String getString(String s) {
        switch (s) {
            case ("Activity"):
                return activity;
            case "Course":
                return course;
            case "Start week (ISO)":
                return startweek;
            case "Start day":
                return startday;
            case "Start date":
                return startdate;
            case "Start time":
                return starttime;
            case "End day":
                return endday;
            case "End date":
                return enddate;
            case "End time":
                return endtime;
            case "Duration":
                return duration;
            case "Activity type":
                return activitytype;
            case "Staff member":
                return staffmember;
            case "Location":
                return location;
            case "Groups":
                return groups;
            case "Department":
                return department;
            case "Size":
                return size;
            case "Notes":
                return notes;
        }
        return null;
    }
}
