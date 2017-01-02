package com.realbadapps.pendulum.server.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = PictureFlash.TYPE, value = PictureFlash.class),
        @JsonSubTypes.Type(name = DealOrNoDeal.TYPE, value = DealOrNoDeal.class),
        @JsonSubTypes.Type(name = Intro.TYPE, value = Intro.class)})
public abstract class Game {
    private String type;

    @JsonIgnore
    private boolean started;

    public Game() {
        this(null);
    }

    public Game(String type) {
        this.type = type;
        this.started = false;
    }

    public abstract void convertFramesToTime();

    /*
        Getters & setters
     */

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}