package timetableapp.util.observer;

import java.util.Observable;
import lombok.Getter;

public class ObservableValue<T> extends Observable {

    @Getter
    T value;
    T startValue;
    
    public ObservableValue(T value) {
        this.value = value;
        startValue = value;
    }

    public void setValue(T value) {
        this.value = value;
        setChanged();
        notifyObservers();
    }
    public void resetValue() {
        value = startValue;
    }
}
