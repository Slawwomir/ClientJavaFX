package sample;

import javafx.animation.AnimationTimer;
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
    private AnimationTimer timer;
    private final static double delta = 0.015;

    @FXML
    public void initialize() throws UnknownHostException {
        createBoard();
        gameController = new GameController(InetAddress.getByName("localhost"), 9191, board);
        new Thread(gameController).start();
        mainPane.getChildren().addAll(gameController.getOwn().getCharacter(), gameController.getFriend().getCharacter());
        gameController.getOwn().getCharacter().toFront();
        gameController.getFriend().getCharacter().toFront();
        mainPane.requestFocus();
        mainPane.setFocusTraversable(true);

        timer = new AnimationTimer()
        {
            private long last = 0;
            @Override
            public void handle(long now)
            {
                if(now - last > 10_000_000) {
                    gameController.move(delta);
                    gameController.getOwn().updateFromGUI();
                    gameController.getFriend().updateFromGUI();
                    if(board.isInitialized())
                        board.refreshWater(0.1);
                    last = now;
                }
            }
        };

        timer.start();
    }

    @FXML
    private void keyPressedHandler(KeyEvent event){
        //gameController.move(event);
        gameController.keyPressed(event);
    }

    @FXML
    private void keyReleasedHandler(KeyEvent event){
        gameController.keyReleased(event);
    }

    private void createBoard(){
        board = new Board("map.txt");
        mainPane.getChildren().addAll(board.getElements().stream().
                map(BoardElement::getImageView).collect(Collectors.toList()));

        mainPane.getChildren().addAll(board.getAdditional());
    }

}
