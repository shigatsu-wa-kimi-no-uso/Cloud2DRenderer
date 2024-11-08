package me.project.cloud2drenderer.renderer.entity.others;

import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;

public class SequenceFrameParams {

    private float frequency; //每多少帧序列帧切换到下一帧


    private int currentFrameIndex;


    private float[] flipBookShape;


    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public void setCurrentFrameIndex(int currentFrameIndex) {
        this.currentFrameIndex = currentFrameIndex;
    }

    public void increaseCurrentFrameIndex() {
        this.currentFrameIndex++;
    }

    public float[] getFlipBookShape() {
        return flipBookShape;
    }

    public void setFlipBookShape(float[] flipBookShape) {
        this.flipBookShape = flipBookShape;
    }
}
