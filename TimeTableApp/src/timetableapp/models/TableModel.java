package timetableapp.models;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import processing.data.Table;

public class TableModel {

    private Table data;
    @Getter
    private List columns;

    public TableModel(Table table) {
        data = table;
        columns = Arrays.asList(data.getColumnTitles());

    }

}
