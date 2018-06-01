package sample.Player;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Player implements Runnable {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ImageView character;

    public Player(){
        character = new ImageView(new Image("http://icons.iconarchive.com/icons/custom-icon-design/flatastic-6/72/Circle-icon.png"));

        try {
            socket = new Socket("localhost", 9090);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
            try(Socket s = new Socket("localhost", 9090);
                Scanner in = new Scanner(s.getInputStream(), "UTF-8")){

                while(in.hasNextLine()){
                    String line = in.nextLine();
                }

            }catch (IOException e){
                e.printStackTrace();
            }
    }

    public ImageView getCharacter() {
        return character;
    }
}
