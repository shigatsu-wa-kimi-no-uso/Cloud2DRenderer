package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.material.TextureLoader;

public abstract class Material {
    //一个材质只能绑定一个shader
    public String name;

    protected boolean fullyLoaded;

    protected Shader shader;

    protected Texture[] textures;

    protected TextureLoader textureLoader;


    protected Material(){}

    public boolean isFullyLoaded() {
        return fullyLoaded;
    }

    public void setFullyLoaded(boolean fullyLoaded) {
        this.fullyLoaded = fullyLoaded;
    }

    public TextureLoader getTextureLoader() {
        return textureLoader;
    }

    public void setTextureLoader(TextureLoader bitmapLoader) {
        this.textureLoader = bitmapLoader;
    }



    public Texture[] getTextures(){
        return textures;
    }

    public void setShader(Shader shader){
        this.shader = shader;
    }

    public Shader getShader(){
        return shader;
    }

}
