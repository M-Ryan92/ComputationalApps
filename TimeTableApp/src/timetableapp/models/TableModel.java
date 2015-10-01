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
    private Map<Integer, List<DataRow>> pages;

    @Getter
    private int pageCount;

    public List<DataRow> getPage(int i) {
        if (i >= pages.size()) {
            return null;
        }
        return pages.get(i);
    }

    private int itemsEaPage = 10;

    public TableModel(Table table) {
        pages = new HashMap<>();
        data = table;
        columns = Arrays.asList(data.getColumnTitles());
        rows = Arrays.asList(data.rows());

        List<DataRow> temp = new ArrayList();
        for (TableRow row : data.rows()) {
            if (temp.size() < itemsEaPage) {
                temp.add(new DataRow(row));
            }
            if (temp.size() >= itemsEaPage) {
                pages.put(pages.size(), temp);
                temp = new ArrayList();
            }
        }
        pageCount = pages.size() - 1;
    }

}
