package com.gradle.game.entities;

import com.gradle.game.GameManager;
import com.gradle.game.GameType;
import de.gurkenlabs.litiengine.entities.IEntityController;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    //configuration variables
    private static final float defaultVelocity = 100;

    private static ArrayList<Player> players = new ArrayList<Player>();
    private static boolean initialized = false;

    public static void init() {
        GameType gameType = GameManager.getCurrentGameType();
        if (gameType == GameType.SINGLEPLAYER) {
            players.add(new Player("hoodie"));
        } else {
            //TODO: implement multi-player loader
        }

        initialized = true;
    }

    public static Player getCurrent() {
        return get(0);
    }

    public static Player get(int playerNumber) {
        return initialized ? players.get(playerNumber) : null;
    }

    public static List<Player> getAll() {
        return players;
    }

    public static void addPlayer(String spriteSheetName, IEntityController controller) {
        Player player = new Player(spriteSheetName);
        player.addController(controller); //TODO: check if this works, or if there's a better implementation
        players.add(player);
    }

    public static void freezePlayers() {
        players.forEach(p -> {
            p.setVelocity(0);
        });
    }

    public static void unFreezePlayers() {
        players.forEach(p -> {
            p.setVelocity(defaultVelocity);
        });
    }
}