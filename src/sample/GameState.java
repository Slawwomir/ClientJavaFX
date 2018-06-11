package sample;

import sample.Board.Board;
import sample.Board.BoardProperties;
import sample.Player.Player;
import sample.Player.PlayerProperties;

import java.awt.*;
import java.io.Serializable;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    private List<PlayerProperties> players;
    private BoardProperties boardProperties;
    private int elementSize = 35;

    public GameState(){
        players = new ArrayList<>(2);
    }

    public GameState(GameState gameState){
        this.players = new ArrayList<>();
        for(PlayerProperties pp : gameState.getPlayersProperties())
            players.add(new PlayerProperties(pp.x, pp.y, pp.id));

        this.boardProperties = gameState.boardProperties;
    }

    public List<PlayerProperties> getPlayersProperties() {
        return players;
    }

    public void update(GameState gameState){
        List<PlayerProperties> p = gameState.getPlayersProperties();
        players.get(0).update(p.get(0));
        players.get(1).update(p.get(1));
    }

    public void updateOne(GameState gameState, int id){
        List<PlayerProperties> p = gameState.getPlayersProperties();
        players.get(id).update(p.get(id));
    }
}
