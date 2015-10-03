package timetableapp.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import processing.data.Table;
import processing.data.TableRow;
import timetableapp.util.AppState;

public class TableModel {

    private Table data;
    @Getter
    private List<String> columns;
    @Getter
    private Map<String, Integer> columnsWidth;
    @Getter
    private int maxColSize = 400;
    @Getter
    private List rows;
    @Getter
    private Map<Integer, List<DataRow>> pages;
    @Getter
    private int pageCount;

    private int itemsEaPage = Math.round((AppState.getInstance().getApp().height - 20 - 120 - 24) / 24);

    public List<DataRow> getPage(int i) {
        if (i >= pages.size()) {
            return null;
        }
        return pages.get(i);
    }

    public TableModel(Table table) {
        pages = new HashMap<>();
        data = table;
        columns = Arrays.asList(data.getColumnTitles());
        columnsWidth = new HashMap<>();
        rows = Arrays.asList(data.rows());

        List<DataRow> temp = new ArrayList();

        for (TableRow row : data.rows()) {
            temp = initializePage(row, temp);
            calcColumnWidths(row);
        }
        pages.put(pages.size(), temp);

        pageCount = pages.size() - 1;
    }

    private List<DataRow> initializePage(TableRow row, List<DataRow> temp) {
        if (temp.size() < itemsEaPage) {
            temp.add(new DataRow(row));
        }
        if (temp.size() >= itemsEaPage) {
            pages.put(pages.size(), temp);
            temp = new ArrayList();
        }
        return temp;
    }

    private void calcColumnWidths(TableRow row) {
        for (String col : row.getColumnTitles()) {
            String text = row.getString(col);
            if (!columnsWidth.containsKey(col)) {
                columnsWidth.put(col, Math.round((AppState.getInstance().getApp().textWidth(col))) + 20);
            } else {
                if (text != null) {
                    reAdjustColWidth(text, col);
                }
            }
        }
    }

    private void reAdjustColWidth(String text, String col) {
        text = text.trim();
        int width = Math.round((AppState.getInstance().getApp().textWidth(text))) + 20;
        if (width > columnsWidth.get(col)) {
            if (width < maxColSize) {
                columnsWidth.put(col, width);
            } else {
                columnsWidth.put(col, maxColSize + 20);
            }
        }
    }

}
