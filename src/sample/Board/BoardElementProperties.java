package sample.Board;

import java.io.Serializable;

public class BoardElementProperties implements Serializable {
    private boolean permeable;
    private boolean used;

    public BoardElementProperties(boolean permeable, boolean used) {
        this.permeable = permeable;
        this.used = used;
    }

    public boolean isPermeable() {
        return permeable;
    }

    public void setPermeable(boolean permeable) {
        this.permeable = permeable;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
