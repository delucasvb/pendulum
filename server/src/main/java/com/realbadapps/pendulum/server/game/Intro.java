package com.realbadapps.pendulum.server.game;

public class Intro extends Game {
    public static final String TYPE = "intro";

    private IntroScene[] scenes;

    public Intro() {
        super(TYPE);
    }

    @Override
    public void convertFramesToTime() {
        // Not supported
    }

    /*
        Getters & setters
     */

    public IntroScene[] getScenes() {
        return scenes;
    }

    public void setScenes(IntroScene[] scenes) {
        this.scenes = scenes;
    }

    public static class IntroScene {
        private String content;
        private long length;

        public IntroScene() {
            //
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getLength() {
            return length;
        }

        public void setLength(long length) {
            this.length = length;
        }
    }
}