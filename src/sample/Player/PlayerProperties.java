package sample.Player;

import java.io.Serializable;

public class PlayerProperties implements Serializable {
    public double x;
    public double y;
    public int id;

    public PlayerProperties(Player player){
        this.x = player.getX();
        this.y = player.getY();
        this.id = player.getId();
    }

    public PlayerProperties(double x, double y, int id){
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public synchronized void update(PlayerProperties pp){
        x = pp.x;
        y = pp.y;
        id = pp.id;
    }
}