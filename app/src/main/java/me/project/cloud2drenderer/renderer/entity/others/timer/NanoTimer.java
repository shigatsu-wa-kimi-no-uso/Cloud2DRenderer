package me.project.cloud2drenderer.renderer.entity.others.timer;

public class NanoTimer extends Timer{

    @Override
    public long getTick(){
        return System.nanoTime();
    }


    @Override
    public float toSeconds(long time){
        return time/1E9f;
    }

}
