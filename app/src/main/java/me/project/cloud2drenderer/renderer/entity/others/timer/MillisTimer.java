package me.project.cloud2drenderer.renderer.entity.others.timer;

public class MillisTimer extends Timer{

    @Override
    public long getTick() {
        return System.currentTimeMillis();
    }

    @Override
    public float toSeconds(long time){
        return time/1E3f;
    }


}
