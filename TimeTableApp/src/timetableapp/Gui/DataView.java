package timetableapp.Gui;

import java.util.Map;

public class DataView extends BaseView {

    private int btnheight;
    private boolean ishidden;

    public DataView(Map<String, ?> properties) {
        super(properties);
        btnheight = (Integer) properties.get("btnheight");
        ishidden = true;
    }

    public void hide() {
        ishidden = true;
        cp5.getAll().stream().forEach(ci -> ci.hide());
        app.background(state.getBackgroundcolor());
    }

    public void show() {
        ishidden = false;
        cp5.getAll().stream().forEach(ci -> ci.show());
        app.background(state.getBackgroundcolor());
        draw();
    }

    private void draw() {
        if (ishidden == false) {
            app.rect(20, 50, app.width - 40, app.height - 200);
        }
    }

    @Override
    protected void checkProperties() {
        if (!properties.containsKey("btnheight")) {
            new Dialog().fatalErrorDialog("could not render application view, closing application");
        }
    }
}
