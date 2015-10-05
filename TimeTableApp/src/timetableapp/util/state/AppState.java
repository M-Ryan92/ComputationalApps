package timetableapp.util.state;

import controlP5.ControlP5;
import java.awt.Font;
import java.io.File;
import java.util.Arrays;
import timetableapp.util.observer.ObservableValue;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PFont;
import timetableapp.gui.Dialog;
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

    public PFont getIconFont() throws Exception {
        Font f = Font.createFont(Font.TRUETYPE_FONT, app.getClass().getResource("../../resources/fontawesome-webfont.ttf").openStream());
        f = f.deriveFont(Font.PLAIN, 16.0F);
        return new PFont(f, true);
    }

    public void setIconFont() {
        try {
            app.textFont(getIconFont(), 11);
        } catch (Exception e) {
            new Dialog().fatalErrorDialog("icons font could not be loaded, application closes now");
        }
    }

    private void setBaseFont(int size) {
        PFont font = app.createFont(Arrays.asList(PFont.list()).get(0), size);
        app.textFont(font, size);
    }

    public void setFont() {
        try {
            Font f = Font.createFont(Font.TRUETYPE_FONT, app.getClass().getResource("../../resources/OpenSans-Regular.ttf").openStream());
            PFont font = new PFont(f, true);
            app.textFont(font, 11);
        } catch (Exception e) {
            setBaseFont(11);
        }
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
