package timetableapp.Gui;

import java.util.Map;
import processing.core.PApplet;
import timetableapp.models.DataManager;

public class DataView extends BaseView {

    private int btnheight;
    private DataManager dm = DataManager.getInstance();

    public DataView(Map<String, ?> properties) {
        super(properties);
        btnheight = (Integer) properties.get("btnheight");
        ishidden = true;
    }

    @Override
    protected void checkProperties() {
        if (!properties.containsKey("btnheight")) {
            new Dialog().fatalErrorDialog("could not render application view, closing application");
        }
    }

    @Override
    public void draw() {
        if (ishidden == false) {
            
            app.translate(20, 20);
            int offset = 20;
            app.rect(0, 0, app.width - 40, app.height - 170);
            
            app.textAlign(PApplet.CENTER);
            
            dm.getTm().getColumns().forEach(s -> {
                String txt = (String) s;
                int width = (int)app.textWidth(txt) + 10;
                app.rect(0, 0, width, btnheight);
                app.line(width, 20, width, app.height - 170);
                app.fill(0);
                app.text(txt, width - (width/2), btnheight -7);
                app.fill(255);
                app.translate(width, 0);
            });
            app.translate(0, 0);

        }
    }
}
