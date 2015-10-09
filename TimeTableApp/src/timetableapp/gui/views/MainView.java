package timetableapp.gui.views;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.ControllerInterface;
import controlP5.Textfield;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import lombok.Getter;
import timetableapp.eventhandlers.NewFileSelectedHandler;
import timetableapp.gui.BaseView;
import timetableapp.gui.drawHelper.Draw;
import timetableapp.gui.drawHelper.DrawBuildingVis;
import timetableapp.models.DataManager;
import timetableapp.util.AppProperties;
import timetableapp.util.observer.StateObserver;
import timetableapp.util.state.ViewStates;

public final class MainView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    @Getter
    private DrawBuildingVis dbv;

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

        picker("day", app.width - (app.width / 3), state.getDisplayPanelHeight(), 30);
        picker("month", app.width - (app.width / 3) + 35, state.getDisplayPanelHeight(), 40);
        picker("year", app.width - (app.width / 3) + 80, state.getDisplayPanelHeight(), 40);

        state.getNewFileSelectedStateObserver().addObserver(new StateObserver(new NewFileSelectedHandler()));

        state.getLoadingFileStateObserver().addObserver(new StateObserver(() -> {
            if (state.getLoadingFileState() == 1) {
                state.setSelectedViewState(ViewStates.LoadView);
                this.hide();
            }
            return null;
        }));

        state.getFileLoadedStateObserver().addObserver(new StateObserver(() -> {
            state.setSelectedViewState(ViewStates.MainView);
            this.show();
            return null;
        }));

        state.getFileLoadedStateObserver().addObserver(new StateObserver(() -> {
            if (state.getFileLoadedState() == 1) {
                getcontrollerByName("viewData").show();
                getcontrollerByName("floorUp").show();
                getcontrollerByName("floorDown").show();
            }
            return null;
        }));

    }

    private void picker(String name, int x, int y, int width) {
        getControllers().add(cp5.addButton(name + "Plus")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(x, y + (AppProperties.buttonHeight * 1))
                .setLabel(name + " +")
                .setSize(width, AppProperties.buttonHeight)
        );
        getControllers().add(cp5.addTextfield(name + "Val")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(x, state.getDisplayPanelHeight() + (AppProperties.buttonHeight * 2) + 4)
                //.setInputFilter(ControlP5.INTEGER)
                .setSize(width, AppProperties.buttonHeight)
                .setLabel("")
                .lock()
        );
        ((Textfield) getcontrollerByName(name + "Val")).getValueLabel().alignX(ControlP5.CENTER);
        getControllers().add(cp5.addButton(name + "Minus")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(x, y + (AppProperties.buttonHeight * 3) + 8)
                .setLabel(name + " -")
                .setSize(width, AppProperties.buttonHeight)
        );
    }

    public void controlEvent(ControlEvent evt) {
        Controller<?> controller = evt.getController();
        ControllerInterface ctrl;
        int newVal = 0;
        boolean isDigit = true;
        switch (controller.getName()) {
            case ("dayPlus"):
                ctrl = getcontrollerByName("dayVal");
                ((Textfield) ctrl).setText("0");
                break;
            case ("dayMinus"):
                ctrl = getcontrollerByName("dayVal");
                ((Textfield) ctrl).setText("32");
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
            } else {
                //do some epic drawing magic =D
                dbv.draw(dm.getBl().get("WBH"));
                dbv.checkBtnState(getcontrollerByName("floorDown"), getcontrollerByName("floorUp"));
                app.text(dbv.getEtageRange(), (app.width / 2), state.getDisplayPanelHeight() + (AppProperties.buttonHeight * 3) - 8);
            }
        }
    }

}
