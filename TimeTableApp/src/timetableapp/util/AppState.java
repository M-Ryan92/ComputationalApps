package timetableapp.util;

import lombok.Getter;

public class AppState {

    private @Getter
    ObservableValue<Integer> fileSelectedStateObserver = new ObservableValue(0);
    private @Getter
    ObservableValue<Integer> newFileSelectedStateObserver = new ObservableValue(0);
    private @Getter
    ObservableValue<Integer> loadingFileStateObserver = new ObservableValue(0);
    private @Getter
    ObservableValue<Integer> fileLoadedStateObserver = new ObservableValue(0);
    private @Getter
    ObservableValue<Integer> selectedViewStateObserver = new ObservableValue(0);

    private static AppState instance = new AppState();

    public static AppState getInstance() {
        return instance;
    }

    private AppState() {

    }

    public int getFileSelectedState() {
        return fileSelectedStateObserver.getValue();
    }

    public void setFileSelectedState(int value) {
        fileSelectedStateObserver.setValue(value);
    }

    public int getNewFileSelectedState() {
        return newFileSelectedStateObserver.getValue();
    }

    public void setNewFileSelectedState(int value) {
        newFileSelectedStateObserver.setValue(value);
    }

    public int getLoadingFileState() {
        return loadingFileStateObserver.getValue();
    }

    public void setLoadingFileState(int value) {
        loadingFileStateObserver.setValue(value);
    }

    public int getFileLoadedState() {
        return fileLoadedStateObserver.getValue();
    }

    public void setFileLoadedState(int value) {
        fileLoadedStateObserver.setValue(value);
    }

    public int getSelectedViewState() {
        return selectedViewStateObserver.getValue();
    }

    public void setSelectedViewState(int value) {
        selectedViewStateObserver.setValue(value);
    }
}
