package sample.Board;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import sample.Player.Player;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    private int elementSize;
    private BoardElement[][] elements;
    private List<Rectangle> additional;
    private int boardSize;
    private boolean initialized;
    private boolean elementChanged = false;
    private List<Tuple<ConnectedBoardElement, Point>> connections;
    private List<Point> changedElements;
    private boolean underWater = false;
    public Board(String fileName){
        elementSize = 35;
        additional = new ArrayList<>();
        connections = new ArrayList<>();
        changedElements = new ArrayList<>();
        initialized = false;
        try(BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            String line = bf.readLine();
            boardSize = line.length();
            elements = new BoardElement[line.length()][line.length()];
            int i = 0;
            while(line != null) {
                for(int k = 0, j = 0; k < line.length(); k++)
                {
                    char c = line.charAt(k);
                    switch(c) {
                        case 'w':   // wall
                            elements[i][j] = new BoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_064.png", false, 'w'); break;
                        case 't': //element with timer
                            elements[i][j] = new ConnectedBoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_026.png",
                                    "file:platformer-pack-medieval/PNG/medievalTile_029.png",
                                    true, false, true, true, 'd');
                            elements[i][j].setDelayTime(0);
                            break;
                        case 'x':
                            Rectangle water = new Rectangle(elementSize, (i+1)*elementSize, (boardSize-4)/2*elementSize, elementSize);
                            water.setFill(javafx.scene.paint.Color.rgb(0, 191, 255, 0.3));
                            additional.add(water);
                        case 'f':   // floor
                            elements[i][j] = new BoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_263.png", true, 'f'); break;
                        case 'c':{  //connected with
                            String xe = line.substring(k+2, k+line.substring(k+1).indexOf(']') + 1);
                            String[] st = xe.split("\\|");
                            int y = Integer.parseInt(st[0]);
                            int x = Integer.parseInt(st[1]);
                            ConnectedBoardElement el = new ConnectedBoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_031.png",
                                    "file:platformer-pack-medieval/PNG/medievalTile_031.png",
                                    true, false, true, true, 'c');
                            elements[i][j] = el;
                            connections.add(new Tuple<>(el, new Point(x, y)));
                            k+= xe.length() + 2;
                        } break;

                        case 'o':{
                            elements[i][j] = new ConnectedBoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_102.png",
                                    "file:platformer-pack-medieval/PNG/medievalTile_101.png",
                                    true, false, true, false, 'o'); break;
                        }

                        case 'd': {
                            elements[i][j] = new ConnectedBoardElement(i, j, elementSize,
                                    "file:platformer-pack-medieval/PNG/medievalTile_222.png",
                                    "file:platformer-pack-medieval/PNG/medievalTile_263.png",
                                    false, false, false, false, 'd'); break;
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

    public BoardElement[][] getElementsArray(){
        return elements;
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

    public synchronized BoardProperties getBoardProperties(){
        BoardElementProperties[][] elementProperties = new BoardElementProperties[boardSize][boardSize];

        int m = 0;
        for(BoardElement element : getElements()){
            boolean used = false;
            if(element instanceof Usable){
                if(((ConnectedBoardElement) element).isUsed())
                    used = true;
            }
            elementProperties[(int)(element.getPosY()/elementSize)][(int)(element.getPosX()/elementSize)]
                    = new BoardElementProperties(element.isPermeable(), used, element.getDelayTime(), element.getSign());
        }

        BoardProperties properties = new BoardProperties(elementProperties);

        return properties;
    }

    public int getBoardSize(){
        return boardSize;
    }

    public double getElementSize(){
        return elementSize;
    }

    public boolean isInitialized(){
        return initialized;
    }

    public synchronized void setProperties(BoardProperties properties){
        initialized = true;
        BoardElementProperties[][] el = properties.getElements();
        for(int i = 0; i < boardSize; i++)
            for(int j = 0; j < boardSize; j++){
                if(elements[i][j] instanceof ConnectedBoardElement) {
                    if (!(el[i][j].isUsed() == ((ConnectedBoardElement) elements[i][j]).isUsed())) {
                        elements[i][j].setDelayTime(0);
                        ((ConnectedBoardElement) elements[i][j]).useDirectly();
                        elements[i][j].setDelayTime(el[i][j].getDelayTime());
                    }
                }
                if(elements[i][j].isPermeable() != el[i][j].isPermeable())
                    elements[i][j].setPermeable(el[i][j].isPermeable());
            }
    }

    public List<Rectangle> getAdditional() {
        return additional;
    }

    public synchronized void refreshWater(double value){
        Rectangle r = additional.get(0);

        Platform.runLater(() -> {
            r.setHeight(value);
            r.setY(elementSize * (elements.length/2 - 1) - value);
        });

        if(r.getY() > 0 && r.getY() < elementSize*(boardSize-1)) {

            for (BoardElement element : elements[(int) (r.getY() + elementSize / 2) / elementSize]){
                if(element.getSign() == 'd' && !((ConnectedBoardElement)element).isUsed()){
                    elementChanged = true;
                    ((Usable) element).use();
                    element.setPermeable(true);
                    changedElements.add(new Point((int)(element.getPosX()/elementSize),(int) (r.getY() + elementSize / 2) / elementSize));
                }
            }
        }
    }

    public synchronized void setElementChanged(boolean elementChanged){
        this.elementChanged = elementChanged;
    }

    public synchronized boolean isElementChanged() {
        return elementChanged;
    }

    public synchronized List<Point> getChangedElements() {
        return changedElements;
    }

    public synchronized void setChangedElements(List<Point> changedElements) {
        this.changedElements = changedElements;
    }

    public synchronized void clearChangedElements(){
        changedElements.clear();
    }
}

