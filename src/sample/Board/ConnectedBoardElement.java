package sample.Board;

import java.util.Arrays;
import java.util.List;

public class ConnectedBoardElement extends BoardElement implements Usable {
    private List<BoardElement> connectedWith;
    private boolean used;
    private boolean reversible;
    private boolean deactivated;    // preventing endless loop

    public ConnectedBoardElement(double posX, double posY, String imagePath, boolean used, boolean reversible,
                                 BoardElement... connectedWith){
        super(posX, posY, imagePath);
        this.used = used;
        this.reversible = reversible;
        this.connectedWith = Arrays.asList(connectedWith);
        this.deactivated = false;
    }

    public boolean isUsed(){
        return used;
    }

    @Override
    public void use() {
        if(!deactivated && (!used || reversible)) {
            deactivated = true;                                     // for loops in connections paths
             for (BoardElement boardElement : connectedWith)
                if (boardElement instanceof Usable)
                    ((Usable) boardElement).use();
            deactivated = false;
        }

        used = !reversible || !used;
    }

}
