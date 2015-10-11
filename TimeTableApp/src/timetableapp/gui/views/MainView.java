package timetableapp.gui.views;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.DropdownList;
import controlP5.Textfield;
import java.util.Calendar;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import lombok.Getter;
import timetableapp.eventhandlers.NewFileSelectedHandler;
import timetableapp.gui.BaseView;
import timetableapp.gui.drawHelper.Draw;
import timetableapp.gui.drawHelper.DrawBuildingVis;
import timetableapp.models.Activity;
import timetableapp.models.DataManager;
import timetableapp.util.AppProperties;
import timetableapp.util.observer.StateObserver;
import timetableapp.util.state.ViewStates;

public final class MainView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    @Getter
    private DrawBuildingVis dbv;

    private int pickerx = app.width - (app.width / 3) - (app.width / 9);
    private int pickery = state.getDisplayPanelHeight() + 30;
    private Calendar startTime = state.getStartTime();
    private Calendar endTime = state.getEndTime();
    private String[] items = state.getItems();

    @Getter
    private String building = null;

    public MainView() {
        super();
        dbv = new DrawBuildingVis(app);
        getControllers().add(cp5
                .addButton(cp5, "selectFileBtn")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(20, app.height - AppProperties.buttonHeight - 20)
                .setSize(70, AppProperties.buttonHeight)
                .setLabel("Select File"));

        getControllers().add(cp5
                .addButton(cp5, "viewData")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(20, app.height - (AppProperties.buttonHeight * 2) - 30)
                .setSize(70, AppProperties.buttonHeight)
                .setLabel("View Data")
                .hide());

        getControllers().add(cp5
                .addDropdownList("selectBuilding")
                .setBarHeight(AppProperties.buttonHeight)
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(app.width - ((app.width / 3) * 2), state.getDisplayPanelHeight() + AppProperties.buttonHeight)
                .setItemHeight(AppProperties.buttonHeight)
                .setHeight(AppProperties.buttonHeight * 4)
                .addItems(items)
                .hide()
        );

        getControllers().add(cp5
                .addButton(cp5, "floorUp")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition((app.width / 2) - 10, state.getDisplayPanelHeight() + AppProperties.buttonHeight)
                .setSize(20, AppProperties.buttonHeight)
                .setLabel(Character.toString('\uf062'))
                .hide());
        ((Button) getcontrollerByName("floorUp")).getCaptionLabel().setFont(state.getIconFont());

        getControllers().add(cp5
                .addButton(cp5, "floorDown")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition((app.width / 2) - 10, state.getDisplayPanelHeight() + (AppProperties.buttonHeight * 3))
                .setSize(20, AppProperties.buttonHeight)
                .setLabel(Character.toString('\uf063'))
                .hide());
        ((Button) getcontrollerByName("floorDown")).getCaptionLabel().setFont(state.getIconFont());

        //dateselector
        getControllers().add(cp5
                .addLabel("Select Date")
                .setFont(state.getFont())
                .setPosition(pickerx + 20, pickery)
                .hide()
        );
        datePicker("day", pickerx, pickery, 30, startTime.get(Calendar.DAY_OF_MONTH));
        datePicker("month", pickerx + 35, pickery, 40, startTime.get(Calendar.MONTH) + 1);
        datePicker("year", pickerx + 80, pickery, 40, startTime.get(Calendar.YEAR));

        //time start selector
        getControllers().add(cp5
                .addLabel("Select Start Time")
                .setFont(state.getFont())
                .setPosition(pickerx + 145, pickery)
                .hide()
        );
        timePicker("startHour", pickerx + 160, pickery, 40, startTime.get(Calendar.HOUR_OF_DAY));
        timePicker("startMin", pickerx + 205, pickery, 40, startTime.get(Calendar.MINUTE));

        //time start selector
        getControllers().add(cp5
                .addLabel("Select End Time")
                .setFont(state.getFont())
                .setPosition(pickerx + 145 + 135, pickery)
                .hide()
        );
        timePicker("endHour", pickerx + 160 + 135, pickery, 40, startTime.get(Calendar.HOUR_OF_DAY));
        timePicker("endMin", pickerx + 205 + 135, pickery, 40, startTime.get(Calendar.MINUTE));

        state.getNewFileSelectedStateObserver().addObserver(new StateObserver(new NewFileSelectedHandler()));

        state.getLoadingFileStateObserver().addObserver(new StateObserver(() -> {
            if (state.getLoadingFileState() == 1) {
                state.setSelectedViewState(ViewStates.LoadView);
                hide();
            }
        }));

        state.getFileLoadedStateObserver().addObserver(new StateObserver(() -> {
            state.setSelectedViewState(ViewStates.MainView);
                showNoControlls();
                getcontrollerByName("viewData").show();
                getcontrollerByName("selectFileBtn").show();  
                getcontrollerByName("selectBuilding").show();  
        }));

        state.getStartTimeObserver().addObserver(new StateObserver(() -> {
            startTime = state.getStartTime();
            endTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH));
            setStartTimeFields();
            setDateFields();
            SetClassRoomStates(startTime, endTime);
        }));
        state.getEndTimeObserver().addObserver(new StateObserver(() -> {
            endTime = state.getEndTime();
            endTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH));
            setEndTimeFields();
            SetClassRoomStates(startTime, endTime);
        }));
    }

    private void SetClassRoomStates(Calendar start, Calendar end) {
        List<Activity> activitiesNow = dm.getActivitiesByCalendarDateAndBuilding(start, end, building);

        dm.getBl().get(building).getFloorList().forEach((floor, classrooms) -> {
            classrooms.forEach((location, classroom) -> {
                classroom.setAvailable(true);
            });
        });

        for (Activity activity : activitiesNow) {
            dm.getBl().get(building)
                    .getFloorList()
                    .get(activity.getFloor())
                    .get(activity.getClassroom())
                    .setAvailable(false);
        }
    }

    private String getDisplayString(int input) {
        if (input < 10) {
            return 0 + String.valueOf(input);
        } else {
            return String.valueOf(input);
        }
    }

    private void timePicker(String name, int x, int y, int width, int input) {
        getControllers().add(cp5.addButton(name + "Plus")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(x, y + (AppProperties.buttonHeight * 1))
                .setLabel("+")
                .setSize(width, AppProperties.buttonHeight)
                .hide()
        );

        getControllers().add(cp5.addTextfield(name + "Val")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(x, y + (AppProperties.buttonHeight * 2) + 4)
                .setSize(width, AppProperties.buttonHeight)
                .setText(getDisplayString(input))
                .setLabel("")
                .lock()
                .hide()
        );
        ((Textfield) getcontrollerByName(name + "Val")).getValueLabel().alignX(ControlP5.CENTER);
        getControllers().add(cp5.addButton(name + "Minus")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(x, y + (AppProperties.buttonHeight * 3) + 8)
                .setLabel("-")
                .setSize(width, AppProperties.buttonHeight)
                .hide()
        );
    }

    private void datePicker(String name, int x, int y, int width, int input) {
        getControllers().add(cp5.addButton(name + "Plus")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(x, y + (AppProperties.buttonHeight * 1))
                .setLabel(name + " +")
                .setSize(width, AppProperties.buttonHeight)
                .hide()
        );
        getControllers().add(cp5.addTextfield(name + "Val")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(x, y + (AppProperties.buttonHeight * 2) + 4)
                .setSize(width, AppProperties.buttonHeight)
                .setText(getDisplayString(input))
                .setLabel("")
                .lock()
                .hide()
        );
        ((Textfield) getcontrollerByName(name + "Val")).getValueLabel().alignX(ControlP5.CENTER);
        getControllers().add(cp5.addButton(name + "Minus")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(x, y + (AppProperties.buttonHeight * 3) + 8)
                .setLabel(name + " -")
                .setSize(width, AppProperties.buttonHeight)
                .hide()
        );
    }

    private void setEndTimeFields() {
        if (getcontrollerByName("endHourVal") != null && getcontrollerByName("endMinVal") != null) {
            ((Textfield) getcontrollerByName("endHourVal")).setText(getDisplayString(endTime.get(Calendar.HOUR_OF_DAY)));
            ((Textfield) getcontrollerByName("endMinVal")).setText(getDisplayString(endTime.get(Calendar.MINUTE)));
        }
    }

    private void setStartTimeFields() {
        if (getcontrollerByName("startHourVal") != null && getcontrollerByName("startMinVal") != null) {
            ((Textfield) getcontrollerByName("startHourVal")).setText(getDisplayString(startTime.get(Calendar.HOUR_OF_DAY)));
            ((Textfield) getcontrollerByName("startMinVal")).setText(getDisplayString(startTime.get(Calendar.MINUTE)));
        }
    }

    private void setDateFields() {
        if (getcontrollerByName("dayVal") != null && getcontrollerByName("monthVal") != null && getcontrollerByName("yearVal") != null) {
            ((Textfield) getcontrollerByName("dayVal")).setText(getDisplayString(startTime.get(Calendar.DAY_OF_MONTH)));
            ((Textfield) getcontrollerByName("monthVal")).setText(getDisplayString(startTime.get(Calendar.MONTH) + 1));
            ((Textfield) getcontrollerByName("yearVal")).setText(getDisplayString(startTime.get(Calendar.YEAR)));
        }
    }

    public void controlEvent(ControlEvent evt) {
        Controller<?> controller = evt.getController();
        int newVal = 0;
        boolean isDigit = true;
        switch (controller.getName()) {
            case ("selectBuilding"):
                building = (String) ((DropdownList) controller).getItem((int) controller.getValue()).get("text");
                dbv.reset();
                show();
                break;
            case ("startHourPlus"):
                startTime.add(Calendar.HOUR_OF_DAY, 1);
                state.setStartTime(startTime);
            case ("endHourPlus"):
                endTime.add(Calendar.HOUR_OF_DAY, 1);
                state.setEndTime(endTime);
                break;

            case ("startHourMinus"):
                startTime.add(Calendar.HOUR_OF_DAY, -1);
                state.setStartTime(startTime);
            case ("endHourMinus"):
                endTime.add(Calendar.HOUR_OF_DAY, -1);
                if (endTime.before(startTime)) {
                    startTime.add(Calendar.HOUR_OF_DAY, -1);
                    state.setStartTime(startTime);
                }
                state.setEndTime(endTime);
                break;

            case ("startMinPlus"):
                startTime.add(Calendar.MINUTE, 1);
                state.setStartTime(startTime);
            case ("endMinPlus"):
                endTime.add(Calendar.MINUTE, 1);
                state.setEndTime(endTime);
                break;

            case ("startMinMinus"):
                startTime.add(Calendar.MINUTE, -1);
                state.setStartTime(startTime);
            case ("endMinMinus"):
                endTime.add(Calendar.MINUTE, -1);
                if (endTime.before(startTime)) {
                    startTime.add(Calendar.MINUTE, -1);
                    state.setStartTime(startTime);
                }
                state.setEndTime(endTime);
                break;

            case ("dayPlus"):
                startTime.add(Calendar.DATE, 1);
                state.setStartTime(startTime);
                break;
            case ("dayMinus"):
                startTime.add(Calendar.DATE, -1);
                state.setStartTime(startTime);
                break;
            case ("monthPlus"):
                startTime.add(Calendar.MONTH, 1);
                state.setStartTime(startTime);
                break;
            case ("monthMinus"):
                startTime.add(Calendar.MONTH, -1);
                state.setStartTime(startTime);
                break;
            case ("yearPlus"):
                startTime.add(Calendar.YEAR, 1);
                state.setStartTime(startTime);
                break;
            case ("yearMinus"):
                startTime.add(Calendar.YEAR, -1);
                state.setStartTime(startTime);
                break;
            case ("selectFileBtn"):
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("data files(txt, ics, csv, tsv, tab)",
                        new String[]{"txt", "ics", "csv", "tsv", "tab"}));
                fc.setAcceptAllFileFilterUsed(false);

                int fcResult = fc.showOpenDialog(null);
                if (fcResult == JFileChooser.APPROVE_OPTION) {
                    fcResult = -1;
                    state.setSelectedFile(fc.getSelectedFile());
                    state.setNewFileSelectedState(1);
                }
                break;
            case ("floorUp"):
                dbv.floorsUp();
                dbv.checkBtnState(getcontrollerByName("floorDown"), getcontrollerByName("floorUp"));
                break;
            case ("floorDown"):
                dbv.floorsDown();
                dbv.checkBtnState(getcontrollerByName("floorDown"), getcontrollerByName("floorUp"));
                break;
        }
    }

    @Override
    public void draw() {
        if (ishidden == false) {
            Draw.drawDisplay();

            if (state.getFileLoadedState() != 1) {
                Draw.drawDisplayMessage("no file selected");
            } else if (building != null) {
                //do some epic drawing magic =D
                dbv.draw(dm.getBl().get(building));
                dbv.checkBtnState(getcontrollerByName("floorDown"), getcontrollerByName("floorUp"));
                drawNavigationBackground();
            } else {
                Draw.drawDisplayMessage("selecte a building");
            }
        }
    }

    private void drawNavigationBackground() {
        app.fill(AppProperties.displayColor);
        app.rect(pickerx - 10, pickery - 5, 140, (AppProperties.buttonHeight * 3) + 42);

        app.rect(pickerx + 150 - 10, pickery - 5, 125, (AppProperties.buttonHeight * 3) + 42);//start time picker

        app.rect(pickerx + 150 + 135 - 10, pickery - 5, 125, (AppProperties.buttonHeight * 3) + 42);//end time picker
        app.fill(255);

        app.text(dbv.getEtageRange(), (app.width / 2), state.getDisplayPanelHeight() + (AppProperties.buttonHeight * 3) - 8);
    }

}
