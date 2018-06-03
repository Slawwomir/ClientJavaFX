package sample.Board;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    private int elementSize;
    //private List<BoardElement> elements;
    private BoardElement[][] elements;

    public Board(String fileName){
        elementSize = 35;

        try(BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            String line = bf.readLine();
            elements = new BoardElement[line.length()][line.length()];
            int i = 0, j = 0;
            while(line != null) {
                for(char x : line.toCharArray()){
                    switch(x) {
                        case 'w':   // wall
                            elements[i][j] = new BoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_064.png", false); break;
                        case 'f':   // floor
                            elements[i][j] = new BoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_263.png", true); break;
                    }
                    j++;
                }
                j = 0;
                i++;
                line = bf.readLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<BoardElement> getElements() {
        return Arrays.stream(elements).flatMap(Arrays::stream).collect(Collectors.toList());
    }
}
