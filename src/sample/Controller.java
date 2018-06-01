package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class Controller {

    @FXML
    private Pane mainPane;

    private ImageView character;

    @FXML
    public void initialize(){
        //It works!

        //Initialize character image
        character = new ImageView(new Image("http://icons.iconarchive.com/icons/kidaubis-design/cool-heroes/128/Ironman-icon.png"));
        character.relocate(10, 10);
        mainPane.getChildren().add(character);
        mainPane.requestFocus();
        mainPane.setFocusTraversable(true);
    }

    @FXML
    private void keyPressedHandler(KeyEvent event){
        switch(event.getCode()){
            case UP: character.setY(character.getY()-1); break;
            case DOWN: character.setY(character.getY()+1); break;
            default: System.out.println("XD");
        }
    }

}
