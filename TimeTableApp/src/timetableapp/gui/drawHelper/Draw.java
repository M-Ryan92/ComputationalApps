package timetableapp.gui.drawHelper;

import processing.core.PApplet;
import timetableapp.util.AppState;

public class Draw {

    private static final AppState state = AppState.getInstance();
    private static final PApplet app = AppState.getInstance().getApp();

    public static void drawDisplay() {
        app.fill(AppState.displayColor);
        app.rect(state.displayPanelXOffset, state.displayPanelYOffset, state.getDisplayPanelWidth(), state.getDisplayPanelHeight());
        app.fill(255);
    }

    public static void drawDisplayMessage(String text) {
        state.setFont(26);
        app.textAlign(PApplet.CENTER);
        String txt = "processing file please wait";
        app.text(txt, (state.getDisplayPanelWidth() / 2), (state.getDisplayPanelHeight() / 2) - 70);
        state.setFont();
    }

}
