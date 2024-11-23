package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class DiffuseTextureMaterial extends Material{



    public DiffuseTextureMaterial(){
        textures = new Texture[1];
    }


    public Texture getDiffuseTexture() {
        return textures[0];
    }

    public void setDiffuseTexture(Texture texture){
        textures[0] = texture;
    }


}
