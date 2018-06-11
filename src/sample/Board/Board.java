package sample.Board;

import sample.Player.Player;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    private int elementSize;
    private BoardElement[][] elements;

    public Board(String fileName){
        elementSize = 35;

        List<Tuple<ConnectedBoardElement, Point>> connections = new ArrayList<>();

        try(BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            String line = bf.readLine();
            elements = new BoardElement[line.length()][line.length()];
            int i = 0;
            while(line != null) {
                for(int k = 0, j = 0; k < line.length(); k++)
                {
                    char c = line.charAt(k);
                    switch(c) {
                        case 'w':   // wall
                            elements[i][j] = new BoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_064.png", false); break;
                        case 'f':   // floor
                            elements[i][j] = new BoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_263.png", true); break;
                        case 'c':{  //connected with
                            String xe = line.substring(k+2, k+line.substring(k+1).indexOf(']') + 1);
                            String[] st = xe.split("\\|");
                            int y = Integer.parseInt(st[0]);
                            int x = Integer.parseInt(st[1]);
                            ConnectedBoardElement el = new ConnectedBoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_031.png",
                                    "file:platformer-pack-medieval/PNG/medievalTile_031.png",
                                    true, false, true, true);
                            elements[i][j] = el;
                            connections.add(new Tuple<>(el, new Point(x, y)));
                            k+= xe.length() + 2;
                        } break;

                        case 'o':{
                            elements[i][j] = new ConnectedBoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_101.png",
                                    "file:platformer-pack-medieval/PNG/medievalTile_102.png",
                                    true, false, true, false); break;
                        }

                    }
                    j++;
                }
                i++;
                line = bf.readLine();
            }

            for(Tuple<ConnectedBoardElement, Point> pair : connections){
                pair.x.addConnection(elements[pair.y.y][pair.y.x]);
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

