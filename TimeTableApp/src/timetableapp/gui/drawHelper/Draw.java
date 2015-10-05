package timetableapp.gui.drawHelper;

import processing.core.PApplet;
import timetableapp.util.Properties;
import timetableapp.util.state.AppState;

public class Draw {

    private static final AppState state = AppState.getInstance();
    private static final PApplet app = AppState.getInstance().getApp();

    public static void drawDisplay() {
        app.fill(Properties.displayColor);
        app.rect(Properties.displayPanelXOffset, Properties.displayPanelYOffset, state.getDisplayPanelWidth(), state.getDisplayPanelHeight());
        app.fill(255);
    }

    public static void drawDisplayMessage(String text) {
        state.setFont(30);
        app.textAlign(PApplet.CENTER);
        app.text(text, (state.getDisplayPanelWidth() / 2), (state.getDisplayPanelHeight() / 2) - 70);
        state.setFont();
    }

}
