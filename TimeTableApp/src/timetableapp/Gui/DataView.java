package timetableapp.Gui;

import controlP5.Textfield;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import processing.core.PApplet;
import timetableapp.models.DataManager;
import timetableapp.models.DataRow;

public class DataView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    private List<Integer> columnsWidth;

    @Getter
    private int page = 0;
    private int btnheight;
    private int btnWidth = 80;
    private int maxWidth = app.width - 40;

    public void pagePlus() {
        if (page < dm.getTm().getPageCount()) {
            page += 1;
            setBtnState();
            setPageNrToField(page);
            draw();
        }
    }

    public void pageMinus() {
        if (page > 0) {
            page -= 1;
            setBtnState();
            setPageNrToField(page);
            draw();
        }
    }

    public void handleEnter() {
        int maxPages = dm.getTm().getPageCount();
        int selectedPage = getPageNrFromField();

        if (page != selectedPage
                && selectedPage >= 0
                && selectedPage <= maxPages) {
            page = selectedPage;
            setBtnState();
        } else {
            setPageNrToField(page);
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
                .addButton(cp5, "PreviousPage")
                .setPosition((app.width / 2) - (btnWidth / 2) - btnWidth - 10, app.height - 130)
                .setSize(btnWidth, btnheight)
                .setLabel("Previous Page")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "NextPage")
                .setPosition((app.width / 2) - (btnWidth / 2) + btnWidth + 10, app.height - 130)
                .setSize(btnWidth, btnheight)
                .setLabel("Next Page")
                .hide());

        getControllers().add(cp5
                .addTextfield("PageNr")
                .setValue("1")
                .setPosition((app.width / 2) - 30, app.height - 130)
                .setSize(60, btnheight)
                .setFont(app.createFont("arial", 20))
                .setAutoClear(false)
                .hide());
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

    public void setPage(int p) {
        page = p;
        setPageNrToField(p);
    }

    public void setBtnState() {
        if (page >= dm.getTm().getPageCount()) {
            getcontrollerByName("NextPage").hide();
        } else {
            getcontrollerByName("NextPage").show();
        }
        if (page == 0) {
            getcontrollerByName("PreviousPage").hide();
        } else {
            getcontrollerByName("PreviousPage").show();
        }
    }

    public void setPageNrToField(int p) {
        ((Textfield) getcontrollerByName("PageNr")).setText(String.valueOf(p + 1));
    }

    public int getPageNrFromField() {
        return Integer.valueOf(((Textfield) getcontrollerByName("PageNr")).getText()) - 1;
    }

}
