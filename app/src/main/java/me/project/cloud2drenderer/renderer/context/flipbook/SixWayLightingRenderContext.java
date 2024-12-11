package me.project.cloud2drenderer.renderer.context.flipbook;

import android.opengl.Matrix;

import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.entity.model.shape.Rectangle;
import me.project.cloud2drenderer.renderer.entity.others.light.DistantLight;
import me.project.cloud2drenderer.renderer.entity.others.light.PointLight;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.material.SixWayLighting;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.SequenceFrameParams;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.ShaderUniform;
import me.project.cloud2drenderer.util.MatUtils;

public class SixWayLightingRenderContext extends RenderContext {

    private SequenceFrameParams seqFrameParams;

    private SixWayLighting sixWayLighting;


    private float[] transform;

    private final float[] modelInverse;

    private final float[] modelIT;


    private float[] position;

    private float[] scale;

    private DistantLight distantLight;

    private PointLight pointLight;

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getScale() {
        return scale;
    }

    public void setScale(float[] scale) {
        this.scale = scale;
    }


    public SixWayLightingRenderContext(){
        modelInverse = new float[16];
        modelIT = new float[16];
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
    public void setTransform(float[] transform) {
        this.transform = transform;
        updateModelIT();
    }

    @Override
    @ShaderUniform(uniformName = "uModeling")
    public float[] getTransform(){
        return transform;
    }
    @ShaderUniform(uniformName = "uPointLight",flags = {UniformFlag.IS_STRUCT})
    public PointLight getPointLight() {
        return pointLight;
    }


    @ShaderUniform(uniformName = "uView")
    public float[] getView(){
        return camera.getView();
    }

    @ShaderUniform(uniformName = "uProjection")
    public float[] getProjection(){
        return camera.getProjection();
    }

    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }
    @ShaderUniform(uniformName = "uSeqFrameParams",flags = {UniformFlag.IS_STRUCT})
    public SequenceFrameParams getSeqFrameParams() {
        return seqFrameParams;
    }

    public void setSeqFrameParams(SequenceFrameParams seqFrameParams) {
        this.seqFrameParams = seqFrameParams;
    }

    public void setSixWayLighting(SixWayLighting sixWayLighting) {
        this.sixWayLighting = sixWayLighting;
    }

    @ShaderUniform(uniformName = "uDistantLight",flags = {UniformFlag.IS_STRUCT})
    public DistantLight getDistantLight() {
        return distantLight;
    }

    public void setDistantLight(DistantLight distantLight) {
        this.distantLight = distantLight;
    }

    @ShaderUniform(uniformName = "uFlipBookAlbedo")
    public int getFlipBookAlbedoUnit(){
        return sixWayLighting.getDiffuseTexture().unit;
    }

    @ShaderUniform(uniformName = "uFlipBookLightMap.mapRLT")
    public int getFlipBookLightingRLTUnit(){
        return sixWayLighting.getLightMapA().unit;
    }

    @ShaderUniform(uniformName = "uFlipBookLightMap.mapBBF")
    public int getFlipBookLightingBBFUnit(){
        return sixWayLighting.getLightMapB().unit;
    }

    @ShaderUniform(uniformName = "uFlipBookLightMap.mapRTB")
    public int getFlipBookLightingRTBUnit(){
        return sixWayLighting.getLightMapA().unit;
    }

    @ShaderUniform(uniformName = "uFlipBookLightMap.mapLBF")
    public int getFlipBookLightingLBFUnit(){
        return sixWayLighting.getLightMapB().unit;
    }

    @Override
    public Shader getShader() {
        return sixWayLighting.getShader();
    }

    @Override
    public Texture[] getTextures() {
        return sixWayLighting.getTextures();
    }

    @Override
    public void setMaterial(Material material) {
        sixWayLighting = (SixWayLighting) material;
    }

    @Override
    public void setMaterial(Material[] material) {

    }

    long lastTick;


    @Override
    public void adjustContext() {
       // seqFrameParams.setFrequency(1);
        long thisTick = System.nanoTime();
        float durationInSecond = (thisTick - lastTick)/1E9f;
        lastTick = thisTick;
        float fps = sixWayLighting.getFramesPerSecond();
        seqFrameParams.increaseCurrentFrameIndex(durationInSecond*fps);
        float[] eyePos = camera.getPosition();
        float[] eyePosOS = MatUtils.sub(eyePos,position);
        float[] transform = Rectangle.getBillboardTransform(eyePosOS,position);
        Matrix.scaleM(transform,0,scale[0],scale[1],scale[2]);
        setTransform(transform);
    }



    @Override
    public void initContext() {
        seqFrameParams = new SequenceFrameParams();
        seqFrameParams.setCurrentFrameIndex(0);
        seqFrameParams.setFlipBookShape(sixWayLighting.getShape());
        lastTick = System.nanoTime();
       // seqFrameParams.setFrequency(sixWayLighting.getFramesPerSecond());
    }
}
