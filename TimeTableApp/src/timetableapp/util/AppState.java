package timetableapp.util;

import controlP5.ControlP5;
import java.awt.Color;
import java.io.File;
import java.net.URL;
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
    
    @Getter
    @Setter
    private int backgroundcolor = Color.decode("#98A59A").getRGB();

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
    private ObservableValue<Integer> selectedViewStateObserver = new ObservableValue(0);

    public static AppState getInstance() {
        return instance;
    }

    private AppState() {

    }

    public void setFont() {
        URL resource = this.getClass().getResource("ttf/OpenSans-Regular.ttf");
        font = app.createFont(resource.getFile(), 11);
        app.textFont(font);
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
