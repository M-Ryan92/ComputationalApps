package timetableapp.eventhandlers;

import java.util.concurrent.Callable;
import processing.data.Table;
import timetableapp.models.DataManager;
import timetableapp.models.TableModel;
import timetableapp.util.AppState;
import timetableapp.util.Parser;

public class NewFileSelectedHandler implements Callable {

    private AppState state = AppState.getInstance();
    private Table data;

    @Override
    public Object call() throws Exception {
        try {
            state.getNewFileSelectedStateObserver().resetValue();
            state.setLoadingFileState(1);

            data = new Parser(state.getSelectedFile()).parse();
            DataManager dm = DataManager.getInstance();
            dm.setTm(new TableModel(data));

            state.getLoadingFileStateObserver().resetValue();
            state.setFileLoadedState(1);
            
            return null;
        } catch (Exception e) {
            throw new Exception("could not load file properly, application will close now");
        }
    }

}
