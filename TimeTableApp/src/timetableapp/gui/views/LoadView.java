package timetableapp.gui.views;

import java.util.logging.Level;
import java.util.logging.Logger;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.radians;
import static processing.core.PApplet.sin;
import timetableapp.BaseApplication;
import timetableapp.gui.BaseView;
import timetableapp.gui.drawHelper.Draw;
import timetableapp.util.AppState;

public class LoadView extends BaseView {

    private int start = 0;
    private int width = app.width;
    private int height = app.height;

    public LoadView() {
        super();
    }

    @Override
    public void draw() {
        int x = 0;
        int r = 70;
        int s = 20;

        Draw.drawDisplay();

        Draw.drawDisplayMessage("processing file please wait");
        
        app.translate((state.getDisplayPanelWidth() / 2), (state.getDisplayPanelHeight() / 2) + 50);
        app.noStroke();
        
        while (x < 360) {
            app.fill(AppState.specialColor);
            if (x == start) {
                app.fill(AppState.specialActiveColor);
            }

            app.ellipse(sin(-radians(x)) * r, cos(-radians(x)) * r, s, s);
            x += 30;
        }
        
        app.stroke(AppState.strokeColor);
        start += 30;
        if (start == 360) {
            start = 0;
        }
        try {
            Thread.sleep(150);
        } catch (InterruptedException ex) {
            Logger.getLogger(BaseApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

        app.fill(255);
        app.translate(-(state.getDisplayPanelWidth()), -(state.getDisplayPanelHeight() + 50));
    }

}
