package me.project.cloud2drenderer.renderer.context.flipbook;

import me.project.cloud2drenderer.renderer.entity.others.flipbook.SequenceFrameStatus;

import android.opengl.Matrix;
import android.util.Log;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.entity.model.shape.Rectangle;
import me.project.cloud2drenderer.renderer.entity.others.timer.NanoTimer;
import me.project.cloud2drenderer.renderer.entity.others.timer.Timer;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.FlipBookConfig;
import me.project.cloud2drenderer.renderer.entity.others.light.DistantLight;
import me.project.cloud2drenderer.renderer.entity.others.light.PointLight;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.material.SixWayLighting;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.SequenceFrameParams;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.loader.AssetLoader;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.material.TextureLoader;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.ShaderVariableSetterWrapper;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformVar;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;
import me.project.cloud2drenderer.util.MatUtils;

public class SixWayLightingRenderContext extends RenderContext {

    private static final String tag = SixWayLightingRenderContext.class.getSimpleName();

    private final SequenceFrameParams seqFrameParams = new SequenceFrameParams();

   // private SixWayLighting sixWayLighting;

    private final FlipBookConfig currentFlipBookConfig = new FlipBookConfig();

    private Vector<FlipBookConfig> flipBookConfigs;


    private AssetLoader assetLoader;

    private int nextFlipbookId;

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

    private static DistantLight distantLight;

    private static final Vector<ShaderVariableSetterWrapper> staticUniformAssignments = new Vector<>(0);

    private static boolean distantLightUpdated = false;

    private PointLight pointLight;

    private Set<Integer> availableFlipbookIds;

    public static class ManualCommitUniforms{
        @ShaderUniform(uniformName = "uModelIT",flags = {})
        public final UniformVar<float[]> uModelITWrapper = new UniformVar<>();

        @ShaderUniform(uniformName = "uView",flags = {})
        public final UniformVar<float[]> uViewWrapper = new UniformVar<>();

        @ShaderUniform(uniformName = "uProjection",flags = {})
        public final UniformVar<float[]> uProjectionWrapper = new UniformVar<>();

        @ShaderUniform(uniformName = "uModeling",flags = {})
        public final UniformVar<float[]> uTransformWrapper = new UniformVar<>();

        @ShaderUniform(uniformName = "uFlipBookLightMap.mapLBF",flags = {})
        public final UniformVar<Integer>  uFlipBookLightMapLBFWrapper = new UniformVar<>();

        @ShaderUniform(uniformName = "uFlipBookLightMap.mapRTB",flags = {})
        public final UniformVar<Integer>  uFlipBookLightMapRTBWrapper = new UniformVar<>();

        @ShaderUniform(uniformName = "uCloudAlbedo",flags = {})
        public final UniformVar<float[]> uCloudAlbedoWrapper = new UniformVar<>();

        @ShaderUniform(uniformName = "uFlipBookAlbedo",flags = {})
        public final UniformVar<Integer> uFlipBookAlbedoWrapper = new UniformVar<>();

    }

    @Override
    public void applyUniformAssignments(){
        super.applyUniformAssignments();
        if(distantLightUpdated){
            for(ShaderVariableSetterWrapper wrapper: staticUniformAssignments){
                wrapper.apply();
            }
            distantLightUpdated = false;
        }
    }


