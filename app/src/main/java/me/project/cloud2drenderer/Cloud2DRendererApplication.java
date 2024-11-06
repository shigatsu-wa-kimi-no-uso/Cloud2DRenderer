package me.project.cloud2drenderer;

import android.app.Application;

import me.project.cloud2drenderer.exception.ExceptionHandler;

public class Cloud2DRendererApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
    }

}
