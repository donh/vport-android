package com.littlesparkle.growler.core.am;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class ActivityManager {
    private final int ICE_CREAM_SANDWICH = 14;

    private boolean isManagedByApplication = false;

    /**
     * The static instance of this class
     */
    private static ActivityManager instance;

    /**
     * This array holds activities and their running states
     */
    private ArrayList<ActivityHolder> activities;

    /**
     * Reference to move on another activity after finishing current one
     * instead of returning back to previous
     * <p/>
     * # Usage:
     * Call setNextStep method on Activity A then start Activity B
     * and on B instead of calling finish method, call moveForward method
     */
    private ActivityTransporter nextStep;

    /**
     * The static synchronized method to get instance to have singleton class structure
     * so we will have only one instance not to get confused with array and its items
     */
    public static synchronized ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
            instance.activities = new ArrayList<>();
        }
        return instance;
    }

    /**
     * By this configuration, you will not need to extend activities from ManagedBaseActivity
     * this will automatically notify ActivityManager about activityLifecycle whenever necessary
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void configure(Application application) {
        if (Build.VERSION.SDK_INT < ICE_CREAM_SANDWICH) {
            Log.e("ActivityManager", "This method is not supported before ICE_CREAM_SANDWICH (Api Level 14)");
            return;
        }

        isManagedByApplication = true;

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityManager.getInstance().onCreate(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                ActivityManager.getInstance().onStart(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                ActivityManager.getInstance().onResume(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                ActivityManager.getInstance().onPause(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                ActivityManager.getInstance().onStop(activity);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityManager.getInstance().onDestroy(activity);
            }
        });
    }

    /**
     * To check whether currently application is running or not.
     * If application is on background -pressed home button or somehow interrupt-
     * then it assumes that application is not running
     */
    public boolean isApplicationRunning() {
        synchronized (activities) {
            for (int i = 0; i < activities.size(); i++)
                if (activities.get(i).isRunning())
                    return true;
        }
        return false;
    }

    /**
     * Returns currently active activity
     */
    public Activity getCurrentActivity() {
        if (activities.size() > 0) {
            synchronized (activities) {
                for (int i = 0; i < activities.size(); i++) {
                    if (activities.get(i).isRunning()) {
                        return activities.get(i).getActivity();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Keeps reference to next activity which is needed to call after current activity
     * instead of finishing back to previous one
     */
    public void setNextStep(ActivityTransporter transporter) {
        nextStep = transporter;
    }

    /**
     * Starts preDefined activity with preDefined extras from current activity,
     * and finishes current if it is required
     */
    public void moveForward(boolean finishThis) {
        if (nextStep != null) {
            Activity current = getCurrentActivity();
            if (current != null) {
                Intent intent = new Intent(current, nextStep.toClazz());
                if (nextStep.getExtras() != null) {
                    for (int i = 0; i < nextStep.getExtras().size(); i++) {
                        ActivityExtra extra = nextStep.getExtras().get(i);
                        intent.putExtra(extra.getKey(), extra.getValue());
                    }
                }
                getCurrentActivity().startActivity(intent);
                if (finishThis)
                    current.finish();
            }
        }
    }

    /**
     * Calls onCreate method asLanding default false.
     * # This method is just to keep track has no affect on given activity
     */
    public void onCreate(Activity activity) {
        onCreate(activity, false);
    }

    /**
     * Appends array, which holds instance of activities,
     * and specifies if given activity is landing one or not.
     * # This method is just to keep track has no affect on given activity
     */
    public void onCreate(Activity activity, boolean asLanding) {
        if (isManagedByApplication && activity instanceof ManagedBaseActivity) {
            Log.e("ActivityManager", "If your application extended from ManagedApplication," +
                    "DO NOT extend your activities from ManagedBaseActivity");
            return;
        }
        synchronized (activities) {
            activities.add(new ActivityHolder(activity, asLanding));
        }
    }

    /**
     * Return landing activity's instance
     */
    public ActivityHolder getLanding() {
        synchronized (activities) {
            for (int i = 0; i < activities.size(); i++) {
                if (activities.get(i).isLanding())
                    return activities.get(i);
            }
        }
        return null;
    }

    /**
     * To change activities landing property later
     */
    public void setAsLanding(Activity activity) {
        synchronized (activities) {
            // Clear previous landing, because having more than one landing is not supported
            for (int i = 0; i < activities.size(); i++) {
                if (activities.get(i).isLanding()) {
                    activities.get(i).setLanding(false);
                    break;
                }
            }

            // Set currently given as landing
            for (int i = 0; i < activities.size(); i++) {
                if (activities.get(i).getActivity().equals(activity)) {
                    activities.get(i).setLanding(true);
                    break;
                }
            }
        }
    }

    /**
     * To determine there is already a landing activity or not
     */
    public boolean hasLanding() {
        synchronized (activities) {
            for (int i = 0; i < activities.size(); i++) {
                if (activities.get(i).isLanding())
                    return true;
            }
        }
        return false;
    }

    /**
     * Changes the state of given activity as it is not running anymore.
     * # This method is just to keep track has no affect on given activity
     */
    public void onPause(Activity activity) {
        synchronized (activities) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (activities.get(i).getActivity().equals(activity)) {
                    activities.get(i).pause();
                    break;
                }
            }
        }
    }

    public void onStart(Activity activity) {
        synchronized (activities) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (activities.get(i).getActivity().equals(activity)) {
                    activities.get(i).start();
                    break;
                }
            }
        }
    }

    public void onStop(Activity activity) {
        synchronized (activities) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (activities.get(i).getActivity().equals(activity)) {
                    activities.get(i).stop();
                    break;
                }
            }
        }
    }

    /**
     * Changes the state of given activity as it is running now.
     * # This method is just to keep track has no affect on given activity
     */
    public void onResume(Activity activity) {
        synchronized (activities) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (activities.get(i).getActivity().equals(activity)) {
                    activities.get(i).resume();
                    break;
                }
            }
        }
    }

    /**
     * Removes given activity from array.
     * # This method is just to keep track has no affect on given activity
     */
    public void onDestroy(Activity activity) {
        synchronized (activities) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (activities.get(i).getActivity().equals(activity)) {
                    activities.remove(i).removed();
                    break;
                }
            }
        }
    }

    /**
     * If any instance of given class exists then finishes and removes it from array.
     */
    public void finishInstance(Class<?> clazz) {
        synchronized (activities) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (clazz.isInstance(activities.get(i).getActivity())) {
                    activities.get(i).finish();
                    activities.remove(i);
                }
            }
        }
    }

    /**
     * Finishes all activities with Landing
     */
    public void finishAll() {
        synchronized (activities) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                activities.get(i).finish();
                activities.remove(i);
            }
        }
    }

    /**
     * Finishes all activities except Landing
     */
    public void toLandingActivity() {
        synchronized (activities) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (activities.get(i).isLanding()) {
                    return;
                } else {
                    activities.get(i).finish();
                    activities.remove(i);
                }
            }
        }
    }

    /**
     * Finishes all activities till instance of given class
     */
    public void toInstanceOf(Class<?> clazz) {
        synchronized (activities) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (clazz.isInstance(activities.get(i).getActivity())) {
                    return;
                } else {
                    activities.get(i).finish();
                    activities.remove(i);
                }
            }
        }
    }

}