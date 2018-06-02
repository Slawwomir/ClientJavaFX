package sample;

import javafx.scene.input.KeyEvent;
import sample.Player.Player;
import sample.Player.PlayerProperties;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class GameController implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Player friend;
    private Player own;

    public GameController(InetAddress host, int port){
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
                //PlayerProperties test = (PlayerProperties) inputStream.readObject();
                //friend.update(test);
                outputStream.writeObject(own.getProperties());
            } catch (ClassNotFoundException | IOException e){
                e.printStackTrace();
                break;
            }
        }
    }

    public void move(KeyEvent event){
        switch(event.getCode()){

            case UP: own.getCharacter().setY(own.getCharacter().getY()-1); break;
            case DOWN: own.getCharacter().setY(own.getCharacter().getY()+1); break;
            case LEFT: own.getCharacter().setX(own.getCharacter().getX()-1); break;
            case RIGHT: own.getCharacter().setX(own.getCharacter().getX()+1); break;

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
