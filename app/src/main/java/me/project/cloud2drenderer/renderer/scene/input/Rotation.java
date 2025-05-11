package me.project.cloud2drenderer.renderer.scene.input;

import me.project.cloud2drenderer.util.MatUtils;

public class Rotation {
    public float x,y,z;

    public Rotation(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setRotate(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setRotateX(float x) {
        this.x = x;
    }

    public void setRotateY(float y) {
        this.y = y;
    }

    public void setRotateZ(float z) {
        this.z = z;
    }
    float[] getDirection(){
        float[] rotation = MatUtils.newRotationMatrix(x,y,z);
        float[] dir = {0,0,-1,0};
        dir = MatUtils.matVecMultiply(rotation,dir,4);
        return MatUtils.normalized(rotation);
    }

}
