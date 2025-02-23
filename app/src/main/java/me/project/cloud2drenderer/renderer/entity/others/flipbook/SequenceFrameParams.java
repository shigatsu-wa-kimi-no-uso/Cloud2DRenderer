package me.project.cloud2drenderer.renderer.entity.others.flipbook;

import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;

public class SequenceFrameParams {

    private float currentFrameIndex;

    private int[] flipBookShape;

    private SequenceFrameStatus status = SequenceFrameStatus.IDLE;

    public SequenceFrameStatus getStatus() {
        return status;
    }

    public void setStatus(SequenceFrameStatus status) {
        this.status = status;
    }

    @ShaderUniform(uniformName = "currFrameIndex")
    public float getCurrentFrameIndex() {
        if(currentFrameIndex < 0){
            return 0;
        }else {
            return currentFrameIndex;
        }
    }

    public boolean isCurrentFrameIndexBelowZero() {
        return currentFrameIndex < 0;
    }

    public void setCurrentFrameIndex(float currentFrameIndex) {
        this.currentFrameIndex = currentFrameIndex;
    }

    public void resetCurrentFrameIndex() {
        currentFrameIndex = 0;
    }

    public void setIdleTime(float fps,float timeInSeconds){
        this.currentFrameIndex = - timeInSeconds*fps;
    }

    public void updateCurrentFrameIndex(float durationInSeconds,float fps) {
        this.currentFrameIndex += durationInSeconds * fps;
    }


    public void increaseCurrentFrameIndex(float increment) {
        this.currentFrameIndex += increment;
    }

    @ShaderUniform(uniformName = "flipBookShape")
    public int[] getFlipBookShape() {
        return flipBookShape;
    }

    public void setFlipBookShape(int[] flipBookShape) {
        this.flipBookShape = flipBookShape;
    }
}
