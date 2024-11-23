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

    public Texture getTexture1(){
        return textures[0];
    }

    public Texture getTexture2(){
        return textures[1];
    }

    public void setRatio(float ratio){
        this.ratio = ratio;
    }


    public float getRatio(){
        return ratio;
    }



}
