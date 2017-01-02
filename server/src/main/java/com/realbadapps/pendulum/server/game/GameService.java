package com.realbadapps.pendulum.server.game;

import com.realbadapps.pendulum.server.api.Answer;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class GameService {
    private GameConfiguration gameConfiguration;
    private int currentGamePosition;
    private Map<String, LinkedList<Answer>> answerMap;

    public GameService() {
        this.gameConfiguration = null;
        this.currentGamePosition = -1;
        this.answerMap = new TreeMap<>();
    }

    public void gameLoaded(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public Answer addAnswer(Answer answer, String key) {
        LinkedList<Answer> answerList = answerMap.computeIfAbsent(key, k -> new LinkedList<>());
        answerList.add(answer);
        return answer;
    }

    public void nextGame() {
        currentGamePosition = (currentGamePosition + 1) % gameConfiguration.getGames().length;
    }

    /*
        Getters & setters
     */

    public boolean isLoaded() {
        return gameConfiguration != null;
    }

    public Game getCurrentGame() {
        if (currentGamePosition == -1 && gameConfiguration != null && gameConfiguration.getGames().length > 0)
            currentGamePosition = 0;
        else if (currentGamePosition == -1)
            throw new IllegalStateException("No games available");

        return gameConfiguration.getGames()[currentGamePosition];
    }

    public Map<String, LinkedList<Answer>> getAnswerMap() {
        return answerMap;
    }

    public boolean isStarted() {
        return getCurrentGame().isStarted();
    }

    public void setStarted(boolean started) {
        getCurrentGame().setStarted(started);
    }
}