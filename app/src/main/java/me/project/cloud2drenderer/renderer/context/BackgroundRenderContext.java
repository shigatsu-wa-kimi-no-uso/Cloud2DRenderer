package me.project.cloud2drenderer.renderer.context;

import me.project.cloud2drenderer.renderer.entity.material.DiffuseTextureMaterial;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformVar;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;

public class BackgroundRenderContext extends RenderContext{


    private float[] transform;

    private DiffuseTextureMaterial material;

    @ShaderUniform(uniformName = "uTexture",flags = {})
    public final UniformVar<Integer> uTextureWrapper = new UniformVar<>();

    @ShaderUniform(uniformName = "uView",flags = {})
    public final UniformVar<float[]> uViewWrapper = new UniformVar<>();

    @ShaderUniform(uniformName = "uProjection",flags = {})
    public final UniformVar<float[]> uProjectionWrapper = new UniformVar<>();

    @Override
    @ShaderUniform(uniformName = "uModeling")
    public float[] getTransform() {
        return transform;
    }

    public float[] getView(){
        return camera.getView();
    }

    //  @ShaderUniform(uniformName = "uProjection")
    public float[] getProjection(){
        return camera.getProjection();
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
    public Texture[] getTextures() {
        return material.getTextures();
    }

    @Override
    public void setMaterial(Material material) {
        this.material =(DiffuseTextureMaterial) material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(Material[] material) {

    }



    public int getTextureUnit(){
        return material.getDiffuseTexture().unit;
    }

    @Override
    public void adjustContext() {

    }

    @Override
    public void initContext() {
        uTextureWrapper.setValue(getTextureUnit());
        uViewWrapper.setValue(getView());
        uProjectionWrapper.setValue(getProjection());
        commitUniformAssignment(uTextureWrapper);
        commitUniformAssignment(uViewWrapper);
        commitUniformAssignment(uProjectionWrapper);
    }
}
