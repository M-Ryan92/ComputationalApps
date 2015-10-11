package timetableapp.util.state;

import controlP5.ControlP5;
import java.awt.Font;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PFont;
import timetableapp.gui.Dialog;
import timetableapp.util.AppProperties;
import timetableapp.util.observer.ObservableValue;

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
    private ObservableValue<Calendar> startTimeObserver;
    @Getter
    private ObservableValue<Calendar> endTimeObserver;
    @Getter
    private ObservableValue<Integer> newFileSelectedStateObserver;
    @Getter
    private ObservableValue<Integer> loadingFileStateObserver;
    @Getter
    private ObservableValue<Integer> fileLoadedStateObserver;
    @Getter
    private ObservableValue<Integer> selectedViewStateObserver;

    public void setApp(PApplet app) {
        this.app = app;
        displayPanelWidth = app.width - (AppProperties.displayPanelXOffset * 2);
        displayPanelHeight = app.height - (AppProperties.displayPanelYOffset * 2) - 100;
        displayPanelHeight = displayPanelHeight - (displayPanelHeight % 24);
    }

    public static AppState getInstance() {
        return instance;
    }

    private PFont txtfont, iconFont;

    private Font iFont;

    private AppState() {
        this.formatTwo = new String[]{"KMH"};
        this.formatOne = new String[]{"KSH", "STU", "MLH", "TTH", "BPH", "WBH"};
        this.buildingNames = new HashMap() {
            {
                put("KSH", "KOHNSTAMMHUIS");
                put("STU", "STUDIO HVA");
                put("MLH", "MULLER-LULOFSHUIS");
                put("TTH", "THEO THIJSSENHUIS");
                put("BPH", "BENNO PREMSELAHUIS");
                put("WBH", "WIBAUTHUIS");
                put("KMH", "KOETSIER-MONTAIGNEHUIS");
            }
        };
        this.items = new String[]{
            "KSH", "STU", "MLH", "TTH", "BPH", "WBH", "KMH"
        };
        
        this.selectedViewStateObserver = new ObservableValue(ViewStates.MainView);
        this.fileLoadedStateObserver = new ObservableValue(0);
        this.loadingFileStateObserver = new ObservableValue(0);
        this.newFileSelectedStateObserver = new ObservableValue(0);
        this.startTimeObserver = new ObservableValue(Calendar.getInstance());
        this.endTimeObserver = new ObservableValue(Calendar.getInstance());

        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResource("ttf/OpenSans-Regular.ttf").openStream());
            f = f.deriveFont(Font.PLAIN, 14F);
            txtfont = new PFont(f, true);
            f = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResource("ttf/fontawesome-webfont.ttf").openStream());
            f = f.deriveFont(Font.PLAIN, 16F);
            iconFont = new PFont(f, true);
        } catch (Exception e) {
            new Dialog().fatalErrorDialog("error occured app closes now");
        }
    }

    @Getter
    private final String[] items;
    @Getter
    private final Map<String, String> buildingNames;
    @Getter
    private final String[] formatOne;
    @Getter
    private final String[] formatTwo;

    public PFont getIconFont() {
        return iconFont;
    }

    public PFont getFont() {
        return txtfont;
    }

    public void setFontSize() {
        AppState.this.setFontSize(14);
    }

    public void setFontSize(int size) {
        app.textFont(txtfont, size);
    }

    public void setStartTime(Calendar c) {
        startTimeObserver.setValue(c);
    }

    public void setEndTime(Calendar c) {
        endTimeObserver.setValue(c);
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

    public Calendar getStartTime() {
        return startTimeObserver.getValue();
    }

    public Calendar getEndTime() {
        return endTimeObserver.getValue();
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
