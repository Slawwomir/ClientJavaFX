package sample;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import sample.Board.*;
import sample.Player.Player;
import sample.Player.PlayerProperties;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class GameController implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Player friend;
    private Player own;
    private Board board;
    private GameState gameState;
    private Point changed;

    private boolean connected;
    /*
    private boolean upReleased;
    private boolean downReleased;
    private boolean rightReleased;
    private boolean leftReleased;
    */

    private boolean upPressed;
    private boolean downPressed;
    private boolean rightPressed;
    private boolean leftPressed;
    private boolean usePressed;
    private double speed;

    public GameController(InetAddress host, int port, Board board){
        this.board = board;
        speed = 100;
        own = new Player();
        friend = new Player();
        try {
            socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            connected = true;
        }catch (IOException e){
            connected = false;
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            PlayerProperties initialMe = (PlayerProperties) inputStream.readObject();
            own.update(initialMe);
            PlayerProperties initialFriend = (PlayerProperties) inputStream.readObject();
            friend.update(initialFriend);
            gameState = (GameState) inputStream.readObject();
            board.setProperties(gameState.getBoardProperties());
            //outputStream.writeObject(own.getProperties());
            connected = true;
        } catch (IOException | ClassNotFoundException e) {
            connected = false;
            e.printStackTrace();
        }

        double delta;
        double actualTime = System.currentTimeMillis();

        while(true){
            if(connected)
                try {

                    GameState out = new GameState(gameState);
                    outputStream.writeObject(out);
                    gameState.setChangesInElements(false);
                    GameState state = (GameState) inputStream.readObject();
                    gameState.update(state);
                    gameState.getPlayersProperties().get(own.getId()).update(own.getProperties());
                ////////////////////////////////////////
                    BoardProperties myProperties = board.getBoardProperties();
                    BoardProperties gameProperties = state.getBoardProperties();
                    boolean ownChange = false;
                    boolean propEquals = myProperties.equals(gameProperties);
                    if(!propEquals && own.didAction()){
                        //wyślij moje na serwer
                        ownChange = true;
                        gameState.setBoardProperties(myProperties);
                        gameState.setChangesInElements(true);
                        BoardElement us = board.getElementsArray()[changed.y][changed.x];
                        if(us instanceof ConnectedBoardElement) {
                            List<BoardElement> connection = ((ConnectedBoardElement) us).getConnectedWith();
                            List<Point> points = new ArrayList<>();
                            for(BoardElement c : connection)
                                points.add(new Point((int)(c.getPosX()/board.getElementSize()), ((int)(c.getPosY()/board.getElementSize()))));
                            gameState.setChanged(points);
                        }

                        gameState.setChanged(Collections.singletonList(changed));
                        own.doAction(false);
                    }

                    if (!propEquals && board.isElementChanged()){
                        ownChange = true;
                        gameState.setBoardProperties(myProperties);
                        gameState.setChanged(board.getChangedElements());
                        gameState.setChangesInElements(true);
                        board.clearChangedElements();
                        board.setElementChanged(false);
                    }

                    if (!propEquals && !ownChange){
                        //zapisz
                        board.setProperties(gameProperties);
                        gameState.setBoardProperties(gameProperties);
                    }
                //////////////////////////////////
                    friend.update(gameState.getPlayersProperties().get(friend.getId()));

                } catch (ClassNotFoundException | IOException e){
                    e.printStackTrace();
                    break;
                }
        }
    }

    public void move(double delta){
        double prevX = own.getX();
        double prevY = own.getY();

        if(upPressed)
            own.setY(own.getY() - delta*speed);
        if(downPressed)
            own.setY(own.getY() + delta*speed);
        if(rightPressed)
            own.setX(own.getX() + delta*speed);
        if(leftPressed)
            own.setX(own.getX() - delta*speed);

        List<BoardElement> elementsCoveredByPlayer = board.getElementsCoveredByPlayer(own);
        for (BoardElement element : elementsCoveredByPlayer) {
            if(!element.isPermeable()){
                own.setX(prevX);
                own.setY(prevY);
                break;
            }
            //if(element instanceof Usable && usePressed)
            //    ((Usable) element).use();
        }
    }

    public void keyPressed(KeyEvent event){
        switch(event.getCode()){
            case UP: upPressed = true; break;
            case DOWN: downPressed = true; break;
            case LEFT: leftPressed = true; break;
            case RIGHT: rightPressed = true; break;
            case E: usePressed = true;
            List<BoardElement> elementsCoveredByPlayer = board.getElementsCoveredByPlayer(own);
            for (BoardElement element : elementsCoveredByPlayer) {
                if (element instanceof Usable && usePressed) {
                    changed = new Point((int)(element.getPosX()/board.getElementSize()),
                            (int)(element.getPosY()/board.getElementSize()));
                    ((Usable) element).useDirectly();
                    own.doAction(true);
                }
            }
            break;
        }
    }

    public void keyReleased(KeyEvent event){
        switch(event.getCode()){
            case UP: upPressed = false; break;
            case DOWN: downPressed = false; break;
            case LEFT: leftPressed = false; break;
            case RIGHT: rightPressed = false; break;
            case E: usePressed = false; break;
        }
    }

    public void makeMove(KeyCode code){
        switch(code){
            case UP: own.setY(own.getY()-1); break;
            case DOWN: own.setY(own.getY()+1); break;
            case LEFT: own.setX(own.getX()-1); break;
            case RIGHT: own.setX(own.getX()+1); break;
        }
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public Player getFriend() {
        return friend;
    }

    public Player getOwn() {
        return own;
    }
}
