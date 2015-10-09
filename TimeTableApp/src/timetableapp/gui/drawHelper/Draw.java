package timetableapp.gui.drawHelper;

import processing.core.PApplet;
import timetableapp.util.AppProperties;
import timetableapp.util.state.AppState;

public class Draw {

    private static final AppState state = AppState.getInstance();
    private static final PApplet app = AppState.getInstance().getApp();

    public static void drawDisplay() {
        app.fill(AppProperties.displayColor);
        app.rect(AppProperties.displayPanelXOffset, AppProperties.displayPanelYOffset, state.getDisplayPanelWidth(), state.getDisplayPanelHeight());
        app.fill(255);
    }

    public static void drawDisplayMessage(String text) {
        state.setFontSize(30);
        app.textAlign(PApplet.CENTER);
        app.text(text, (state.getDisplayPanelWidth() / 2), (state.getDisplayPanelHeight() / 2) - 70);
        state.setFontSize();
    }

}
