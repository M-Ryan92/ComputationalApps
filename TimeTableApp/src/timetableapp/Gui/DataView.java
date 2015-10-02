package timetableapp.Gui;

import controlP5.Textfield;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import processing.core.PApplet;
import timetableapp.models.DataManager;
import timetableapp.models.DataRow;
import timetableapp.util.AppState;

public class DataView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    private List<Integer> columnsWidth;

    @Getter
    private int page = 0;
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

    public DataView() {
        super();
        ishidden = true;

        getControllers().add(cp5
                .addButton(cp5, "showPreviousColumns")
                .setColorBackground(AppState.buttonColor)
                .setPosition(app.width - (btnWidth * 2) - 30, app.height - 130)
                .setSize(btnWidth, state.getButtonHeight())
                .setLabel("Previous Columns")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "showNextColumns")
                .setColorBackground(AppState.buttonColor)
                .setPosition(app.width - btnWidth - 20, app.height - 130)
                .setSize(btnWidth, state.getButtonHeight())
                .setLabel("Next Columns")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "BackToMainView")
                .setColorBackground(AppState.buttonColor)
                .setPosition(20, app.height - 130)
                .setSize(btnWidth, state.getButtonHeight())
                .setLabel("Back")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "PreviousPage")
                .setColorBackground(AppState.buttonColor)
                .setPosition((app.width / 2) - (btnWidth / 2) - btnWidth - 10, app.height - 130)
                .setSize(btnWidth, state.getButtonHeight())
                .setLabel("Previous Page")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "NextPage")
                .setColorBackground(AppState.buttonColor)
                .setPosition((app.width / 2) - (btnWidth / 2) + btnWidth + 10, app.height - 130)
                .setSize(btnWidth, state.getButtonHeight())
                .setLabel("Next Page")
                .hide());

        getControllers().add(cp5
                .addTextfield("PageNr")
                .setColorBackground(AppState.buttonColor)
                .setValue("1")
                .setPosition((app.width / 2) - 30, app.height - 130)
                .setSize(60, state.getButtonHeight())
                .setFont(app.createFont("arial", 20))
                .setAutoClear(false)
                .hide());
        ((Textfield) getcontrollerByName("PageNr")).getCaptionLabel().alignX(PApplet.CENTER);
        //((Textfield) getcontrollerByName("PageNr")).getValueLabel().alignX(PApplet.CENTER);
        //((Textfield) getcontrollerByName("PageNr")).updateAbsolutePosition();
    }

    @Override
    public void draw() {
        if (ishidden == false) {
            int currentWidth = 0;
            int colNr = 0;
            int widthOffset = 0;
            app.rect(state.displayPanelXOffset, state.displayPanelYOffset, state.getDisplayPanelWidth(), state.getDisplayPanelHeight());
            app.textAlign(PApplet.CENTER);
            initializeColumnsWidth();
            app.translate(20, 20);
            for (String column : (List<String>) dm.getTm().getColumns()) {
                int width = columnsWidth.get(colNr);
                int rowheight = state.getButtonHeight() - 7;
                currentWidth += width;
                if (currentWidth < maxWidth) {
                    for (DataRow row : dm.getTm().getPage(page)) {
                        int lineheight = rowheight + 7;

                        app.fill(0);
                        rowheight += state.getButtonHeight();
                        app.text(row.getString(column) != null ? row.getString(column) : "",
                                widthOffset + (width - (width / 2)), rowheight);
                        app.line(widthOffset, lineheight, widthOffset + width, lineheight);
                        app.fill(255);
                    }
                    app.rect(widthOffset, 0, width, state.getButtonHeight());
                    app.line(widthOffset + width, 0, widthOffset + width, state.getDisplayPanelHeight());
                    app.fill(0);
                    app.text(column, widthOffset + (width - (width / 2)), state.getButtonHeight() - 7);
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
        try {
            return Integer.valueOf(((Textfield) getcontrollerByName("PageNr")).getText()) - 1;
        } catch (Exception e) {
            return page;
        }
    }

}
