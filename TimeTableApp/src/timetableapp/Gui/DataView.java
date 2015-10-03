package timetableapp.Gui;

import controlP5.Textfield;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import processing.core.PApplet;
import timetableapp.models.DataManager;
import timetableapp.models.DataRow;
import timetableapp.util.AppState;

public class DataView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    private Map<String, Integer> columnsWidth;
    @Getter
    private int page = 0;
    @Getter
    private int colPage = 0;
    private int btnWidth = 80;
    private int widthOffset, heightOffset, currentWidth, width;
    private List<String> columns;
    private List<Integer> colslides;
    private DataRow currentRow;

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

    public void handleEnter() {
        int maxPages = dm.getTm().getPageCount();
        int selectedPage = getPageNrFromField();

        if (page != selectedPage
                && selectedPage >= 0
                && selectedPage <= maxPages) {
            page = selectedPage;
            setPageBtnState();
        } else {
            setPageNrToField(page);
        }
    }

    private void setColPerPage() {
        colslides = new ArrayList<>();
        currentWidth = 0;
        boolean currentslidebaseSet = true;
        int fitableColumns = 0;
        for (String column : (List<String>) dm.getTm().getColumns()) {
            currentWidth += columnsWidth.get(column);

            if (colslides.size() > 0 && currentslidebaseSet == false) {
                currentWidth += columnsWidth.get(columns.get(0));
                currentslidebaseSet = true;
            }

            if (currentWidth <= state.getDisplayPanelWidth()) {
                fitableColumns++;
            } else {
                colslides.add(fitableColumns != 0 ? fitableColumns : 1);
                fitableColumns = 1;
                currentWidth = columnsWidth.get(column);
                currentslidebaseSet = false;
            }
        }
        if (fitableColumns > 0) {
            colslides.add(fitableColumns);
        }
    }

    private void adjustColumnWidth(int pos) {
        int nextColWidth = 0;
        if (pos < columns.size()) {
            nextColWidth = columnsWidth.get(columns.get(pos));
        }

        if (nextColWidth == 0 || currentWidth + nextColWidth > state.getDisplayPanelWidth()) {
            currentWidth -= width;
            width = state.getDisplayPanelWidth() - currentWidth;
            currentWidth += width;
        }
    }

    private int skipCols() {
        int skipColPages = 0;
        int skipCol = 0;

        while (skipColPages < colPage) {
            skipCol += colslides.get(skipColPages);
            skipColPages++;
        }
        return skipCol;
    }

    private void drawHeader() {
        currentWidth = 0;
        widthOffset = 0;
        heightOffset = 2 * state.getButtonHeight();

        if (skipCols() > 0) {
            String colName = columns.get(0);
            width = columnsWidth.get(colName);
            currentWidth += width;
            adjustColumnWidth(1);

            drawHeaderColumns(width, colName);
            widthOffset += width;
        }

        for (int col = skipCols(); col < columns.size(); col++) {
            String colName = columns.get(col);

            if (currentWidth < state.getDisplayPanelWidth() && colslides.get(colPage) > (col - skipCols())) {
                width = columnsWidth.get(colName);
                currentWidth += width;
                adjustColumnWidth(col + 1);

                drawHeaderColumns(width, colName);
                widthOffset += width;
            } else {
                break;
            }
        }
    }

    private void drawHeaderColumns(int width, String colName) {
        app.rect(widthOffset, 0, width, state.getButtonHeight());
        app.line(widthOffset + width, 0, widthOffset + width, state.getDisplayPanelHeight());
        app.fill(0);
        app.text(colName, widthOffset + (width - (width / 2)), state.getButtonHeight() - 7);
        app.fill(255);
    }

    private void drawRow() {
        currentWidth = 0;
        widthOffset = 0;

        if (skipCols() > 0) {
            String colName = columns.get(0);
            width = columnsWidth.get(colName);
            currentWidth += width;
            adjustColumnWidth(1);

            drawRowColumns(width, colName);
            widthOffset += width;
        }

        for (int col = skipCols(); col < columns.size(); col++) {
            String colName = columns.get(col);
            if (currentWidth < state.getDisplayPanelWidth() && colslides.get(colPage) > (col - skipCols())) {
                width = columnsWidth.get(colName);
                currentWidth += width;
                adjustColumnWidth(col + 1);

                drawRowColumns(width, colName);
                widthOffset += width;
            }
        }
        heightOffset += state.getButtonHeight();
    }

    private void drawRowColumns(int width, String colName) {
        app.fill(0);
        app.line(widthOffset, heightOffset, widthOffset + width, heightOffset);

        String text = currentRow.getString(colName) != null ? currentRow.getString(colName) : "";
        if (app.textWidth(text) <= dm.getTm().getMaxColSize()) {
            app.text(text,
                    widthOffset + (width - (width / 2)), heightOffset - 7);
        } else {
            String concatedtext = "";
            for (char c : text.toCharArray()) {
                if (app.textWidth(concatedtext + c) < dm.getTm().getMaxColSize() - app.textWidth("...")) {
                    concatedtext += c;
                } else {
                    concatedtext += "...";
                    break;
                }
            }
            app.text(concatedtext,
                    widthOffset + (width - (width / 2)), heightOffset - 7);
        }

        app.fill(255);
    }

    @Override
    public void draw() {
        if (ishidden == false) {
            app.fill(AppState.displayColor);
            app.rect(state.displayPanelXOffset, state.displayPanelYOffset, state.getDisplayPanelWidth(), state.getDisplayPanelHeight());
            app.fill(255);

            app.textAlign(PApplet.CENTER);
            columnsWidth = dm.getTm().getColumnsWidth();
            columns = dm.getTm().getColumns();

            app.translate(20, 20);

            if (colslides == null) {
                setColPerPage();
            }

            drawHeader();
            for (DataRow row : dm.getTm().getPage(page)) {
                currentRow = row;
                drawRow();
            }

            app.translate(-20, -20);
        }
    }

    public void setPage(int p) {
        page = p;
        setPageNrToField(p);
    }

    public void colPagePlus() {
        if (colPage + 1 < colslides.size()) {
            colPage += 1;
            setColPageBtnState();
            draw();
        }
    }

    public void colPageMinus() {
        if (colPage > 0) {
            colPage -= 1;
            setColPageBtnState();
            draw();
        }
    }

    public void setColPageBtnState() {
        if (colPage + 1 >= colslides.size()) {
            getcontrollerByName("showNextColumns").hide();
        } else {
            getcontrollerByName("showNextColumns").show();
        }
        if (colPage == 0) {
            getcontrollerByName("showPreviousColumns").hide();
        } else {
            getcontrollerByName("showPreviousColumns").show();
        }
    }

    public void pagePlus() {
        if (page < dm.getTm().getPageCount()) {
            page += 1;
            setPageBtnState();
            setPageNrToField(page);
            draw();
        }
    }

    public void pageMinus() {
        if (page > 0) {
            page -= 1;
            setPageBtnState();
            setPageNrToField(page);
            draw();
        }
    }

    public void setPageBtnState() {
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
