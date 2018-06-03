package sample;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import sample.Board.Board;
import sample.Board.BoardElement;
import sample.Player.Player;
import sample.Player.PlayerProperties;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class GameController implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Player friend;
    private Player own;
    private Board board;

    public GameController(InetAddress host, int port, Board board){
        this.board = board;
        own = new Player();
        friend = new Player();
        try {
            socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            outputStream.writeObject(own.getProperties());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            try {
                friend.update((PlayerProperties) inputStream.readObject());
                outputStream.writeObject(own.getProperties());
            } catch (ClassNotFoundException | IOException e){
                e.printStackTrace();
                break;
            }
        }
    }

    public void move(KeyEvent event){
        double prevX = own.getX();
        double prevY = own.getY();
        makeMove(event.getCode());
        List<BoardElement> elementsCoveredByPlayer = board.getElementsCoveredByPlayer(own);
        for (BoardElement element : elementsCoveredByPlayer) {
            if(!element.isPermeable()){
                //undo move
                //makeMove(new KeyCode((event.getCode().getCode() - 23) % 4 + 25, "Undo"));
                own.setX(prevX);
                own.setY(prevY);
                break;
            }
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
