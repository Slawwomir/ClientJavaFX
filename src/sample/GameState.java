package sample;

import sample.Board.BoardElementProperties;
import sample.Board.BoardProperties;
import sample.Player.PlayerProperties;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    private List<PlayerProperties> players;
    private BoardProperties boardProperties;
    private int elementSize = 35;
    private boolean changesInElements;
    private List<Point> changed;
    private double waterLevel;
    private List<String> messages;

    public GameState(){
        players = new ArrayList<>(2);
        messages = new ArrayList<>();
        messages.add("");
        messages.add("");
        boardProperties = new BoardProperties();
        changed = new ArrayList<>();
        waterLevel = 0;
        int id = 0;
        for(Point pos : boardProperties.getPlayersPositions()){
            PlayerProperties p = new PlayerProperties(pos.y * elementSize, pos.x*elementSize, id);
            id++;
            players.add(p);
        }
    }

    public GameState(GameState gameState){
        this.changesInElements = gameState.changesInElements;
        this.players = new ArrayList<>();
        this.changed = new ArrayList<>(gameState.changed);
        this.messages = new ArrayList<>(gameState.messages);
        for(PlayerProperties pp : gameState.getPlayersProperties())
            players.add(new PlayerProperties(pp.x, pp.y, pp.id));

        this.waterLevel = gameState.waterLevel;
        this.boardProperties = new BoardProperties(gameState.boardProperties.getElements());
    }

    public synchronized List<PlayerProperties> getPlayersProperties() {
        return players;
    }

    public synchronized void updateChanged(GameState gameState){
        BoardElementProperties[][] elementsOld = boardProperties.getElements();
        BoardElementProperties[][] elementsNew = gameState.boardProperties.getElements();
        for(Point p: gameState.getChanged()){
            elementsOld[p.y][p.x]= new BoardElementProperties(elementsNew[p.y][p.x]);
        }
    }

    public synchronized void update(GameState gameState){
        List<PlayerProperties> p = gameState.getPlayersProperties();
        waterLevel = gameState.waterLevel;
        players.get(0).update(p.get(0));
        players.get(1).update(p.get(1));
    }

    public synchronized void updateOne(GameState gameState, int id){
        players.get(id).update(gameState.getPlayersProperties().get(id));
    }

    public synchronized BoardProperties getBoardProperties() {
        return boardProperties;
    }

    public synchronized void setBoardProperties(BoardProperties boardProperties){
        this.boardProperties = boardProperties;
    }

    public boolean isChangesInElements() {
        return changesInElements;
    }

    public void setChangesInElements(boolean changesInElements) {
        if(!changesInElements)
            changed.clear();

        this.changesInElements = changesInElements;
    }

    public synchronized List<Point> getChanged() {
        return changed;
    }

    public synchronized void setChanged(List<Point> changed) {
        this.changed.addAll(changed);
    }

    public void increaseWaterLevel(double addition){
        waterLevel+=addition;
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    public String getMessage(int identity) {
        return messages.get(identity);
    }

    public synchronized void setMessage(String message, int identity) {
        messages.set(identity, message);
    }
}
