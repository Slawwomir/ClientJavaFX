package sample;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import sample.Player.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class ViewController {

    @FXML
    private Pane mainPane;
    private ImageView character;
    private Player own;
    private GameController gameController;


    @FXML
    public void initialize() throws UnknownHostException {
        gameController = new GameController(InetAddress.getByName("localhost"), 9090);
        new Thread(gameController).start();
        mainPane.getChildren().addAll(gameController.getOwn().getCharacter(), gameController.getFriend().getCharacter());
        mainPane.requestFocus();
        mainPane.setFocusTraversable(true);
    }

    @FXML
    private void keyPressedHandler(KeyEvent event){
        gameController.move(event);
    }

}
