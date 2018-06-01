package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import sample.Player.Player;

import java.util.ArrayList;
import java.util.List;

public class ViewController {

    @FXML
    private Pane mainPane;

    private ImageView character;
    private List<Player> PlayerList;
    private Player own;


    @FXML
    public void initialize(){
        PlayerList = new ArrayList<>();
        own = new Player();
        PlayerList.add(own);
        //Initialize character image
        own.getCharacter().relocate(10, 10);
        mainPane.getChildren().add(own.getCharacter());
        mainPane.requestFocus();
        mainPane.setFocusTraversable(true);
    }

    @FXML
    private void keyPressedHandler(KeyEvent event){
        switch(event.getCode()){
            case UP: own.getCharacter().setY(own.getCharacter().getY()-1); break;
            case DOWN: own.getCharacter().setY(own.getCharacter().getY()+1); break;
            case LEFT: own.getCharacter().setX(own.getCharacter().getX()-1); break;
            case RIGHT: own.getCharacter().setX(own.getCharacter().getX()+1); break;
        }
    }

}
