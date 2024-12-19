package me.project.cloud2drenderer.renderer.context;

import android.opengl.Matrix;

import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.material.luminous.Luminous;
import me.project.cloud2drenderer.renderer.entity.others.light.DistantLight;
import me.project.cloud2drenderer.renderer.entity.others.light.PointLight;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.ShaderUniform;
import me.project.cloud2drenderer.util.MatUtils;

public class LuminousRenderContext extends RenderContext{
    private float[] transform;

    private PointLight pointLight;



    private Luminous material;

    public LuminousRenderContext(){

    }


    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

    @ShaderUniform(uniformName = "uLightIntensity")
    public float[] getLightIntensity(){
        return material.getLightIntensity();
    }

    @ShaderUniform(uniformName = "uEyePosition")
    public float[] getEyePosition(){
        return camera.getPosition();
    }

    @Override
    @ShaderUniform(uniformName = "uModeling")
    public float[] getTransform() {
        return transform;
    }

    @Override
    public void setTransform(float[] transform) {
        this.transform = transform;
    }

    @ShaderUniform(uniformName = "uView")
    public float[] getView(){
        return camera.getView();
    }

    @ShaderUniform(uniformName = "uProjection")
    public float[] getProjection(){
        return camera.getProjection();
    }

    @Override
    public Shader getShader() {
        return material.getShader();
    }

    @Override
    public Texture[] getTextures() {
        return material.getTextures();
    }

    @Override
    public void setMaterial(Material material) {
        this.material = (Luminous) material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(Material[] material) {

    }

    long startTimeMillis = System.currentTimeMillis();


    public float[] getCoordinates(float angleInDegrees,float r){
        float rad = angleInDegrees*(float)Math.PI/180.0f;
        float x = (float)Math.cos(rad);
        float y = (float)Math.sin(rad);
        return new float[]{x*r,y*r,transform[2]};
    }


    void setTransformPosition(float[] position){
        transform[12] = position[0];
        transform[13] = position[1];
        transform[14] = position[2];
    }

    @Override
    public void adjustContext() {
        /*
        long currTimeMillis = System.currentTimeMillis();
        long timeElapsedMillis = currTimeMillis - startTimeMillis;
        float degreesPerSecond = 20f;
        float angle = (timeElapsedMillis/1000.0f)*degreesPerSecond;
        float[] newPos = getCoordinates(angle,1.0f);
        pointLight.setPosition(newPos);
        setTransformPosition(newPos);*/
    }

    @Override
    public void initContext() {

    }
}
