package com.cara2v.util;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chiranjib on 8/12/17.
 **/

public class MyApplication extends Application {
    private Activity activeActivity;
    private static Map<String, Activity> activityMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        setupActivityListener();
    }

    public static void addActivity(Activity activity){
        activityMap.put(activity.getClass().getName(), activity);
    }

    public static void finishActivity(String activityName){
        if(activityMap.get(activityName)!=null){
            activityMap.get(activityName).finish();
        }
    }

    public static void finishAllActivity(){
        for(Map.Entry<String, Activity> entry : activityMap.entrySet()) {
            //String key = entry.getKey();
            Activity activity = entry.getValue();
            activity.finish();
        }
    }

    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                activeActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                activeActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public Activity getActiveActivity() {
        return activeActivity;
    }
}
