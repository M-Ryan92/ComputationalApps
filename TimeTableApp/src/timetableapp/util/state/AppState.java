package timetableapp.util.state;

import timetableapp.util.state.ViewStates;
import controlP5.ControlP5;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import timetableapp.util.observer.ObservableValue;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PFont;
import timetableapp.util.Properties;

public class AppState {

    private static AppState instance = new AppState();

    @Getter
    @Setter
    private File selectedFile;

    @Getter
    private PApplet app;

    @Getter
    @Setter
    private ControlP5 cp5;

    @Getter
    private int displayPanelWidth;
    @Getter
    private int displayPanelHeight;

    @Getter
    PFont font;

    @Getter
    private ObservableValue<Integer> fileSelectedStateObserver = new ObservableValue(0);
    @Getter
    private ObservableValue<Integer> newFileSelectedStateObserver = new ObservableValue(0);
    @Getter
    private ObservableValue<Integer> loadingFileStateObserver = new ObservableValue(0);
    @Getter
    private ObservableValue<Integer> fileLoadedStateObserver = new ObservableValue(0);
    @Getter
    private ObservableValue<Integer> selectedViewStateObserver = new ObservableValue(ViewStates.MainView);

    public void setApp(PApplet app) {
        this.app = app;
        displayPanelWidth = app.width - (Properties.displayPanelXOffset * 2);
        displayPanelHeight = app.height - (Properties.displayPanelYOffset * 2) - 120;
        displayPanelHeight = displayPanelHeight - (displayPanelHeight % 24);
    }

    public static AppState getInstance() {
        return instance;
    }

    private AppState() {

    }

    public void setFont(int size) {
        List<String> fonts = Arrays.asList(PFont.list());
        List<String> style = Arrays.asList(new String[]{"Arial", "Calibri"});

        String name = style.stream().filter(s -> fonts.contains(s) == true).findFirst().get();
        if (name == null) {
            name = fonts.get(0);
        }

        font = app.createFont(name, size);
        app.textFont(font);
    }

    public void setFont() {
        setFont(11);
    }

    public void setFileSelectedState(int value) {
        fileSelectedStateObserver.setValue(value);
    }

    public void setNewFileSelectedState(int value) {
        newFileSelectedStateObserver.setValue(value);
    }

    public void setLoadingFileState(int value) {
        loadingFileStateObserver.setValue(value);
    }

    public void setFileLoadedState(int value) {
        fileLoadedStateObserver.setValue(value);
    }

    public void setSelectedViewState(int value) {
        selectedViewStateObserver.setValue(value);
    }

    public int getFileSelectedState() {
        return fileSelectedStateObserver.getValue();
    }

    public int getNewFileSelectedState() {
        return newFileSelectedStateObserver.getValue();
    }

    public int getLoadingFileState() {
        return loadingFileStateObserver.getValue();
    }

    public int getFileLoadedState() {
        return fileLoadedStateObserver.getValue();
    }

    public int getSelectedViewState() {
        return selectedViewStateObserver.getValue();
    }
}
