package me.project.cloud2drenderer.renderer.scene.input;

public class Rotation2 {
    private float horizontal,vertical;

    public Rotation2()
    {

    }



    public void setRotation2(float horizontal, float vertical)
    {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public void setHorizontal(float horizontal)
    {
        this.horizontal = horizontal;
    }

    public void setVertical(float vertical)
    {
        this.vertical = vertical;
    }

    public float getVertical() {
        return vertical;
    }

    public float getHorizontal() {
        return horizontal;
    }

    public float[] getDirection(){
        float theta = (float)Math.toRadians(vertical);
        float phi = (float)Math.toRadians(horizontal);
        float sinTheta = (float)Math.sin(theta);
        float sinPhi = (float)Math.sin(phi);
        float cosTheta = (float)Math.cos(theta);
        float cosPhi = (float)Math.cos(phi);
        float x =sinTheta * cosPhi;
        float y = sinTheta * sinPhi;
        float z = cosTheta;
        return new float[]{-y,-z,-x};
    }
}
