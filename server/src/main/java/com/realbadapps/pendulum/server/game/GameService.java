package com.realbadapps.pendulum.server.game;

import org.springframework.stereotype.Service;

@Service
public class GameService {
    private GameConfiguration gameConfiguration;

    public GameService() {
        this.gameConfiguration = null;
    }

    public void gameLoaded(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    /*
        Getters & setters
     */

    public boolean isLoaded() {
        return gameConfiguration != null;
    }

    public GameConfiguration getGameConfiguration() {
        return gameConfiguration;
    }
}