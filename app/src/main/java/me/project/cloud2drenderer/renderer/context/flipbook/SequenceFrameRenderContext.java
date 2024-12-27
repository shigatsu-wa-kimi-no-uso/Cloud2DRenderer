package me.project.cloud2drenderer.renderer.context.flipbook;

import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.entity.material.DiffuseTextureMaterial;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.SequenceFrameParams;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;

public class SequenceFrameRenderContext extends RenderContext {
    private SequenceFrameParams seqFrameParams;

    private DiffuseTextureMaterial material;


    private float[] transform;


    public SequenceFrameRenderContext(){
    }

    @Override
    public void setTransform(float[] transform) {
        this.transform = transform;
    }

    public void setSeqFrameParams(SequenceFrameParams seqFrameParams) {
        this.seqFrameParams = seqFrameParams;
    }

    @ShaderUniform(uniformName = "uSeqFrameParams",flags = {UniformFlag.IS_STRUCT})
    public SequenceFrameParams getSeqFrameParams() {
        return seqFrameParams;
    }


    public DiffuseTextureMaterial getMaterial() {
        return material;
    }

    public void setMaterial(DiffuseTextureMaterial material) {
        this.material = material;
    }

    public Texture[] getTextures() {
        return material.getTextures();
    }

    @Override
    public void setMaterial(Material material) {
        this.material = (DiffuseTextureMaterial) material;
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

    @ShaderUniform(uniformName = "uFlipBookTexture")
    public int getFlipBookTextureUnit(){
        return material.getDiffuseTexture().unit;
    }

    @Override
    @ShaderUniform(uniformName = "uModeling")
    public float[] getTransform() {
        return transform;
    }


    @Override
    public Shader getShader() {
        return material.getShader();
    }


    //每绘制一帧前都会调用一次
    @Override
    public void adjustContext() {
        seqFrameParams.increaseCurrentFrameIndex(1);
    }

    @Override
    public void initContext() {

    }
}
