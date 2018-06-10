package sample.Board;

import java.util.*;

public class ConnectedBoardElement extends BoardElement implements Usable {
    private List<BoardElement> connectedWith;
    private boolean used;
    private boolean reversible;
    private boolean deactivated;    // preventing endless loop
    private boolean availableDirectly;
    private String pathUsed;
    private String pathNotUsed;

    public ConnectedBoardElement(double posY, double posX, double elementSize, String imagePath, String imagePathUsed,
                                 boolean permeable, boolean used, boolean reversible, boolean availableDirectly){
        super(posY, posX, elementSize, imagePath, permeable);
        this.used = used;
        this.pathNotUsed = imagePath;
        this.pathUsed = imagePathUsed;
        this.reversible = reversible;
        this.connectedWith = new ArrayList<>();
        this.deactivated = false;
        this.availableDirectly = availableDirectly;
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

        if(used = !reversible || !used){
            this.changeImageView(pathUsed);
        }else {
            this.changeImageView(pathNotUsed);
        }
    }

    public void useDirectly(){
        if(availableDirectly)
            use();
    }

    public void addConnection(BoardElement connected){
        connectedWith.add(connected);
    }
}
