package com.realbadapps.pendulum.server.game;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ConfigFileParser {
    public static GameConfiguration parse(String configLocation) {
        GameConfiguration gameConfiguration;

        File configFile = new File(configLocation);
        if (configFile.isFile()) {
            try {
                gameConfiguration = new Gson().fromJson(new FileReader(configFile), GameConfiguration.class);
            }
            catch (FileNotFoundException e) {
                throw new IllegalArgumentException("Bad config file location");
            }
        }
        else
            throw new IllegalArgumentException("Bad config file location");

        return gameConfiguration;
    }
}