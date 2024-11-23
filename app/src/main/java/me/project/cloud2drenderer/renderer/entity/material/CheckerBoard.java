package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class CheckerBoard extends BlinnPhong {

    private final float[] colors;

    public CheckerBoard() {
        textures = new Texture[0];
        colors = new float[6];
    }

    public float[] getColors() {
        return colors;
    }

    public void setColor1(float r, float g, float b) {
        this.colors[0] = r;
        this.colors[1] = g;
        this.colors[2] = b;
    }


    public void setColor2(float r, float g, float b) {
        this.colors[3] = r;
        this.colors[4] = g;
        this.colors[5] = b;
    }


}
