package com.realbadapps.pendulum.server;

import com.realbadapps.pendulum.server.game.ConfigFileParser;
import com.realbadapps.pendulum.server.game.GameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        if (args.length != 1)
            throw new IllegalArgumentException("One argument required: the game configuration's file location");

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        GameService gameService = context.getBean(GameService.class);
        gameService.gameLoaded(ConfigFileParser.parse(args[0]));    //TODO use Spring values
    }
}