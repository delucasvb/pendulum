package com.realbadapps.pendulum.server.api;

import com.realbadapps.pendulum.server.game.Game;
import com.realbadapps.pendulum.server.game.GameService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class GameController {
    private static Logger logger = Logger.getLogger(GameController.class);

    @Autowired
    private GameService gameService;

    @RequestMapping("/")
    public Status status() {
        return new Status(gameService.isStarted());
    }

    @RequestMapping(value = "/start/", method = RequestMethod.POST)
    public void start() {
        if (gameService.isLoaded())
            gameService.setStarted(true);
        else
            throw new ResourceNotFoundException();
    }

    @RequestMapping("/game/")
    public Game getGameConfiguration() {
        if (gameService.isStarted())
            return gameService.getCurrentGame();
        else
            throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "/game/next/", method = RequestMethod.POST)
    public void nextGame() {
        if (gameService.isLoaded())
            gameService.nextGame();
        else
            throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "/{team}/answer/", method = RequestMethod.POST)
    public Answer addAnswer(
            @RequestBody Answer answer,
            @PathVariable(value = "team") String team) {
        logger.info(String.format("New answer '%s' from '%s'", answer.getContent(), team));
        return gameService.addAnswer(answer, team);
    }

    @RequestMapping(value = "/answer/")
    public Map<String, LinkedList<Answer>> getAnswers() {
        return gameService.getAnswerMap();
    }

    private class Status {
        private boolean ready;

        public Status() {
            this(false);
        }

        public Status(boolean ready) {
            this.ready = ready;
        }

        /*
            Getters & setters
         */

        public boolean isReady() {
            return ready;
        }

        public void setReady(boolean ready) {
            this.ready = ready;
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private class ResourceNotFoundException extends RuntimeException {}
}