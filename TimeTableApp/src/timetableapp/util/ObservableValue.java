package timetableapp.util;

import java.util.Observable;
import lombok.Getter;

public class ObservableValue<T> extends Observable {

    @Getter
    T value;

    public ObservableValue(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
        setChanged();
        notifyObservers();
    }
}
