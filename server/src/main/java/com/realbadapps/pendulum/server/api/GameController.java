package com.realbadapps.pendulum.server.api;

import com.realbadapps.pendulum.server.game.GameConfiguration;
import com.realbadapps.pendulum.server.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    @Autowired
    private GameService gameService;

    @RequestMapping("/")
    public Status status() {
        return new Status(gameService.isLoaded());
    }

    @RequestMapping("/configuration/")
    public GameConfiguration getGameConfiguration() {
        if (gameService.isLoaded())
            return gameService.getGameConfiguration();
        else
            throw new ResourceNotFoundException();
    }

    private class Status {
        private boolean loaded;

        public Status() {
            this(false);
        }

        public Status(boolean loaded) {
            this.loaded = loaded;
        }

        /*
            Getters & setters
         */

        public boolean isLoaded() {
            return loaded;
        }

        public void setLoaded(boolean loaded) {
            this.loaded = loaded;
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private class ResourceNotFoundException extends RuntimeException {}
}