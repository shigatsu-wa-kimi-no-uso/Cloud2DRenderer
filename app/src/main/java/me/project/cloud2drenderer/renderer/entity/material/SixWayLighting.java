package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class SixWayLighting extends DiffuseTextureMaterial{



    public SixWayLighting(){
        textures = new Texture[3];
        textures[0] = Texture.nullTexture();
        textures[1] = Texture.nullTexture();
        textures[2] = Texture.nullTexture();
    }

    public void setLightMapA(Texture texture){
        textures[1] = texture;
    }

    public void setLightMapB(Texture texture){
        textures[2] = texture;
    }


    //maybe right left top  or   right top back
    public Texture getLightMapA(){
        return textures[1];
    }

    //maybe bottom back front  or  left bottom front
    public Texture getLightMapB(){
        return textures[2];
    }


}
