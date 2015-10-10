package timetableapp.util.observer;

import java.util.Observable;
import java.util.Observer;
import timetableapp.gui.Dialog;

public class StateObserver implements Observer {

    private Runnable runnable;

    public StateObserver(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            runnable.run();
        } catch (Exception ex) {
            new Dialog().fatalErrorDialog(ex.getMessage());
        }
    }
}
