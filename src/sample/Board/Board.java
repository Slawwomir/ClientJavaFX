package sample.Board;

import sample.Player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    private int elementSize;
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

    public List<BoardElement> getElementsCoveredByPlayer(Player player){
        double r = player.getSize()/2;
        double xMiddle = player.getX() + r;
        double yMiddle = player.getY() + r;

        if(Math.abs((xMiddle - r) % player.getSize()) < 1e-1 &&  Math.abs((yMiddle - r) % player.getSize()) < 1e-1){
            // player is inside one element
            return Collections.singletonList(elements[(int) yMiddle / elementSize][(int) xMiddle / elementSize]);
        }
        else if (Math.abs((yMiddle - r )% player.getSize()) < 1e-1){
            // player covers two elements in x axis
            return Arrays.asList(elements[(int)yMiddle/elementSize][(int)(xMiddle + r)/elementSize],
                    elements[(int)yMiddle/elementSize][(int)(xMiddle - r)/elementSize]);
        }
        else if (Math.abs((xMiddle - r) % player.getSize()) < 1e-1) {
            // player covers two elements in y axis
            return Arrays.asList(elements[(int)(yMiddle+r)/elementSize][(int)xMiddle/elementSize],
                    elements[(int)(yMiddle - r)/elementSize][(int)xMiddle/elementSize]);
        }
        else {
            // player covers maximum number of elements, ie 4 elements around
            return Arrays.asList(elements[(int)(yMiddle + r)/elementSize][(int)(xMiddle + r)/elementSize],
                    elements[(int)(yMiddle + r)/elementSize][(int)(xMiddle - r)/elementSize],
                    elements[(int)(yMiddle - r)/elementSize][(int)(xMiddle + r)/elementSize],
                    elements[(int)(yMiddle - r)/elementSize][(int)(xMiddle - r)/elementSize]);
        }
    }
}
