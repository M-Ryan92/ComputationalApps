package timetableapp.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import processing.data.Table;
import processing.data.TableRow;

public class TableModel {

    private Table data;
    @Getter
    private List columns;
    @Getter
    private List rows;

    @Getter
    private Map<Integer, List<TableRow>> pages;
    private int itemsEaPage = 1;

    public TableModel(Table table) {
        pages = new HashMap<>();
        data = table;
        columns = Arrays.asList(data.getColumnTitles());
        rows = Arrays.asList(data.rows());

        List<TableRow> temp = new ArrayList();
        for (TableRow row : data.rows()) {
            if (temp.size() < itemsEaPage) {
                temp.add(row);
            }
            if (temp.size() >= itemsEaPage) {
                pages.put(pages.size(), temp);
                temp = new ArrayList();
            }
        }
    }

}
