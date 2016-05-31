package com.vpooc.crashdemo;

import java.util.ArrayList;


import android.app.Activity;
import android.app.Application;
import android.os.Process;

public class TApplication extends Application {

    public static ArrayList<Activity> activities = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        System.out.println("TApplication");
        //未处理的异常处理并重新启动应用程序
        CrashHandler crashHandler = new CrashHandler(this);
        Thread mainThread = Thread.currentThread();
        mainThread.setDefaultUncaughtExceptionHandler(crashHandler);
        super.onCreate();

    }

    public static void finishActivity() {
        ArrayList<Activity> l = activities;
        for (Activity a : l) {
            a.finish();
        }
        Process.killProcess(Process.myPid());
    }
}
