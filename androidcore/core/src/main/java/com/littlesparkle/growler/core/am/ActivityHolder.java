package com.littlesparkle.growler.core.am;

import android.app.Activity;

public class ActivityHolder {

    private Activity activity;
    private boolean isLanding = false;
    private boolean isRunning = false;
    private boolean isStopped = true;
    private String activityName;

    public ActivityHolder(Activity activity) {
        this(activity, false);
    }

    public ActivityHolder(Activity activity, boolean isLanding) {
        this.activity = activity;
        this.activityName = activity.getClass().getSimpleName();
        this.isLanding = isLanding;
    }

    public void pause() {
        isRunning = false;
    }

    public void resume() {
        isRunning = true;
    }

    public void start() {
        isStopped = false;
    }

    public void stop() {
        isStopped = true;
    }

    public Activity getActivity() {
        return activity;
    }

    public boolean isLanding() {
        return isLanding;
    }

    public void setLanding(boolean isLanding) {
        this.isLanding = isLanding;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public String getActivityName() {
        return activityName;
    }

    public void finish() {
        activity.finish();
    }

    public void removed() {
    }
}