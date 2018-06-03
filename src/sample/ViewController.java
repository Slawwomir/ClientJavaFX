package sample;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import sample.Board.Board;
import sample.Board.BoardElement;
import sample.Player.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;


public class ViewController {

    @FXML
    private Pane mainPane;
    private ImageView character;
    private Player own;
    private GameController gameController;
    private Board board;

    @FXML
    public void initialize() throws UnknownHostException {
        createBoard();

        gameController = new GameController(InetAddress.getByName("localhost"), 9090, board);
        new Thread(gameController).start();
        mainPane.getChildren().addAll(gameController.getOwn().getCharacter(), gameController.getFriend().getCharacter());
        mainPane.requestFocus();
        mainPane.setFocusTraversable(true);
    }

    @FXML
    private void keyPressedHandler(KeyEvent event){
        gameController.move(event);
    }

    private void createBoard(){
        board = new Board("map.txt");
        mainPane.getChildren().addAll(board.getElements().stream().
                map(BoardElement::getImageView).collect(Collectors.toList()));
    }

}
