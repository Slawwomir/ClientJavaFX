package sample.Player;

import java.io.Serializable;

public class PlayerProperties implements Serializable {
    public int x;
    public int y;

    public PlayerProperties(Player player){
            //this.x = (int)player.getPosition().getX();
            //this.y = (int)player.getPosition().getY();
        this.x = (int)player.getCharacter().getX();
        this.y = (int)player.getCharacter().getY();
    }
}
