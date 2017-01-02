package com.realbadapps.pendulum.server.media;

public class Audio {
    private String location;
    private int bpm;

    public Audio() {}

    /*
        Getters & setters
     */

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }
}