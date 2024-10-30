package com.example.speedtyper;

import java.util.HashMap;
import java.util.Map;

public class GameSession {
    private String code;

    private Map<String, String> players = new HashMap<String, String>();

    // when game session created there's atleast one player
    public GameSession(String code, String player) {
        this.code = code;
        // when created the user has not entered their username yet
        this.players.put(player, "");
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
    public Map<String,String> getPlayers() {
        return players;
    }

    /**
     * This method will add the new playerID to the room if not exists, or it will add a new playerID, name pair
     * **/
    public void setPlayerName(String playerID, String name) {
        // update the name
        if(players.containsKey(playerID)){
            players.remove(playerID);
            players.put(playerID, name);
        }else{ // add new user
            players.put(playerID, name);
        }
    }

    /**
     * This method will remove a player from this room
     * **/
    public void removePlayer(String userID){
        players.remove(userID);
    }

    public boolean inRoom(String userID){
        return players.containsKey(userID);
    }
}
