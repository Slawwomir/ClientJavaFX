package sample.Player;

import java.io.Serializable;

public class PlayerProperties implements Serializable {
    public double x;
    public double y;

    public PlayerProperties(Player player){
        //this.x = player.getCharacter().getX();
        //this.y = player.getCharacter().getY();
        this.x = player.getX();
        this.y = player.getY();
    }
}
