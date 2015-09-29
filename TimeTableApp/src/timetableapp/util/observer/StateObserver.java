package timetableapp.util.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import timetableapp.Gui.Dialog;

public class StateObserver implements Observer {

    private Callable callable;

    public StateObserver(Callable callable) {
        this.callable = callable;
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            callable.call();
        } catch (Exception ex) {
            Dialog dialog = new Dialog();
            dialog.fatalErrorDialog(ex.getMessage());
        }
    }
}
