package timetableapp.Gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;
import processing.data.TableRow;
import timetableapp.models.DataManager;

public class DataView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    private List<Integer> columnsWidth;
    private int page = 0;
    private int btnheight;

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

    private int maxWidth = app.width - 40;
    private int currentWidth = 0;

    @Override
    public void draw() {
        if (ishidden == false) {
            int colNr = 0;

            app.translate(20, 20);
            app.rect(0, 0, app.width - 40, app.height - 170);
            app.textAlign(PApplet.CENTER);
            initializeColumnsWidth();

            for (String column : (List<String>) dm.getTm().getColumns()) {
                int width = columnsWidth.get(colNr);
                int rowheight = btnheight - 7;
                currentWidth += width;
                if (currentWidth < maxWidth) {
                    for (TableRow row : (List<TableRow>) dm.getTm().getPages().get(page)) {
                        int lineheight = rowheight + 7;

                        app.fill(0);
                        rowheight += btnheight;
                        app.text(row.getString(column) != null ? row.getString(column) : "",
                                width - (width / 2), rowheight);

                        app.line(0, lineheight, width, lineheight);
                        app.fill(255);
                    }

                    app.rect(0, 0, width, btnheight);
                    app.line(width, 20, width, app.height - 170);
                    app.fill(0);
                    app.text(column, width - (width / 2), btnheight - 7);
                    app.fill(255);
                    app.translate(width, 0);
                    colNr++;
                }
            }
        }
    }

    private void initializeColumnsWidth() {
        columnsWidth = new ArrayList<>();
        for (String column : (List<String>) dm.getTm().getColumns()) {
            int width = (int) app.textWidth(column) + 10;;
            for (TableRow row : (List<TableRow>) dm.getTm().getPages().get(page)) {
                if (row.getString(column) != null) {
                    int nw = (int) app.textWidth(row.getString(column)) + 10;
                    if (nw > width) {
                        width = nw;
                    }
                }
            }
            columnsWidth.add(width);
        }
    }
}
