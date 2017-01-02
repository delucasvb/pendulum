package com.realbadapps.pendulum.server.media;

public class MultiIllumination extends Illumination {
    private LightState[] states;

    public MultiIllumination() {}

//    private void checkFlash() {
//        if (getFrames() % 2 == 1)
//            Arrays.stream(states).forEach(lightState -> lightState.makeFlash(true));
//        else
//            Arrays.stream(states).forEach(lightState -> lightState.makeFlash(false));
//    }

    /*
        Getters & setters
     */

    public LightState[] getStates() {
        return states;
    }

    public void setStates(LightState[] states) {
        this.states = states;

//        if (getFrames() > 0)
//            checkFlash();
    }
}