package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class MixedImgMaterial extends Material{

    private float ratio;

    public MixedImgMaterial(){
        textures = new Texture[2];
    }

    public void setTexture1(Texture texture){
        textures[0] = texture;
    }

    public void setTexture2(Texture texture){
        textures[1] = texture;
    }

    public void setRatio(float ratio){
        this.ratio = ratio;
    }


    public float getRatio(){
        return ratio;
    }


    @Override
    public void distribute() {

    }
}
