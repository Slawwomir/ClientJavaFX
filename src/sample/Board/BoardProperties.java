package sample.Board;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoardProperties implements Serializable {
    private int elementSize = 35;
    private BoardElementProperties[][] elements;
    private List<Point> playersPositions;

    public BoardProperties() {
        playersPositions = new ArrayList<>();

        try (
                BufferedReader bf = new BufferedReader(new FileReader("map.txt")))
        {
            String line = bf.readLine();
            elements = new BoardElementProperties[line.length()][line.length()];
            int i = 0;
            while (line != null) {
                for (int k = 0, j = 0; k < line.length(); k++) {
                    char c = line.charAt(k);
                    switch (c) {
                        case 'd':
                        case 'w':   // wall
                            elements[i][j] = new BoardElementProperties(false, false, c);
                            break;
                        case 'p':
                            playersPositions.add(new Point(i, j));
                            elements[i][j] = new BoardElementProperties(true, false, c);
                            break;
                        case 't':
                            elements[i][j] = new BoardElementProperties(true, false, 0, c);
                            break;
                        case 'x':
                            //waterLevel = (i+1)*elementSize;
                        case 'o':
                        case 'f':   // floor
                            elements[i][j] = new BoardElementProperties(true, false, c);
                            break;
                        case 'c': {  //connected with
                            elements[i][j] = new BoardElementProperties(true, false, c);
                            String xe = line.substring(k+2, k+line.substring(k+1).indexOf(']') + 1);
                            k += xe.length() + 2;
                        }
                        break;
                    }
                    j++;
                }
                i++;
                line = bf.readLine();
            }
        } catch (IOException e)

        {
            e.printStackTrace();
        }
    }

    public BoardProperties(BoardElementProperties[][] elementProperties){
        elements = new BoardElementProperties[elementProperties.length][elementProperties.length];
        for(int i = 0; i < elementProperties.length; i++){
            for(int j = 0; j < elementProperties[i].length; j++){
                elements[i][j] = new BoardElementProperties(elementProperties[i][j]);
            }
        }
    }

    public List<Point> getPlayersPositions() {
        return playersPositions;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BoardProperties){
            BoardProperties prop = (BoardProperties) obj;
            for(int i = 0; i < prop.elements.length; i++){
                for(int j = 0; j < prop.elements[i].length; j++){
                    if(!(elements[i][j].isPermeable() && prop.elements[i][j].isPermeable() &&
                            elements[i][j].isUsed() && prop.elements[i][j].isUsed()))
                        return false;
                }
            }
            return true;
        }
        else return false;
    }

    public BoardElementProperties[][] getElements() {
        return elements;
    }
}

