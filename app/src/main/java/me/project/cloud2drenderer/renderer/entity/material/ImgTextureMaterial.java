package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class ImgTextureMaterial extends Material{
    private Texture imgTexture;

    public ImgTextureMaterial(){
        textures = new Texture[2];
    }
    public Texture getImgTexture() {
        return imgTexture;
    }

    public void setImgTexture(Texture imgTexture) {
        this.imgTexture = imgTexture;
        textures[0] = this.imgTexture;
    }
}
