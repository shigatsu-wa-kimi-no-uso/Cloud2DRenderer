package me.project.cloud2drenderer.renderer.entity.others.flipbook;

import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.ShaderUniform;

public class SequenceFrameParams {

    private float frequency; //每多少帧序列帧切换到下一帧

    private float delta;

    private int currentMapIndex;

    private float currentFrameIndex;


    private float[] flipBookShape;

    /*

    @ShaderUniform(uniformName = "frequency")
    public float getFrequency() {
        return frequency;
    }*/

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }



    @ShaderUniform(uniformName = "currFrameIndex")
    public float getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public void setCurrentFrameIndex(float currentFrameIndex) {
        this.currentFrameIndex = currentFrameIndex;
    }

    public void resetCurrentFrameIndex(){
        currentFrameIndex = 0;
    }

    public void increaseCurrentFrameIndex() {
        this.currentFrameIndex++;
    }


    public void increaseCurrentFrameIndex(float increment) {
        this.currentFrameIndex+=increment;
    }

    @ShaderUniform(uniformName = "flipBookShape")
    public float[] getFlipBookShape() {
        return flipBookShape;
    }

    public void setFlipBookShape(float[] flipBookShape) {
        this.flipBookShape = flipBookShape;
    }
}
