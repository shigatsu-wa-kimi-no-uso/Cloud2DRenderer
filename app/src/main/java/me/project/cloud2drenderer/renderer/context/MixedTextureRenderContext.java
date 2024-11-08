package me.project.cloud2drenderer.renderer.context;

import android.opengl.Matrix;

import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.material.MixedImgMaterial;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;

public class MixedTextureRenderContext extends RenderContext{

    private MixedImgMaterial material;

    @ShaderUniform(uniformName = "uModeling",flags = {UniformFlag.USING_RAW,UniformFlag.AUTO_ASSIGN})
    public float[] transform;

    @ShaderUniform(uniformName = "uRatio")
    public float getRatio(){
        return material.getRatio();
    }

    public MixedTextureRenderContext(){
        transform = new float[16];
        Matrix.setIdentityM(transform,0);

    }

    public MixedImgMaterial getMaterial() {
        return material;
    }

    public void setMaterial(MixedImgMaterial material) {
        this.material = material;
    }

    public float[] getTransform() {
        return transform;
    }

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

    @ShaderUniform(uniformName = "texture1")
    public int getTexture1Unit(){
        return material.getTextures()[0].unit;
    }

    @ShaderUniform(uniformName = "texture2")
    public int getTexture2Unit(){
        return material.getTextures()[1].unit;
    }

    @Override
    public Texture[] getTextures(){
        return material.getTextures();
    }


    @Override
    public Shader getShader() {
        return material.getShader();
    }

    @Override
    public void setMaterial(Material material) {
        this.material = (MixedImgMaterial) material;
    }

    @Override
    public void adjustContext() {

    }

    @Override
    public void initContext() {

    }
}
