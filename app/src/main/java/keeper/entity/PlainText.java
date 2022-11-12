package keeper.entity;

import java.io.Serializable;
import java.util.Map;

public class PlainText<T extends Serializable> implements Serializable {
    private final Map<T, T> value;

    public PlainText(Map<T, T> value) {
        this.value = value;
    }

    public Map<T, T> getValue() {
        return value;
    }
}
