package sample.Board;

import java.io.Serializable;

public class BoardElementProperties implements Serializable {
    private boolean permeable;
    private boolean used;
    private int delayTime;
    private char sign;

    public BoardElementProperties(boolean permeable, boolean used, char sign) {
        this.permeable = permeable;
        this.used = used;
        this.delayTime = -1;
        this.sign = sign;
    }

    public BoardElementProperties(boolean permeable, boolean used, int delayTime, char sign) {
        this.permeable = permeable;
        this.used = used;
        this.delayTime = delayTime;
        this.sign = sign;
    }

    public BoardElementProperties(BoardElementProperties elementProperties) {
        this.permeable = elementProperties.permeable;
        this.used = elementProperties.used;
        this.delayTime = elementProperties.delayTime;
        this.sign = elementProperties.sign;
    }

    public boolean isPermeable() {
        return permeable;
    }

    public synchronized void setPermeable(boolean permeable) {
        this.permeable = permeable;
    }

    public boolean isUsed() {
        return used;
    }

    public synchronized void setUsed(boolean used) {
        this.used = used;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public synchronized void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public char getSign() {
        return sign;
    }
}
