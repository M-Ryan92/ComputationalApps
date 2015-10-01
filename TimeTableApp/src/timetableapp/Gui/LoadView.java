package timetableapp.Gui;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.PApplet;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.radians;
import static processing.core.PApplet.sin;
import timetableapp.BaseApplication;
import timetableapp.util.AppState;

public class LoadView extends BaseView {

    public LoadView(Map<String, ?> properties) {
        super(properties);
    }

    @Override
    protected void checkProperties() {

    }

    private int start = 0;
    private int width = app.width;
    private int height = app.height;
    
    @Override
    protected void draw() {
        int x = 0;
        int r = 70;
        int s = 20;
        app.noStroke();
        app.fill(AppState.displayColor);
        app.rect(20, 20, width - 40, height - 120);

        app.translate(width / 2, height / 2);

        app.fill(255);
        AppState.getInstance().setFont(26);
        app.textAlign(PApplet.CENTER);
        app.text("loading file please wait", 0, -130);
        AppState.getInstance().setFont();
        while (x < 360) {
            app.fill(AppState.specialColor);
            if (x == start) {
                app.fill(AppState.specialActiveColor);
            }

            app.ellipse(sin(-radians(x)) * r, cos(-radians(x)) * r, s, s);
            x += 30;
        }
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
        app.translate(-(width / 2), -(height / 2));
    }

}
