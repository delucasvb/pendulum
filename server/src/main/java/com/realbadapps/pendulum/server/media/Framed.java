package com.realbadapps.pendulum.server.media;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Framed {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int frames;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long time;

    public Framed() {
        //
    }

    /*
        Getters & setters
     */

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}