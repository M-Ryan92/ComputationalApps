package timetableapp.gui.views;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.ControllerInterface;
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
    private Calendar c = state.getStartTime();

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
        datePicker("day", pickerx, pickery, 30, c.get(Calendar.DAY_OF_MONTH));
        datePicker("month", pickerx + 35, pickery, 40, c.get(Calendar.MONTH) + 1);
        datePicker("year", pickerx + 80, pickery, 40, c.get(Calendar.YEAR));

        //time start selector
        getControllers().add(cp5
                .addLabel("Select Start Time")
                .setFont(state.getFont())
                .setPosition(pickerx + 145, pickery)
                .hide()
        );
        timePicker("startHour", pickerx + 160, pickery, 40, c.get(Calendar.HOUR_OF_DAY));
        timePicker("startMin", pickerx + 205, pickery, 40, c.get(Calendar.MINUTE));

        state.getNewFileSelectedStateObserver().addObserver(new StateObserver(new NewFileSelectedHandler()));

        state.getLoadingFileStateObserver().addObserver(new StateObserver(() -> {
            if (state.getLoadingFileState() == 1) {
                state.setSelectedViewState(ViewStates.LoadView);
                hide();
            }
        }));

        state.getFileLoadedStateObserver().addObserver(new StateObserver(() -> {
            state.setSelectedViewState(ViewStates.MainView);
            show();
        }));

        state.getFileLoadedStateObserver().addObserver(new StateObserver(() -> {
            if (state.getFileLoadedState() == 1) {
                getcontrollerByName("viewData").show();
                getcontrollerByName("floorUp").show();
                getcontrollerByName("floorDown").show();

                getcontrollerByName("dayPlus").show();
                getcontrollerByName("dayVal").show();
                getcontrollerByName("dayMinus").show();
                getcontrollerByName("monthPlus").show();
                getcontrollerByName("monthVal").show();
                getcontrollerByName("monthMinus").show();
                getcontrollerByName("yearPlus").show();
                getcontrollerByName("yearVal").show();
                getcontrollerByName("yearMinus").show();

                getcontrollerByName("startHourPlus").show();
                getcontrollerByName("startHourVal").show();
                getcontrollerByName("startHourMinus").show();
                getcontrollerByName("startMinPlus").show();
                getcontrollerByName("startMinVal").show();
                getcontrollerByName("startMinMinus").show();

            }
        }));
        state.getStartTimeObserver().addObserver(new StateObserver(() -> {
            c = state.getStartTime();
            setTimeFields();
            setDateFields();
            List<Activity> activitiesNow = dm.getActivitiesByCalendarDateAndBuilding(c, building);

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
        }));

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

    private void setTimeFields() {
        if (getcontrollerByName("startHourVal") != null && getcontrollerByName("startMinVal") != null) {
            ((Textfield) getcontrollerByName("startHourVal")).setText(getDisplayString(c.get(Calendar.HOUR_OF_DAY)));
            ((Textfield) getcontrollerByName("startMinVal")).setText(getDisplayString(c.get(Calendar.MINUTE)));
        }
    }

    private void setDateFields() {
        if (getcontrollerByName("dayVal") != null && getcontrollerByName("monthVal") != null && getcontrollerByName("yearVal") != null) {
            ((Textfield) getcontrollerByName("dayVal")).setText(getDisplayString(c.get(Calendar.DAY_OF_MONTH)));
            ((Textfield) getcontrollerByName("monthVal")).setText(getDisplayString(c.get(Calendar.MONTH) + 1));
            ((Textfield) getcontrollerByName("yearVal")).setText(getDisplayString(c.get(Calendar.YEAR)));
        }
    }

    public void controlEvent(ControlEvent evt) {
        Controller<?> controller = evt.getController();
        ControllerInterface ctrl;
        int newVal = 0;
        boolean isDigit = true;
        switch (controller.getName()) {
            case ("startHourPlus"):
                c.add(Calendar.HOUR_OF_DAY, 1);
                state.setStartTime(c);
                break;
            case ("startHourMinus"):
                c.add(Calendar.HOUR_OF_DAY, -1);
                state.setStartTime(c);
                break;
            case ("startMinPlus"):
                c.add(Calendar.MINUTE, 1);
                state.setStartTime(c);
                break;
            case ("startMinMinus"):
                c.add(Calendar.MINUTE, -1);
                state.setStartTime(c);
                break;
            case ("dayPlus"):
                c.add(Calendar.DATE, 1);
                state.setStartTime(c);
                break;
            case ("dayMinus"):
                c.add(Calendar.DATE, -1);
                state.setStartTime(c);
                break;
            case ("monthPlus"):
                c.add(Calendar.MONTH, 1);
                state.setStartTime(c);
                break;
            case ("monthMinus"):
                c.add(Calendar.MONTH, -1);
                state.setStartTime(c);
                break;
            case ("yearPlus"):
                c.add(Calendar.YEAR, 1);
                state.setStartTime(c);
                break;
            case ("yearMinus"):
                c.add(Calendar.YEAR, -1);
                state.setStartTime(c);
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

    private String building = "WBH";

    @Override
    public void draw() {
        if (ishidden == false) {
            Draw.drawDisplay();

            if (state.getFileLoadedState() != 1) {
                Draw.drawDisplayMessage("no file selected");
            } else {
                //do some epic drawing magic =D
                dbv.draw(dm.getBl().get(building));
                dbv.checkBtnState(getcontrollerByName("floorDown"), getcontrollerByName("floorUp"));

                app.fill(AppProperties.displayColor);
                app.rect(pickerx - 10, pickery - 5, 140, (AppProperties.buttonHeight * 3) + 42);

                app.rect(pickerx + 150 - 10, pickery - 5, 125, (AppProperties.buttonHeight * 3) + 42);
                app.fill(255);

                app.text(dbv.getEtageRange(), (app.width / 2), state.getDisplayPanelHeight() + (AppProperties.buttonHeight * 3) - 8);
            }
        }
    }

}
