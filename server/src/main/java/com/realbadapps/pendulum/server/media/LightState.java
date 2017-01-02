package com.realbadapps.pendulum.server.media;

public class LightState {
    private int id;
    private State state;

    public LightState() {}

    void makeFlash(boolean flash) {
        state.setTransitiontime(flash ? 0 : 1);
    }

    /*
        Getters & setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public static class State {
        private boolean on;
        private int hue;
        private int sat;
        private int bri;
        private int transitiontime;

        public State() {
            this.on = true;
            this.transitiontime = 0;
            this.hue = 0;
            this.sat = 254;
            this.bri = 254;
        }

        /*
            Getters & setters
         */

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        public int getHue() {
            return hue;
        }

        public void setHue(int hue) {
            this.hue = hue;
        }

        public int getSat() {
            return sat;
        }

        public void setSat(int sat) {
            this.sat = sat;
        }

        public int getBri() {
            return bri;
        }

        public void setBri(int bri) {
            this.bri = bri;
        }

        public int getTransitiontime() {
            return transitiontime;
        }

        public void setTransitiontime(int transitiontime) {
            this.transitiontime = transitiontime;
        }
    }
}