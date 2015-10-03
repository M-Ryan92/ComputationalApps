package timetableapp.models;

import java.util.HashMap;
import java.util.Map;
import processing.data.TableRow;

public class DataRow {

    private Map<String, String> columns;

    public DataRow(TableRow row) {
        columns = new HashMap<>();
        for (String col : row.getColumnTitles()) {
            columns.put(col, row.getString(col));
        }
    }

    public String getString(String s) {
        return columns.get(s);
    }
}
