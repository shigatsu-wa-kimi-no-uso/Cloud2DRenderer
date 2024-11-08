package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public abstract class Material {
    //一个材质只能绑定一个shader
    public String name;

    protected Shader shader;

    protected Texture[] textures;

    protected Material(){}

    public Texture[] getTextures(){
        return textures;
    }

    public void setShader(Shader shader){
        this.shader = shader;
    }

    public Shader getShader(){
        return shader;
    }

    public abstract void distribute();
}
