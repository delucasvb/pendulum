package com.realbadapps.pendulum.server.game;

import com.realbadapps.pendulum.server.media.Audio;
import com.realbadapps.pendulum.server.media.Illumination;
import com.realbadapps.pendulum.server.media.Scene;
import com.realbadapps.pendulum.server.media.Window;

public class PictureFlash extends Game {
    public static final String TYPE = "picture_flash";
    public static final long STARTING_DELAY = 5000;

    private Scene[] scenes;
    private Audio audio;
    private Illumination[] lights;
    private Window[] windows;

    public PictureFlash() {
        super(TYPE);
    }

    @Override
    public void convertFramesToTime() {
        long delay = STARTING_DELAY;
        for (Scene scene : scenes) {
            scene.setTime(delay);
            delay += framesToTime(scene.getFrames());
        }

        delay = STARTING_DELAY;
        for (Illumination illumination : lights) {
            illumination.setTime(delay);
            delay += framesToTime(illumination.getFrames());
        }

        for (Window window : windows) {
            window.setStart(framesToTime((int)window.getStart()) + STARTING_DELAY);
            window.setEnd(framesToTime((int)window.getEnd()) + STARTING_DELAY);
        }
    }

    private long framesToTime(int frames) {
        return (long)((double)frames * 60000d / audio.getBpm());
    }

    /*
        Getters & setters
     */

    public Scene[] getScenes() {
        return scenes;
    }

    public void setScenes(Scene[] scenes) {
        this.scenes = scenes;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public Illumination[] getLights() {
        return lights;
    }

    public void setLights(Illumination[] lights) {
        this.lights = lights;
    }

    public Window[] getWindows() {
        return windows;
    }

    public void setWindows(Window[] windows) {
        this.windows = windows;
    }
}