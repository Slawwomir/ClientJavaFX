package sample.Board;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoardProperties implements Serializable {
    private int elementSize;
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
                        case 'w':   // wall
                            elements[i][j] = new BoardElementProperties(false, false);
                            break;
                        case 'p':
                            playersPositions.add(new Point(i, j));
                        case 'o':
                        case 'f':   // floor
                            elements[i][j] = new BoardElementProperties(true, false);
                            break;
                        case 'c': {  //connected with
                            elements[i][j] = new BoardElementProperties(true, false);
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

    public List<Point> getPlayersPositions() {
        return playersPositions;
    }
}

