package me.project.cloud2drenderer.renderer.context.flipbook;

import android.opengl.Matrix;
import android.util.Log;

import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.entity.model.shape.Rectangle;
import me.project.cloud2drenderer.renderer.entity.others.timer.MillisTimer;
import me.project.cloud2drenderer.renderer.entity.others.timer.NanoTimer;
import me.project.cloud2drenderer.renderer.entity.others.timer.Timer;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.FlipbookConfig;
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

    private static final String tag = SixWayLightingRenderContext.class.getSimpleName();

    private final SequenceFrameParams seqFrameParams = new SequenceFrameParams();

   // private SixWayLighting sixWayLighting;

    private final FlipbookConfig currentFlipbookConfig = new FlipbookConfig();

    private Vector<FlipbookConfig> flipbookConfigs;

    private int takenUnitOffset;

    private int frameCount = 0;

    private int[] totalTakenCnt;

    private final float[] rotation = new float[3];

    private float initialIdleTime;

    private final float[] transform = new float[16];

    private final float[] modelInverse = new float[16];

    private final float[] modelIT = new float[16];

    private final float[] rangeOfDepth = new float[2];

    private final Timer timer = new NanoTimer();

    private float[] cloudAlbedo;

    private DistantLight distantLight;

    private PointLight pointLight;

    private Set<Integer> availableFlipbookIds;

    private final Random random = new Random();

    public int getTotalTakenCnt() {
        return totalTakenCnt[0];
    }

    public void setTotalTakenCnt(int[] totalTakenCnt) {
        this.totalTakenCnt = totalTakenCnt;
    }

    public int getTakenUnitOffset() {
        return takenUnitOffset;
    }

    public void setTakenUnitOffset(int takenUnitOffset) {
        this.takenUnitOffset = takenUnitOffset;
    }

    public float[] getPosition() {
        return currentFlipbookConfig.getPosition();
    }

    public void setPosition(float[] position) {
        currentFlipbookConfig.setPosition(position);
    }

    public float[] getScale() {
        return currentFlipbookConfig.getScale();
    }

    public void setScale(float[] scale) {
        currentFlipbookConfig.setScale(scale);
    }

    public float getInitialIdleTime() {
        return initialIdleTime;
    }

    public void setInitialIdleTime(float initialIdleTime) {
        this.initialIdleTime = initialIdleTime;
    }

    public Set<Integer> getAvailableFlipbookIds() {
        return availableFlipbookIds;
    }

    public void setAvailableFlipbookIds(Set<Integer> availableFlipbookIds) {
        this.availableFlipbookIds = availableFlipbookIds;
    }

    public SixWayLightingRenderContext(){
    }

    public float[] getRangeOfDepth() {
        return rangeOfDepth;
    }

    public void setRangeOfDepth(float lowerBound,float upperBound) {
        this.rangeOfDepth[0] = lowerBound;
        this.rangeOfDepth[1] = upperBound;
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
       // this.transform = transform;
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


    public void setSixWayLighting(SixWayLighting sixWayLighting) {
        currentFlipbookConfig.setMaterial(sixWayLighting);
        //this.sixWayLighting = sixWayLighting;
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
        return getMaterial().getDiffuseTexture().unit;
    }

    @ShaderUniform(uniformName = "uFlipBookLightMap.mapRLT")
    public int getFlipBookLightingRLTUnit(){
        return  getMaterial().getLightMapA().unit;
    }

    @ShaderUniform(uniformName = "uFlipBookLightMap.mapBBF")
    public int getFlipBookLightingBBFUnit(){
        return getMaterial().getLightMapB().unit;
    }

    @ShaderUniform(uniformName = "uFlipBookLightMap.mapRTB")
    public int getFlipBookLightingRTBUnit(){
        return getMaterial().getLightMapA().unit;
    }

    @ShaderUniform(uniformName = "uFlipBookLightMap.mapLBF")
    public int getFlipBookLightingLBFUnit(){
        return getMaterial().getLightMapB().unit;
    }

    @ShaderUniform(uniformName = "uCloudAlbedo")
    public float[] getCloudAlbedo() {
        return cloudAlbedo;
    }

    public void setCloudAlbedo(float[] cloudAlbedo) {
        this.cloudAlbedo = cloudAlbedo;
    }



    @Override
    public Shader getShader() {
        return getMaterial().getShader();
    }

    @Override
    public Texture[] getTextures() {
        return currentFlipbookConfig.getMaterial().getTextures();
    }

    @Override
    public void setMaterial(Material material) {
        currentFlipbookConfig.setMaterial((SixWayLighting) material);
    }

    public SixWayLighting getMaterial(){
        return currentFlipbookConfig.getMaterial();
    }

    @Override
    public void setMaterial(Material[] material) {

    }



    public FlipbookConfig getCurrentFlipbookConfig() {
        return currentFlipbookConfig;
    }

    public void setCurrentFlipbookConfig(int newFlipbookConfigOffset) {
        FlipbookConfig lastConfig = flipbookConfigs.get(takenUnitOffset);
        FlipbookConfig nextConfig = flipbookConfigs.get(newFlipbookConfigOffset);
        flipbookConfigs.set(newFlipbookConfigOffset,lastConfig);
        flipbookConfigs.set(takenUnitOffset,nextConfig);
        sampleFlipbookConfig(currentFlipbookConfig,flipbookConfigs.get(takenUnitOffset));
        seqFrameParams.setFlipBookShape(currentFlipbookConfig.getShape());
        Log.d(tag,name + " current flipbook id : "+ currentFlipbookConfig.getId());
        //printCurrentStatus();
    }

    public Vector<FlipbookConfig> getFlipbookConfigs() {
        return flipbookConfigs;
    }

    public void setFlipbookConfigs(Vector<FlipbookConfig> flipbookConfigs) {
        this.flipbookConfigs = flipbookConfigs;
    }



    void updateShaderContext() {
        float fps = currentFlipbookConfig.getFramesPerSecond();
        float duration = timer.getDurationInSecondsAndReset();
        seqFrameParams.updateCurrentFrameIndex(duration, fps);
        float[] moveDir = currentFlipbookConfig.getMoveDirection();
        currentFlipbookConfig.getPosition()[0] += duration * moveDir[0] * currentFlipbookConfig.getVelocity();
        // float[] eyePos = camera.getPosition();
        // float[] eyePosOS = MatUtils.sub(eyePos, currentFlipbookConfig.getPosition());
        //float[] transform = Rectangle.getBillboardTransform(eyePosOS, currentFlipbookConfig.getPosition());
        MatUtils.setTransform(getTransform(),currentFlipbookConfig.getPosition(), currentFlipbookConfig.getScale(),rotation);
        //    float[] scale = currentFlipbookConfig.getScale();
        //  Matrix.scaleM(transform, 0, scale[0], scale[1], scale[2]);
        updateModelIT();
   //     setTransform(transform);
    }


    boolean isCurrentFlipbookOver(FlipbookConfig config,SequenceFrameParams seqFrameParams){
        return config.getFrameCnt() <= seqFrameParams.getCurrentFrameIndex();
    }



    int getNewFlipbookConfigId() {
        return getRandomInt(getTotalTakenCnt(),flipbookConfigs.size());
    }


    float getRandomFloat(float lowerBound, float upperBound){
        return lowerBound + random.nextFloat()*(upperBound - lowerBound);
    }

    int getRandomInt(int lowerBound, int upperBound){
        return lowerBound + random.nextInt(upperBound - lowerBound);
    }

    void sampleFlipbookConfig(FlipbookConfig sampledConfig,FlipbookConfig config){
        config.copy(sampledConfig);
        sampledConfig.getPosition()[2] = getRandomFloat(rangeOfDepth[0],rangeOfDepth[1]);
        float[] posLB = config.getRefreshPositionLB();
        float[] posUB = config.getRefreshPositionUB();
        float scale = getRandomFloat(config.getScaleLB(),config.getScaleUB());
        float fps = getRandomFloat(config.getFramesPerSecondLB(),config.getFramesPerSecondUB());
        sampledConfig.setFramesPerSecond(fps);
        sampledConfig.getPosition()[0] = getRandomFloat(posLB[0],posUB[0]);
        sampledConfig.getScale()[0] *= scale;
        sampledConfig.getScale()[1] *= scale;
        sampledConfig.setVelocity(getRandomFloat(config.getVelocityLB(),config.getVelocityUB()));
        float heightYCompensation = Rectangle.getLowerBoundY() - Rectangle.getLowerBoundY()* sampledConfig.getScale()[1];
        sampledConfig.getPosition()[1] += heightYCompensation;
    }

    void resetSeqFrame(){
        float idleTime = getRandomFloat(0.0f, 16.0f);
        resetSeqFrame(idleTime);
    }

    void resetSeqFrame(float idleTime){
        seqFrameParams.resetCurrentFrameIndex();
        Log.d(tag, name + " idle for " + idleTime + " seconds");
        seqFrameParams.setIdleTime(currentFlipbookConfig.getFramesPerSecond(), idleTime);
    }

    void updateFlipbookConfig() {
        setCurrentFlipbookConfig(getNewFlipbookConfigId());
    }



    void printCurrentStatus(){
        float[] pos = getPosition();
        float[] scale = getScale();
        String text = String.format(Locale.getDefault(),
                "%s current flipbook id: %d, current position: (%.2f,%.2f,%.2f), scale: (%.2f,%.2f,%.2f), color: (%.2f,%.2f,%.2f),velocity: %.5f, fps: %.2f",
                name,currentFlipbookConfig.getId(),pos[0],pos[1],pos[2],
                scale[0],scale[1],scale[2],cloudAlbedo[0],cloudAlbedo[1],cloudAlbedo[2],
                currentFlipbookConfig.getVelocity(),currentFlipbookConfig.getFramesPerSecond());
        Log.w(tag, text);
    }

    @Override
    public void adjustContext() {
        if(isCurrentFlipbookOver(currentFlipbookConfig,seqFrameParams)) {
            updateFlipbookConfig();
            resetSeqFrame();
        }
        updateShaderContext();
        /*if(frameCount % 180 == 0){
            printCurrentStatus();
        }
        frameCount++;*/
    }



    @Override
    public void initContext() {
        resetSeqFrame(initialIdleTime);
        timer.startTick();
    }
}
