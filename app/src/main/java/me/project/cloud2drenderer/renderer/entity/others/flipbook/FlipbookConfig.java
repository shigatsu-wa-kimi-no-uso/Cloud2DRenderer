package me.project.cloud2drenderer.renderer.entity.others.flipbook;


import me.project.cloud2drenderer.renderer.entity.material.SixWayLighting;
import me.project.cloud2drenderer.util.MatUtils;

public class FlipbookConfig {


    private int id;

    private SixWayLighting material;

    private float[] shape = new float[2];

    private float framesPerSecondUB;

    private float framesPerSecondLB;

    private float framesPerSecond;

    private float[] position = new float[3];

    private float[] refreshPositionLB = new float[3];

    private float[] refreshPositionUB = new float[3];

    private float[] moveDirection = new float[3];

    private float velocityLB;

    private float velocityUB;

    private float velocity;

    private float[] scale = new float[3];

    private float scaleLB;

    private float scaleUB;

    private float frameCnt;

    public FlipbookConfig() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void copy(FlipbookConfig dest) {
        dest.id = id;
        dest.material = material;
        MatUtils.arrayCopy(dest.shape, shape);
        dest.framesPerSecondUB = framesPerSecondUB;
        dest.framesPerSecondLB = framesPerSecondLB;
        MatUtils.arrayCopy(dest.position, position);
        MatUtils.arrayCopy(dest.refreshPositionLB, refreshPositionLB);
        MatUtils.arrayCopy(dest.refreshPositionUB, refreshPositionUB);
        MatUtils.arrayCopy(dest.moveDirection, moveDirection);
        dest.velocity = velocity;
        dest.velocityLB = velocityLB;
        dest.velocityUB = velocityUB;
        MatUtils.arrayCopy(dest.scale, scale);
        dest.scaleLB = scaleLB;
        dest.scaleUB = scaleUB;
        dest.frameCnt = frameCnt;
    }


    public float getFramesPerSecond() {
        return framesPerSecond;
    }

    public void setFramesPerSecond(float framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
    }

    public FlipbookConfig(int id, SixWayLighting material, float[] shape, float[] position, float[] scale) {
        this.id = id;
        setMaterial(material);
        setShape(shape);
        setPosition(position);
        setScale(scale);
    }

    public float getFramesPerSecondUB() {
        return framesPerSecondUB;
    }

    public void setFramesPerSecondUB(float framesPerSecondUB) {
        this.framesPerSecondUB = framesPerSecondUB;
    }

    public float getFramesPerSecondLB() {
        return framesPerSecondLB;
    }

    public void setFramesPerSecondLB(float framesPerSecondLB) {
        this.framesPerSecondLB = framesPerSecondLB;
    }

    public float[] getRefreshPositionLB() {
        return refreshPositionLB;
    }

    public void setRefreshPositionLB(float[] refreshPositionLB) {
        this.refreshPositionLB = refreshPositionLB;
    }

    public float[] getRefreshPositionUB() {
        return refreshPositionUB;
    }

    public float getScaleLB() {
        return scaleLB;
    }

    public void setScaleLB(float scaleLB) {
        this.scaleLB = scaleLB;
    }

    public float getScaleUB() {
        return scaleUB;
    }

    public void setScaleUB(float scaleUB) {
        this.scaleUB = scaleUB;
    }


    public void setRefreshPositionUB(float[] refreshPositionUB) {
        this.refreshPositionUB = refreshPositionUB;
    }

    public float[] getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(float[] moveDirection) {
        this.moveDirection = moveDirection;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getVelocityLB() {
        return velocityLB;
    }

    public void setVelocityLB(float velocityLB) {
        this.velocityLB = velocityLB;
    }

    public float getVelocityUB() {
        return velocityUB;
    }

    public void setVelocityUB(float velocityUB) {
        this.velocityUB = velocityUB;
    }

    public void setFrameCnt(float frameCnt) {
        this.frameCnt = frameCnt;
    }


    public float getFrameCnt() {
        return frameCnt;
    }

    public SixWayLighting getMaterial() {
        return material;
    }

    public void setMaterial(SixWayLighting material) {
        this.material = material;
    }

    public float[] getShape() {
        frameCnt = shape[0] * shape[1];
        return shape;
    }

    public void setShape(float[] shape) {
        this.shape = shape;
    }

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getScale() {
        return scale;
    }

    public void setScale(float[] scale) {
        this.scale = scale;
    }
}
