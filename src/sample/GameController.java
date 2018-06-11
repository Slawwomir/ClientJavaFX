package sample;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import sample.Board.Board;
import sample.Board.BoardElement;
import sample.Board.Usable;
import sample.Player.Player;
import sample.Player.PlayerProperties;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class GameController implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Player friend;
    private Player own;
    private Board board;
    private GameState gameState;

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
                /*
                    PlayerProperties f = (PlayerProperties) inputStream.readObject();
                    friend.update(f);
                    outputStream.writeObject(own.getProperties());
                    PlayerProperties me = own.getProperties();
                    //System.out.println(". wysylam " + me.x + "." + me.y + " | Odbieram " + f.x + "." + f.y);
                */
                GameState state = (GameState) inputStream.readObject();
                gameState.update(state);
                gameState.getPlayersProperties().get(own.getId()).update(own.getProperties());
                friend.update(gameState.getPlayersProperties().get(friend.getId()));
                GameState out = new GameState(gameState);
                outputStream.writeObject(out);

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
                if (element instanceof Usable && usePressed)
                    ((Usable) element).useDirectly();
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