    @ShaderUniform(flags = UniformFlag.IS_STRUCT)
    public final ManualCommitUniforms manualCommits = new ManualCommitUniforms();

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
        return currentFlipBookConfig.getPosition();
    }

    public void setPosition(float[] position) {
        currentFlipBookConfig.setPosition(position);
    }

    public float[] getScale() {
        return currentFlipBookConfig.getScale();
    }

    public void setScale(float[] scale) {
        currentFlipBookConfig.setScale(scale);
    }

    public float getInitialIdleTime() {
        return initialIdleTime;
    }


    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public void setAssetLoader(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
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

   // @ShaderUniform(uniformName = "uModelIT")
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
   // @ShaderUniform(uniformName = "uModeling")
    public float[] getTransform(){
        return transform;
    }

    @ShaderUniform(uniformName = "uPointLight",flags = {UniformFlag.IS_STRUCT})
    public PointLight getPointLight() {
        return pointLight;
    }


   // @ShaderUniform(uniformName = "uView")
    public float[] getView(){
        return camera.getView();
    }

  //  @ShaderUniform(uniformName = "uProjection")
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
        currentFlipBookConfig.setMaterial(sixWayLighting);
        //this.sixWayLighting = sixWayLighting;
    }

    @ShaderUniform(uniformName = "uDistantLight",flags = {UniformFlag.IS_STRUCT})
    public DistantLight getDistantLight() {
        return distantLight;
    }

    public static void setDistantLight(DistantLight light) {
        distantLight = light;
    }


    public static void updateDistinctLightToShader(){
        distantLightUpdated = true;
    }



  //  @ShaderUniform(uniformName = "uFlipBookAlbedo")
    public int getFlipBookAlbedoUnit(){
        return getMaterial().getDiffuseTexture().unit;
    }

   // @ShaderUniform(uniformName = "uFlipBookLightMap.mapRLT")
    public int getFlipBookLightingRLTUnit(){
        return  getMaterial().getLightMapA().unit;
    }

  //  @ShaderUniform(uniformName = "uFlipBookLightMap.mapBBF")
    public int getFlipBookLightingBBFUnit(){
        return getMaterial().getLightMapB().unit;
    }

  //  @ShaderUniform(uniformName = "uFlipBookLightMap.mapRTB")
    public int getFlipBookLightingRTBUnit(){
        return getMaterial().getLightMapA().unit;
    }

  //  @ShaderUniform(uniformName = "uFlipBookLightMap.mapLBF")
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
        return currentFlipBookConfig.getMaterial().getTextures();
    }

    @Override
    public void setMaterial(Material material) {
        currentFlipBookConfig.setMaterial((SixWayLighting) material);
    }

    public SixWayLighting getMaterial(){
        return currentFlipBookConfig.getMaterial();
    }

    @Override
    public void setMaterial(Material[] material) {

    }

    private void commitFlipBookTextureUpdates(){
        manualCommits.uFlipBookAlbedoWrapper.setValue(getFlipBookAlbedoUnit());
        manualCommits.uFlipBookLightMapLBFWrapper.setValue(getFlipBookLightingLBFUnit());
        manualCommits.uFlipBookLightMapRTBWrapper.setValue(getFlipBookLightingRTBUnit());
        commitUniformAssignment(manualCommits.uFlipBookAlbedoWrapper);
        commitUniformAssignment(manualCommits.uFlipBookLightMapLBFWrapper);
        commitUniformAssignment(manualCommits.uFlipBookLightMapRTBWrapper);
    }

    public FlipBookConfig getCurrentFlipbookConfig() {
        return currentFlipBookConfig;
    }

    public void setCurrentFlipbookConfig(int newFlipbookConfigOffset) {
        FlipBookConfig lastConfig = flipBookConfigs.get(takenUnitOffset);
        FlipBookConfig nextConfig = flipBookConfigs.get(newFlipbookConfigOffset);
        flipBookConfigs.set(newFlipbookConfigOffset,lastConfig);
        flipBookConfigs.set(takenUnitOffset,nextConfig);
        sampleFlipbookConfig(currentFlipBookConfig, flipBookConfigs.get(takenUnitOffset));
        seqFrameParams.setFlipBookShape(currentFlipBookConfig.getShape());
        Log.d(tag,name + " current flipbook id : "+ currentFlipBookConfig.getId());
        //printCurrentStatus();
    }

    public Vector<FlipBookConfig> getFlipBookConfigs() {
        return flipBookConfigs;
    }

    public void setFlipBookConfigs(Vector<FlipBookConfig> flipBookConfigs) {
        this.flipBookConfigs = flipBookConfigs;
    }


    void updateFrameIndex(float durationInSeconds,float fps){
        seqFrameParams.updateCurrentFrameIndex(durationInSeconds, fps);
    }

    void setFrameIndex(float frameIndex){
        seqFrameParams.setCurrentFrameIndex(frameIndex);
    }



    void updateTransform(float durationInSeconds) {

        //float[] moveDir = currentFlipbookConfig.getMoveDirection();
       // currentFlipbookConfig.getPosition()[0] += durationInSeconds * moveDir[0] * currentFlipbookConfig.getVelocity();
        // float[] eyePos = camera.getPosition();
        // float[] eyePosOS = MatUtils.sub(eyePos, currentFlipbookConfig.getPosition());
        //float[] transform = Rectangle.getBillboardTransform(eyePosOS, currentFlipbookConfig.getPosition());
        MatUtils.setSimpleTransform(getTransform(), currentFlipBookConfig.getPosition(), currentFlipBookConfig.getScale());
        //    float[] scale = currentFlipbookConfig.getScale();
        //  Matrix.scaleM(transform, 0, scale[0], scale[1], scale[2]);
        updateModelIT();
        commitUniformAssignment(manualCommits.uTransformWrapper);
        commitUniformAssignment(manualCommits.uModelITWrapper);
   //     setTransform(transform);
    }


    boolean isFlipbookOver(FlipBookConfig config, SequenceFrameParams seqFrameParams){
        return config.getFrameCnt() <= seqFrameParams.getCurrentFrameIndex();
    }

    boolean isFlipbookIdle(SequenceFrameParams seqFrameParams){
        return seqFrameParams.isCurrentFrameIndexBelowZero();
    }

    public int getNextFlipbookId() {
        return nextFlipbookId;
    }

    public void setNextFlipbookId(int nextFlipbookId) {
        this.nextFlipbookId = nextFlipbookId;
    }

    void refreshNextFlipbookConfigId() {
        setNextFlipbookId(getRandomInt(getTotalTakenCnt(), flipBookConfigs.size()));
    }


    float getRandomFloat(float lowerBound, float upperBound){
        return lowerBound + random.nextFloat()*(upperBound - lowerBound);
    }

    int getRandomInt(int lowerBound, int upperBound){
        return lowerBound + random.nextInt(upperBound - lowerBound);
    }

    void sampleFlipbookConfig(FlipBookConfig sampledConfig, FlipBookConfig config){
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
        float idleTime = getRandomFloat(0.0f, 8.0f);
        resetSeqFrame(idleTime);
    }

    void resetSeqFrame(float idleTime){
        seqFrameParams.resetCurrentFrameIndex();
        Log.d(tag, name + " idle for " + idleTime + " seconds");
        seqFrameParams.setIdleTime(currentFlipBookConfig.getFramesPerSecond(), idleTime);
    }

    void updateFlipbookConfig() {
        setCurrentFlipbookConfig(nextFlipbookId);
        refreshNextFlipbookConfigId();
    }



    void printCurrentStatus(){
        float[] pos = getPosition();
        float[] scale = getScale();
        String text = String.format(Locale.getDefault(),
                "%s current flipbook id: %d, current position: (%.2f,%.2f,%.2f), scale: (%.2f,%.2f,%.2f), color: (%.2f,%.2f,%.2f),velocity: %.5f, fps: %.2f, frame id: %.2f",
                name, currentFlipBookConfig.getId(),pos[0],pos[1],pos[2],
                scale[0],scale[1],scale[2],cloudAlbedo[0],cloudAlbedo[1],cloudAlbedo[2],
                currentFlipBookConfig.getVelocity(), currentFlipBookConfig.getFramesPerSecond(),
                seqFrameParams.getCurrentFrameIndex());
        Log.w(tag, text);
    }

    void loadMaterial(Material material){
        TextureLoader[] loaders = material.getTextureLoaders();
        int current = material.getLoadedTextureCount();
        if(current == loaders.length){
            material.setFullyLoaded(true);
            updateTransform(0);
            commitFlipBookTextureUpdates();
            timer.reset();
            return;
        }
        TextureLoader loader = loaders[current];
        switch (loader.getStatus()){
            case NOT_LOADED:
                loader.asyncLoad();
                zeroTransform();
                break;
            case LOADING:
                break;
            case LOADED:
                loader.create();
                material.increaseLoadedTextureCount();
                break;
        }
    }

    void zeroTransform(){
        Arrays.fill(transform, 0);
        commitUniformAssignment(manualCommits.uTransformWrapper);
    }

    void switchSequenceFrameStatus(SequenceFrameParams seqFrameParams,SequenceFrameStatus newStatus) {
        SequenceFrameStatus oldStatus = seqFrameParams.getStatus();
        if(oldStatus == newStatus){
            return;
        }
        seqFrameParams.setStatus(newStatus);
       /* String text = String.format(Locale.getDefault(),
                "%s flipbook id: %d, switched status form %s to %s",
                name, currentFlipBookConfig.getId(),oldStatus.name(),newStatus.name());*/
    //    Log.i(tag, text);
    }



    SequenceFrameStatus updateSequenceFrameStatus() {
        Material material = getMaterial();
        SequenceFrameStatus oldStatus = seqFrameParams.getStatus();
        switch (oldStatus) {
            case PLAYING:
                if (isFlipbookOver(currentFlipBookConfig, seqFrameParams)) {
                    switchSequenceFrameStatus(seqFrameParams, SequenceFrameStatus.FINISHED);
                }
                break;
            case LOADING:
                if (material.isFullyLoaded()) {
                    switchSequenceFrameStatus(seqFrameParams, SequenceFrameStatus.PREPARED);
                }
                break;
            case IDLE:
                if (!isFlipbookIdle(seqFrameParams)) {
                    switchSequenceFrameStatus(seqFrameParams, SequenceFrameStatus.LOADING);
                }
                break;
            case PREPARED:
                switchSequenceFrameStatus(seqFrameParams, SequenceFrameStatus.PLAYING);
                break;
            case FINISHED:
                switchSequenceFrameStatus(seqFrameParams, SequenceFrameStatus.IDLE);
                break;
        }

        return oldStatus;
    }




    float[] getDirection(float time){
        float[] point = {0,0,0};
        float scalar = 0.00000005f;
     //   float[] point2 = {(float)Math.sin(scalar * time),-1,(float)Math.cos(scalar * time)};
    //    float[] d = point2;
      //  String t = String.format(Locale.getDefault(),"dir:(%.2f,%.2f,%.2f)",d[0],d[1],d[2]);
       // Log.w(tag,t);
        float[] rotation = MatUtils.newRotationMatrix(50,20,55);
        float[] dir = {0,0,-1,0};
        dir = MatUtils.matVecMultiply(rotation,dir,4);
        double theta = Math.toRadians(time*scalar);
        float[] point2 ={(float)Math.sin(theta),0,(float)Math.cos(theta)};
        return MatUtils.normalized(rotation);
    }

    @Override
    public void adjustContext() {
        updateSequenceFrameStatus();
        SequenceFrameStatus currentStatus = seqFrameParams.getStatus();
        Material material = getMaterial();
        float fps = currentFlipBookConfig.getFramesPerSecond();
        float duration = timer.getDurationInSecondsAndReset();
        //float[] d = getDirection(timer.getTick());
       // distantLight.setDirection(d);

       // commitUniformAssignment(distantLight.manualCommits.directionWrapper);
        switch (currentStatus){
            case IDLE:
                updateFrameIndex(duration,fps);
                break;
            case PREPARED:
                updateTransform(duration);
                commitFlipBookTextureUpdates();
                break;
            case PLAYING:
                updateTransform(duration); //需要更新transform, 因为每个context对应的transform均不同
                updateFrameIndex(duration,fps);
            //    setFrameIndex(0);
                break;
            case FINISHED:
                zeroTransform();
                resetSeqFrame();
                updateFlipbookConfig();
                break;
            case LOADING:
                zeroTransform();
                loadMaterial(material);
                //context切换时，不需要更新，因为材质相同，shader相同，对应的texture的unit号相同
                //只要bind texture的目标不同，即可切换
                //当shader不同，或material不同时，才需要更新shader对应的texture unit！！

                timer.reset();
                break;
        }
        /*
        if(isFlipbookIdle(seqFrameParams)){
            switchSequenceFrameStatus(seqFrameParams,SequenceFrameStatus.IDLE);
            updateFrameIndex(duration,fps);
        }else if(material.isFullyLoaded()){
            switchSequenceFrameStatus(seqFrameParams,SequenceFrameStatus.PLAYING);
            if(isFlipbookOver(currentFlipBookConfig,seqFrameParams)) {
                updateFlipbookConfig();
                resetSeqFrame();
                updateTransform(duration);
                commitFlipBookTextureUpdates();
            }else {
                updateFrameIndex(duration,fps);
            }
        }else {
            switchSequenceFrameStatus(SequenceFrameStatus.LOADING);
            loadMaterial(material);
            timer.reset();
        }*/

      /*  if(frameCount % 180 == 0){
            printCurrentStatus();
        }
        frameCount++;*/
    }



    @Override
    public void initContext() {
        resetSeqFrame(initialIdleTime);
        refreshNextFlipbookConfigId();
        timer.startTick();
        zeroTransform();

        manualCommits.uProjectionWrapper.setValue(camera.getProjection());
        manualCommits.uViewWrapper.setValue(camera.getView());
        manualCommits.uTransformWrapper.setValue(transform);
        manualCommits.uModelITWrapper.setValue(modelIT);
        manualCommits.uCloudAlbedoWrapper.setValue(cloudAlbedo);
        commitUniformAssignment(manualCommits.uProjectionWrapper);
        commitUniformAssignment(manualCommits.uViewWrapper);
        commitUniformAssignment(distantLight.manualCommits.directionWrapper);
        commitUniformAssignment(distantLight.manualCommits.intensityWrapper);
        commitUniformAssignment(manualCommits.uCloudAlbedoWrapper);
        if(staticUniformAssignments.size() >= 2){
            return;
        }
        staticUniformAssignments.add(distantLight.manualCommits.directionWrapper.getUniformSetterWrapper());
        staticUniformAssignments.add(distantLight.manualCommits.intensityWrapper.getUniformSetterWrapper());
    }
}
