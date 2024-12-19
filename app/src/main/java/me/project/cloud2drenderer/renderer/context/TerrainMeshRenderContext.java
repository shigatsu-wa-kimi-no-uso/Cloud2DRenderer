package me.project.cloud2drenderer.renderer.context;

import android.opengl.Matrix;

import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.material.TerrainMaterial;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.ShaderUniform;


public class TerrainMeshRenderContext extends RenderContext{

    private TerrainMaterial material;


    private float[] transform;

    public TerrainMeshRenderContext(){
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

    @Override
    public Shader getShader() {
        return material.getShader();
    }

    @Override
    public void setMaterial(Material material) {
        this.material = (TerrainMaterial) material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(Material[] material) {

    }

    @ShaderUniform(uniformName = "uView")
    public float[] getView(){
        return camera.getView();
    }

    @ShaderUniform(uniformName = "uProjection")
    public float[] getProjection(){
        return camera.getProjection();
    }

    @ShaderUniform(uniformName = "heightMap")
    public int getHeightMapUnit(){
        return material.getHeightMap().unit;
    }

    @ShaderUniform(uniformName = "albedo")
    public int getAlbedoTextureUnit(){
        return material.getAlbedo().unit;
    }

    @Override
    public Texture[] getTextures(){
        return material.getTextures();
    }


    @ShaderUniform(uniformName = "uRatio")
    public float getRatio(){
        return 0.5f;
    }

    @Override
    public void adjustContext() {

    }

    @Override
    public void initContext() {

    }
}
