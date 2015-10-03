package timetableapp.gui.drawHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import timetableapp.models.DataManager;
import timetableapp.models.table.DataRow;
import timetableapp.util.Properties;
import timetableapp.util.state.AppState;

public class DrawTable {

    private DataManager dm;
    private AppState state;
    PApplet app;

    @Getter
    @Setter
    private int page = 0;
    @Getter
    @Setter
    private int colPage = 0;

    private int widthOffset, heightOffset,
            currentWidth, width;
    private List<String> columns;
    private List<Integer> colslides;
    private Map<String, Integer> columnsWidth;
    private DataRow currentRow;

    public DrawTable() {
        dm = DataManager.getInstance();
        state = AppState.getInstance();
        app = state.getApp();
    }

    public void draw() {
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

    public int getColSlidesCount() {
        return colslides.size();
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
        heightOffset = 2 * Properties.buttonHeight;

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
        app.rect(widthOffset, 0, width, Properties.buttonHeight);
        app.line(widthOffset + width, 0, widthOffset + width, state.getDisplayPanelHeight());
        app.fill(0);
        app.text(colName, widthOffset + (width - (width / 2)), Properties.buttonHeight - 7);
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
        heightOffset += Properties.buttonHeight;
    }

    private void drawRowColumns(int width, String colName) {
        app.fill(0);
        app.line(widthOffset, heightOffset, widthOffset + width, heightOffset);

        String text = currentRow.getString(colName) != null ? currentRow.getString(colName) : "";
        if (app.textWidth(text) <= dm.getTm().getMaxColSize()) {
            app.text(text,
                    widthOffset + (width - (width / 2)), heightOffset - 7);
        } else {
            app.text(concatText(text),
                    widthOffset + (width - (width / 2)), heightOffset - 7);
        }

        app.fill(255);
    }

    private String concatText(String text) {
        String concatedtext = "";
        for (char c : text.toCharArray()) {
            if (app.textWidth(concatedtext + c) < dm.getTm().getMaxColSize() - app.textWidth("...")) {
                concatedtext += c;
            } else {
                concatedtext += "...";
                break;
            }
        }
        return concatedtext;
    }

}
