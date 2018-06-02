package sample.Board;

import java.util.Arrays;
import java.util.List;

public class ConnectedBoardElement extends BoardElement {
    private List<BoardElement> connectedWith;

    public ConnectedBoardElement(double posX, double posY, String imagePath, BoardElement... connectedWith){
        super(posX, posY, imagePath);
        this.connectedWith = Arrays.asList(connectedWith);
    }
}
