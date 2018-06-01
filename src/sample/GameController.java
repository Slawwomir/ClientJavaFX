package sample;

import sample.Player.Player;

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
        try {
            socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                friend = (Player) inputStream.readObject();
            } catch (ClassNotFoundException | IOException e){
                e.printStackTrace();
                break;
            }
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
