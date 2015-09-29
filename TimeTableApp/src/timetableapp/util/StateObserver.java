package timetableapp.util;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import timetableapp.Gui.Dialog;

public class StateObserver implements Observer{

    Callable callable;
    public StateObserver(Callable callable) {
        this.callable = callable;
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            callable.call();
        } catch (Exception ex) {
            new Dialog("could not update application state", Dialog.ERROR_MESSAGE);
        }
    }
}