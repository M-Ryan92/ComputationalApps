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
    private Map<Integer, List> pages = new HashMap<>();
    private int itemsEaPage = 10;

    public TableModel(Table table) {
        data = table;
        columns = Arrays.asList(data.getColumnTitles());
        rows = Arrays.asList(data.rows());

        List temp = new ArrayList();
        for (TableRow row : table.rows()) {
            if (temp.size() < itemsEaPage) {
                temp.add(row);
            } else {
                pages.put(pages.size(), temp);
                temp = new ArrayList();
            }
        }
    }

}
