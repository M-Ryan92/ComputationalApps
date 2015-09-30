package timetableapp.Gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;
import timetableapp.models.DataManager;
import timetableapp.models.DataRow;

public class DataView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    private List<Integer> columnsWidth;
    private int page = 0;
    private int btnheight;
    private int btnWidth = 80;

    public void pagePlus() {
        page += 1;
        List<DataRow> p = dm.getTm().getPage(page);
        if (p==null){
        page -= 1;
        } else {
            app.rect(20, 20, app.width - 40, app.height - 170);
            System.out.println("page:"+page);
            System.out.println(p.get(0).getString("Activity"));
        }
    }

    public void pageMinus() {
        
        if (page > 0) {
            page -= 1;
            List<DataRow> p = dm.getTm().getPage(page);
            app.rect(20, 20, app.width - 40, app.height - 170);
            System.out.println("page:"+page);
            System.out.println(p.get(0).getString("Activity"));
        }
    }

    public DataView(Map<String, ?> properties) {
        super(properties);
        btnheight = (Integer) properties.get("btnheight");
        ishidden = true;

        getControllers().add(cp5
                .addButton(cp5, "showPreviousColumns")
                .setPosition(app.width - (btnWidth * 2) - 30, app.height - 130)
                .setSize(btnWidth, btnheight)
                .setLabel("Previous Columns")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "showNextColumns")
                .setPosition(app.width - btnWidth - 20, app.height - 130)
                .setSize(btnWidth, btnheight)
                .setLabel("Next Columns")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "BackToMainView")
                .setPosition(20, app.height - 130)
                .setSize(btnWidth, btnheight)
                .setLabel("Back")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "NextPage")
                .setPosition((app.width / 2) - (btnWidth * 2) + 20, app.height - 130)
                .setSize(btnWidth, btnheight)
                .setLabel("Next Page")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "PreviousPage")
                .setPosition((app.width / 2) + (btnWidth * 2) - 20, app.height - 130)
                .setSize(btnWidth, btnheight)
                .setLabel("Previous Page")
                .hide());
    }

    @Override
    protected void checkProperties() {
        if (!properties.containsKey("btnheight")) {
            new Dialog().fatalErrorDialog("could not render application view, closing application");
        }
    }

    private int maxWidth = app.width - 40;

    @Override
    public void draw() {
        if (ishidden == false) {
            int currentWidth = 0;
            int colNr = 0;
            int widthOffset = 0;
            app.rect(20, 20, app.width - 40, app.height - 170);
            app.textAlign(PApplet.CENTER);
            initializeColumnsWidth();
            app.translate(20, 20);
            for (String column : (List<String>) dm.getTm().getColumns()) {
                int width = columnsWidth.get(colNr);
                int rowheight = btnheight - 7;
                currentWidth += width;
                if (currentWidth < maxWidth) {
                    for (DataRow row : dm.getTm().getPage(page)) {
                        int lineheight = rowheight + 7;

                        app.fill(0);
                        rowheight += btnheight;
                        app.text(row.getString(column) != null ? row.getString(column) : "",
                                widthOffset + (width - (width / 2)), rowheight);

                        app.line(widthOffset, lineheight, widthOffset + width, lineheight);
                        app.fill(255);
                    }

                    app.rect(widthOffset, 0, width, btnheight);
                    app.line(widthOffset + width, 0, widthOffset + width, app.height - 170);
                    app.fill(0);
                    app.text(column, widthOffset + (width - (width / 2)), btnheight - 7);
                    app.fill(255);
                    widthOffset += width;
                    colNr++;
                }
            }
            app.translate(-20, -20);
        }
    }

    private void initializeColumnsWidth() {
        columnsWidth = new ArrayList<>();
        for (String column : (List<String>) dm.getTm().getColumns()) {
            int width = (int) app.textWidth(column) + 10;
            for (DataRow row : dm.getTm().getPage(page)) {
                
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
