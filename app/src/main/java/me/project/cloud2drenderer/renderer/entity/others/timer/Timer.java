package me.project.cloud2drenderer.renderer.entity.others.timer;

public abstract class Timer {

    protected long lastTick;

    abstract public long getTick();

    abstract protected float toSeconds(long time);

    protected void refreshTick(long tick){
        lastTick = tick;
    }


    public void startTick(){
        refreshTick(getTick());
    }

    public long getDuration(){
        return getTick() - lastTick;
    }

    public long getDurationAndReset(){
        long thisTick = getTick();
        long tickCnt = thisTick - lastTick;
        refreshTick(thisTick);
        return tickCnt;
    }


    public float getDurationInSeconds() {
        return toSeconds(getDuration());
    }

    public float getDurationInSecondsAndReset(){
        return toSeconds(getDurationAndReset());
    }
}
