package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class TerrainMaterial extends Material{

    public TerrainMaterial(){
        textures = new Texture[2];
    }

    public Texture getHeightMap() {
        return textures[0];
    }

    public void setAlbedo(Texture texture){
        textures[1] = texture;
    }

    public void setHeightMap(Texture texture){
        textures[0] = texture;
    }


    public Texture getAlbedo() {
        return textures[1];
    }


}
