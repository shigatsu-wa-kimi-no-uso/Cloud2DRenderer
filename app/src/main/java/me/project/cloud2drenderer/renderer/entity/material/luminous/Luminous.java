package me.project.cloud2drenderer.renderer.entity.material.luminous;


import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class Luminous extends Material {


    private float[] lightIntensity;



    public Luminous(){
        textures = new Texture[0];
    }

    public float[] getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(float[] lightIntensity) {
        this.lightIntensity = lightIntensity;
    }


}
