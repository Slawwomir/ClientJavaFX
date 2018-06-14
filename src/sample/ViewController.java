package sample;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    @FXML
    private TextArea chatBox;
    @FXML
    private Button sendButton;
    @FXML
    private TextField inputText;

    private ImageView character;
    private Player own;
    private GameController gameController;
    private Board board;
    private AnimationTimer timer;
    private boolean focusRequired;

    private final Object lock = new Object();

    private final static double delta = 0.02;



    @FXML
    public void initialize() throws UnknownHostException {
        createBoard();
        focusRequired = false;
        gameController = new GameController(InetAddress.getByName("localhost"), 9191, board);
        new Thread(gameController).start();
        mainPane.getChildren().addAll(gameController.getOwn().getCharacter(), gameController.getFriend().getCharacter());
        gameController.getOwn().getCharacter().toFront();
        gameController.getFriend().getCharacter().toFront();
        mainPane.requestFocus();
        mainPane.setFocusTraversable(true);
        chatBox.setEditable(false);

        timer = new AnimationTimer()
        {
            private long last = 0;
            @Override
            public void handle(long now)
            {
                if(now - last > 10_000_000) {

                    synchronized (lock) {
                        if (focusRequired && !mainPane.isFocused())
                            mainPane.requestFocus();
                        lock.notify();
                    }

                    gameController.move(delta);
                    gameController.getOwn().updateFromGUI();
                    gameController.getFriend().updateFromGUI();

                    if(gameController.getOwn().getX() >= board.getBoardSize()*board.getElementSize()/2)
                        mainPane.setTranslateX(-board.getBoardSize()*board.getElementSize()/2);
                    else
                        mainPane.setTranslateX(0);

                    if(gameController.getOwn().getY() >= board.getBoardSize()*board.getElementSize()/2)
                        mainPane.setTranslateY(-board.getBoardSize()*board.getElementSize()/2);
                    else
                        mainPane.setTranslateY(0);

                    String message = gameController.getMessage();
                    gameController.clearMessage();
                    if(message != null && !message.isEmpty()){
                        addMessageChatBox(message);
                    }
                    //if(board.isInitialized())
                    //    board.refreshWater(0.1);
                    last = now;
                }
            }
        };

        timer.start();

        mainPane.requestFocus();
        mainPane.setFocusTraversable(true);
    }

    @FXML
    private void keyPressedHandler(KeyEvent event){
        gameController.keyPressed(event);
    }

    @FXML
    private void keyReleasedHandler(KeyEvent event){
        gameController.keyReleased(event);
    }

    @FXML
    private void clickMainPaneHandler(MouseEvent event){
        synchronized (lock) {
            focusRequired = true;
        }
    }

    @FXML
    private void clickChatBoxInput(MouseEvent event){
        synchronized (lock) {
            focusRequired = false;
            inputText.requestFocus();
        }
    }

    @FXML
    private void sendButtonClick(Event event){
        gameController.sendMessage(inputText.getText());
        chatBox.appendText("Me: " + inputText.getText() + "\n");
        inputText.clear();
    }

    @FXML
    private void textFieldKeyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            gameController.sendMessage(inputText.getText());
            chatBox.appendText("Me: " + inputText.getText() + "\n");
            inputText.clear();
        }
    }

    public void addMessageChatBox(String message){
        chatBox.appendText("Friend: " + message + "\n");
    }

    private void createBoard(){
        board = new Board("map.txt");
        mainPane.getChildren().addAll(board.getElements().stream().
                map(BoardElement::getImageView).collect(Collectors.toList()));

        mainPane.getChildren().addAll(board.getAdditional());
    }



}
