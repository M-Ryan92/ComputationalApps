package timetableapp.eventhandlers;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import processing.data.Table;
import timetableapp.gui.Dialog;
import timetableapp.models.DataManager;
import timetableapp.models.TableModel;
import timetableapp.util.AppState;
import timetableapp.util.Parser;

public class NewFileSelectedHandler implements Callable {

    private AppState state = AppState.getInstance();

    @Override
    public Object call() throws Exception {
        ExecutorService tp = Executors.newFixedThreadPool(1);
        tp.submit(() -> {
            try {
                state.getNewFileSelectedStateObserver().resetValue();
                state.setLoadingFileState(1);
                state.setFont();
                Table data = new Parser(state.getSelectedFile()).parse();
                DataManager dm = DataManager.getInstance();
                dm.setTm(new TableModel(data));
                state.getLoadingFileStateObserver().resetValue();
                state.setFileLoadedState(1);
            } catch (Exception e) {
                new Dialog().fatalErrorDialog("could not load file properly, application will close now");
            }
        });
        return null;
    }

}
