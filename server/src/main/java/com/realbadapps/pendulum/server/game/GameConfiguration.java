package com.realbadapps.pendulum.server.game;

public class GameConfiguration {
    private String audio;
    private String[] images;

    public GameConfiguration() {}

    /*
        Getters & setters
     */

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}