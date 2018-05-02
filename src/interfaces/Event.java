package interfaces;

import java.io.Serializable;

public interface Event extends Serializable {
    void accept(EventProcess processor);
}
