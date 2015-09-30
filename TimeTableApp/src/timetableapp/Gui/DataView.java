package timetableapp.Gui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;
import processing.data.TableRow;
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

    private int page = 5;

    @Override
    public void draw() {
        if (ishidden == false) {

            app.translate(20, 20);
            int offset = 20;
            app.rect(0, 0, app.width - 40, app.height - 170);

            app.textAlign(PApplet.CENTER);

            dm.getTm().getColumns().forEach(s -> {
                String txt = (String) s;
                int width = (int) app.textWidth(txt) + 10;
                int rowheight = btnheight - 7;
                for (TableRow row : (List<TableRow>) dm.getTm().getPages().get(page)) {
                    if (row.getString(txt) != null) {
                        int nw = (int) app.textWidth(row.getString(txt)) + 10;
                        if (nw > width) {
                            width = nw;
                        }
                    }
                    app.fill(0);
                    rowheight += btnheight;

                    String colValue = row.getString(txt) != null ? row.getString(txt) : "";
                    app.text(colValue, width - (width / 2), rowheight);
                    int lineheight = rowheight + 7;
                    app.line(0, lineheight, app.width - 20, lineheight);
                    
                    app.fill(255);
                }

                app.rect(0, 0, width, btnheight);
                app.line(width, 20, width, app.height - 170);
                app.fill(0);
                app.text(txt, width - (width / 2), btnheight - 7);
                app.fill(255);
                app.translate(width, 0);
            });

        }
    }
}
