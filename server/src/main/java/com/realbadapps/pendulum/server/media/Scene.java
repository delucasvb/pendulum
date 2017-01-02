package com.realbadapps.pendulum.server.media;

public class Scene extends Framed {
    private String type;
    private String content;
    private boolean showProgress;

    public Scene() {
        //
    }

    /*
        Getters & setters
     */

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }
}