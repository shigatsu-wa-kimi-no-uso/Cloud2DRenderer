package me.project.cloud2drenderer.renderer.context;

import android.opengl.Matrix;

import me.project.cloud2drenderer.renderer.entity.others.light.PointLight;
import me.project.cloud2drenderer.renderer.entity.material.BlinnPhong;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.ShaderUniform;

public class BlinnPhongRenderContext extends RenderContext{

    protected BlinnPhong material;


    protected PointLight pointLight;


    private float[] ambientIntensity;

    private float[] transform;

    private final float[] modelInverse;

    private final float[] modelIT;


    public BlinnPhongRenderContext(){
        modelInverse = new float[16];
        modelIT = new float[16];
    }

    @Override
    @ShaderUniform(uniformName = "uModeling")
    public float[] getTransform() {
        return transform;
    }

    @Override
    public void setTransform(float[] transform) {
        this.transform = transform;
        updateModelIT();
    }



    @ShaderUniform(uniformName = "uMaterial",flags = {UniformFlag.IS_STRUCT})
    public BlinnPhong getMaterial(){
        return material;
    }

    @ShaderUniform(uniformName = "uPointLight",flags = {UniformFlag.IS_STRUCT})
    public PointLight getPointLight() {
        return pointLight;
    }
    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

    @ShaderUniform(uniformName = "uView")
    public float[] getView(){
        return camera.getView();
    }

    @ShaderUniform(uniformName = "uProjection")
    public float[] getProjection(){
        return camera.getProjection();
    }


    @ShaderUniform(uniformName = "uEyePosition")
    public float[] getEyePosition(){
        return camera.getPosition();
    }

    @ShaderUniform(uniformName = "uAmbientIntensity")
    public float[] getAmbientIntensity(){
        return ambientIntensity;
    }

    public void setAmbientIntensity(float[] ambientIntensity) {
        this.ambientIntensity = ambientIntensity;
    }

    @ShaderUniform(uniformName = "uModelIT")
    public float[] getModelIT(){
        return modelIT;
    }

    private void updateModelIT(){
        Matrix.invertM(modelInverse,0,transform,0);
        Matrix.transposeM(modelIT,0, modelInverse,0);
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
        this.material = (BlinnPhong) material;
    }

    @Override
    public void setMaterial(Material[] material) {

    }

    @Override
    public void adjustContext() {

    }

    @Override
    public void initContext() {

    }
}
