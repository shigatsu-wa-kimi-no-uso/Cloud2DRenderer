package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public abstract class Material {
    //一个材质只能绑定一个shader


    public Shader shader;


    public Texture[] textures;
}
