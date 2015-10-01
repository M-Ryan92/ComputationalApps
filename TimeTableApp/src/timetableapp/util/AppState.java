package timetableapp.util;

import controlP5.ControlP5;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import timetableapp.util.observer.ObservableValue;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PFont;

public class AppState {

    private static AppState instance = new AppState();

    @Getter
    @Setter
    private File selectedFile;

    @Getter
    @Setter
    private PApplet app;

    @Getter
    @Setter
    private ControlP5 cp5;

    public static final int backgroundColor = Color.decode("#1C4B5F").getRGB();
    public static final int displayColor = Color.decode("#748993").getRGB();
    public static final int buttonColor = Color.decode("#010D13").getRGB();

    public static final int specialColor = Color.decode("#3F5B68").getRGB();
    public static final int specialActiveColor = Color.decode("#0C2631").getRGB();

    public static final int strokeColor = Color.decode("#010D13").getRGB();
    public static final int textColor = 255;

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
