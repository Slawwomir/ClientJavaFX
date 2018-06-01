package sample.Player;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Player {
    private ImageView character;
    private Point2D position;

    public Player(){
        position = new Point2D(10, 10);
        character = new ImageView(new Image("http://icons.iconarchive.com/icons/custom-icon-design/flatastic-6/72/Circle-icon.png"));
    }

    public ImageView getCharacter() {
        return character;
    }

    public Point2D getPosition() {
        return position;
    }
}
