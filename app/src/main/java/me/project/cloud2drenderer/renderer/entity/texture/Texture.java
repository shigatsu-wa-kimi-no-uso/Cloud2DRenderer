package me.project.cloud2drenderer.renderer.entity.texture;

import me.project.cloud2drenderer.opengl.glresource.texture.GLTexture;

public class Texture {

    public GLTexture texture;
    public int unit;

    public int location;



    public static Texture nullTexture(){
        Texture texture = new Texture();
        texture.texture = GLTexture.nullTexture();
        return texture;
    }
}
