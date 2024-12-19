package me.project.cloud2drenderer.renderer.entity.texture;

import me.project.cloud2drenderer.opengl.glcomponent.texture.GLTexture;

public class Texture {

    public GLTexture texture;
    public int unit;

    private final float[] shape = new float[2];


    public void setShape(float width,float height){
        shape[0] = width;
        shape[1] = height;
    }

    public float[] getShape(){
        return shape;
    }

    public static Texture nullTexture(){
        Texture texture = new Texture();
        texture.texture = GLTexture.nullTexture();
        return texture;
    }
}
