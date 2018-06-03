package sample.Player;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class Player {
    private ImageView character;
    private Point2D position;
    private double size;

    public Player(){
        size = 35;
        position = new Point2D(size+1, size+1);
        character = new ImageView(
                new Image("http://icons.iconarchive.com/icons/custom-icon-design/flatastic-6/72/Circle-icon.png",
                        size, size, false, false));
        character.setX(size+1);
        character.setY(size+1);
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

    public double getX(){
        return character.getX();
    }

    public double getY(){
        return character.getY();
    }

    public double getSize(){
        return size;
    }

    public void setY(double y){
        character.setY(y);
        //position.add(0, y);
    }

    public void setX(double x){
        character.setX(x);
        //position.add(x, 0);
    }
}

