package sample.Player;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class Player {
    private ImageView character;
    private Point2D position;

    public Player(){
        position = new Point2D(10, 10);
        character = new ImageView(new Image("http://icons.iconarchive.com/icons/custom-icon-design/flatastic-6/72/Circle-icon.png"));
    }

    public void update(Player younger){
        position = younger.position;
        character = younger.character;
    }

    public void update(PlayerProperties younger){
        position = new Point2D(younger.x, younger.y);
        character.setX(younger.x);
        character.setY(younger.y);
    }


    public ImageView getCharacter() {
        return character;
    }

    public Point2D getPosition() {
        return position;
    }

    public PlayerProperties getProperties(){
        return new PlayerProperties(this);
    }
}

