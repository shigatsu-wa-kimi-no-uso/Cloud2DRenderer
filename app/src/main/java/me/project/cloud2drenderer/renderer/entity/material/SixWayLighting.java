package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class SixWayLighting extends DiffuseTextureMaterial{

    public SixWayLighting(){
        textures = new Texture[3];
    }

    public void setRLTLighting(Texture texture){
        textures[1] = texture;
    }

    public void setBBFLighting(Texture texture){
        textures[2] = texture;
    }

    public Texture getRLTLighting(){
        return textures[1];
    }

    public Texture getBBFLighting(){
        return textures[2];
    }


}
