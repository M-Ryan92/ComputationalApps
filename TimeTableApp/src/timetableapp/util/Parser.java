package timetableapp.util;

import java.io.File;
import processing.core.PApplet;
import processing.data.Table;
import timetableapp.Gui.Dialog;

public class Parser {

    private PApplet app;
    private File file;
    private String extension;
    private Table table;

    public Parser(File file, PApplet app) {
        this.file = file;
        extension = getExtension(file);
        this.app = app;
    }

    public Table parse() {
        table = null;
        if ("txt".equals(extension)) {
            extension = new Dialog().optionDialog(new String[]{"csv", "tsv"}, "is the data cvs or tsv?");
        }

        switch (extension) {
            case ("tsv"):
            case ("tab"):
                table = app.loadTable(file.getAbsolutePath(), "header, tsv");
                break;
            case ("csv"):
                table = app.loadTable(file.getAbsolutePath(), "header, csv");
                break;
            case ("ics"):
                handleUnimplementedExtension(extension);
        }

        return table;
    }

    private String getExtension(File file) {
        int i = file.getAbsolutePath().lastIndexOf('.');
        if (i > 0) {
            return file.getAbsolutePath().substring(i + 1);
        }
        return null;
    }

    public void handleUnimplementedExtension(String extension) {
        new Dialog("not implemented yet for " + extension, Dialog.WARNING_MESSAGE);
        AppState.getInstance().getFileLoadedStateObserver().resetValue();
    }
}
