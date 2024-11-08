package me.project.cloud2drenderer.renderer.context;

import android.opengl.Matrix;

import me.project.cloud2drenderer.renderer.entity.material.DiffuseTextureMaterial;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.others.SequenceFrameParams;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;

public class SequenceFrameRenderContext extends RenderContext {

    private SequenceFrameParams seqFrameParams;

    private DiffuseTextureMaterial material;

    @ShaderUniform(uniformName = "uModeling",flags = {UniformFlag.USING_RAW,UniformFlag.AUTO_ASSIGN})
    public float[] transform;


    public SequenceFrameRenderContext(){
        transform = new float[16];
        Matrix.setIdentityM(transform,0);
    }

    public SequenceFrameParams getSeqFrameParams() {
        return seqFrameParams;
    }

    @ShaderUniform(uniformName = "uSeqFrameParams.currFrameIndex")
    public int getCurrentFrameIndex(){
        return seqFrameParams.getCurrentFrameIndex();
    }

    @ShaderUniform(uniformName = "uSeqFrameParams.flipBookShape")
    public float[] getFlipBookShape(){
        return seqFrameParams.getFlipBookShape();
    }

    @ShaderUniform(uniformName = "uSeqFrameParams.frequency")
    public float getFrequency(){
        return seqFrameParams.getFrequency();
    }

    public void setSeqFrameParams(SequenceFrameParams seqFrameParams) {
        this.seqFrameParams = seqFrameParams;
    }

    public DiffuseTextureMaterial getMaterial() {
        return material;
    }

    public void setMaterial(DiffuseTextureMaterial material) {
        this.material = material;
    }

    public Texture[] getTextures() {
        return material.textures;
    }

    @Override
    public void setMaterial(Material material) {
        this.material = (DiffuseTextureMaterial) material;
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
        return material.textures[0].unit;
    }

    public float[] getTransform() {
        return transform;
    }

    public void setTransform(float[] transform) {
        this.transform = transform;
    }

    @Override
    public Shader getShader() {
        return material.shader;
    }


    //每绘制一帧前都会调用一次
    @Override
    public void adjustContext() {
       // seqFrameParams.setCurrentFrameIndex(1);
        seqFrameParams.increaseCurrentFrameIndex();
    }

    @Override
    public void initContext() {

    }
}
