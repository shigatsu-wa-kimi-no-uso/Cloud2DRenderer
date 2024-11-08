package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class DiffuseTextureMaterial extends Material{

    private Texture diffuseTexture;

    public DiffuseTextureMaterial(){
        textures = new Texture[1];
    }


    public Texture getDiffuseTexture() {
        return diffuseTexture;
    }

    public void setDiffuseTexture(Texture diffuseTexture) {
        this.diffuseTexture = diffuseTexture;
        textures[0] = diffuseTexture;
    }


    @Override
    public void distribute() {
        diffuseTexture = textures[0];
    }
}
