package com.realbadapps.pendulum.server.media;

public class SimpleIllumination extends Illumination {
    private LightState state;

    public SimpleIllumination() {}

//    private void checkFlash() {
//        state.makeFlash(getFrames() % 2 == 1);
//    }

    /*
        Getters & setters
     */

    public LightState getState() {
        return state;
    }

    public void setState(LightState state) {
        this.state = state;
    }
}