package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class SixWayLighting extends DiffuseTextureMaterial{

    private final float[] shape = new float[2];

    private float framesPerSecond;

    public void setShape(int imagesPerLine,int rowCnt){
        shape[0] = imagesPerLine;
        shape[1] = rowCnt;
    }

    public void setFramesPerSecond(float framesPerSecond){
        this.framesPerSecond = framesPerSecond; //TODO: FPS转每帧翻帧数,需根据当前帧率动态翻页
    }


    public float getFramesPerSecond(){
        return framesPerSecond;
    }

    public final float[] getShape(){
        return shape;
    }

    public SixWayLighting(){
        textures = new Texture[3];
    }

    public void setLightMapA(Texture texture){
        textures[1] = texture;
    }

    public void setLightMapB(Texture texture){
        textures[2] = texture;
    }


    //maybe right left top  or   right top back
    public Texture getLightMapA(){
        return textures[1];
    }

    //maybe bottom back front  or  left bottom front
    public Texture getLightMapB(){
        return textures[2];
    }


}
