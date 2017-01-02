package com.realbadapps.pendulum.server.game.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realbadapps.pendulum.server.game.Game;
import com.realbadapps.pendulum.server.game.GameConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFileParser {
    public static GameConfiguration parse(String configLocation) {
        GameConfiguration gameConfiguration;

        File configFile = new File(configLocation);
        if (configFile.isFile()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                gameConfiguration = objectMapper.readValue(configFile, GameConfiguration.class);
                for (Game game : gameConfiguration.getGames())
                    game.convertFramesToTime();
            }
            catch (IOException e) {
                throw new IllegalArgumentException("Bad config file", e);
            }
        }
        else
            throw new IllegalArgumentException("Bad config file location");

        return gameConfiguration;
    }
}