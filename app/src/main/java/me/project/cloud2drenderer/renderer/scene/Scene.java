package me.project.cloud2drenderer.renderer.scene;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import me.project.cloud2drenderer.renderer.entity.AssetBinding;
import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.controller.CanvasController;
import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.procedure.pipeline.BlendPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.CommonPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.MonoObjectBlendPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.NonBlendPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.RenderPipeline;

public class Scene {

    private static final String tag = Scene.class.getSimpleName();



    private final CanvasController canvasController;

    //private final AssetLoader assetLoader;

  /*  private final ShaderController shaderController;

    private final TextureController textureController;

    private final ModelController modelController;*/

    public final Camera camera;

    private int viewportWidth;

    private int viewportHeight;


    private int lastContextId = -1;

    private int lastPipelineId = -1;

    private final Vector<RenderPipeline> pipelines;   //pipelineId to pipeline

    private final Map<String,Integer> pipelineMap; // pipeline name to pipelineId
    
    private Map<Integer,Integer> pipelineTaskBatchId; // pipilineId to batchTaskId

    private final Vector<AssetBinding> assetBindings;

    private final Vector<MaterialBinding> materialBindings;

    private final Vector<RenderContext> renderContexts;
    
    private final Map<Integer, RenderBatch> renderBatches;   // batchTaskId to batchTask

    static class RenderBatch {
        public int taskId;
        public int pipelineId;
        public Map<Integer,RenderContext> contexts; //contextId in this batch
    }

    private final SceneObjectLoader objectLoader;


    public void initPipeline(String name,RenderPipeline pipeline){
        int pipelineId = ++lastPipelineId;
        pipelines.add(pipeline);
        pipelineMap.put(name,pipelineId);
        RenderBatch task = new RenderBatch();
        task.taskId = pipelineId;
        task.pipelineId = pipelineId;
        task.contexts = new HashMap<>();
        renderBatches.put(pipelineId,task);
    }


    public Scene(Context context){
        materialBindings = new Vector<>();
        canvasController = new CanvasController();
        objectLoader = new SceneObjectLoader(context);
        camera = new Camera();
        assetBindings = new Vector<>();
        renderContexts = new Vector<>();
        renderBatches = new HashMap<>();
        pipelines = new Vector<>();
        pipelineMap = new HashMap<>();
        canvasController.enableDepthTest();
        initPipeline("common",new CommonPipeline());
        initPipeline("non_blend",new NonBlendPipeline());
        initPipeline("blend",new BlendPipeline());
        initPipeline("mono_object_blend",new MonoObjectBlendPipeline());
    }


    public void enableBlend(){
        canvasController.enableBlend();
    }

    public void enableCullFace(){
        canvasController.enableCullFace();
    }

    public void clear(float r,float g,float b,float a){
        canvasController.clearCanvas(r,g,b,a);
    }

    public void setClearColor(float r,float g,float b,float a){
        canvasController.clearColor(r,g,b,a);
    }

    public void clear(){
        canvasController.clearCanvas();
    }

    public void setViewport(int width,int height){
        viewportWidth = width;
        viewportHeight = height;
    }

    public int getViewportWidth(){
        return viewportWidth;
    }

    public int getViewportHeight(){
        return viewportHeight;
    }

    public void updateViewport(){
        canvasController.setCanvasSize(viewportWidth,viewportHeight);
    }

    public void loadMaterial(MaterialBinding materialBinding){
        objectLoader.loadMaterial(materialBinding);
        materialBindings.add(materialBinding);
    }

    public void load(@NonNull AssetBinding assetBinding) {
        objectLoader.loadAsset(assetBinding);
        assetBindings.add(assetBinding);
    }

    private void submitTask(String pipelineName,RenderContext context){
        int pipelineId = pipelineMap.get(pipelineName);
        Objects.requireNonNull(renderBatches.get(pipelineId)).contexts.put(context.contextId,context);
    }

    public void initRenderContexts() {
        RenderContext.camera = camera;
        for (AssetBinding ab : assetBindings) {
            RenderContext context = objectLoader.createRenderContext(ab);
            context.contextId = ++lastContextId;
            context.initContext();
            renderContexts.add(context);
            submitTask(ab.pipelineName, context);
        }
        assetBindings.clear();
    }

    public void adjustObjects(){
        for(RenderContext context: renderContexts){
            context.adjustContext();
        }
    }


    public void loadRenderContextsToPipeline(){
        for(RenderBatch tasks: renderBatches.values()){
            RenderPipeline pipeline = pipelines.get(tasks.pipelineId);
            pipeline.setContexts(tasks.contexts.values());
        }
    }

    public void draw(){
        for(RenderBatch tasks: renderBatches.values()){
            RenderPipeline pipeline = pipelines.get(tasks.pipelineId);
            pipeline.beforeTask();
            pipeline.run();
            pipeline.afterTask();
        }
    }



/*

    public void loadMaterials(@NonNull Collection<MaterialBinding> mbs) {
        for(MaterialBinding mb:mbs){
            assetLoader.loadShaderScript(mb.shaderName);
        }
        Vector<Thread> threads = new Vector<>();
        for(MaterialBinding mb:mbs){
            if(mb.textureNames != null){
                Thread thread = asyncLoadTextureBitmap(mb);
                threads.add(thread);
            }
        }
        for(Thread thread:threads){
            try {
                Log.d(tag,"waiting for thread "+thread.getId());
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for(MaterialBinding mb:mbs){
            if(mb.textureNames != null){
                for(String textureName : mb.textureNames){
                    Log.d(tag,"creating texture " + textureName + "...");
                    if(assetLoader.loadTexture(textureName)!=null) {
                        Log.d(tag, "create texture " + textureName + " success.");
                    }else {
                        Log.e(tag,"create texture " + textureName + " failed.");
                    }
                }
            }
            mb.material = getMaterial(mb);
        }
    }*/

/*    @NonNull
    private Thread asyncLoadTextureBitmap(MaterialBinding mb) {
        Thread thread = new Thread(() -> {
            for(String textureName : mb.textureNames){
                Log.d(tag,"loading texture bitmap " + textureName + "...");
                if(assetLoader.loadTextureBitmap(textureName)!=null) {
                    Log.d(tag, "load texture bitmap " + textureName + " success.");
                }else {
                    Log.e(tag,"load texture bitmap " + textureName + " failed.");
                }
            }
        });
        thread.start();
        return thread;
    }*/

/*
    @Deprecated
    private void loadTextures(MaterialBinding mb){
        for (String textureName : mb.textureNames) {
            Log.d(tag, "loading texture " + textureName + "...");
            if (assetLoader.loadTexture(textureName) != null) {
                Log.d(tag, "loaded texture " + textureName + " success.");
            } else {
                Log.e(tag, "loaded texture " + textureName + " failed.");
            }
        }
    }

    @Deprecated
    private void injectTextures(MaterialBinding mb){
        for (int i = 0; i < mb.textureNames.length; i++) {
            Texture texture = textureController.getTexture(mb.textureNames[i]); //若textureName==null 则返回null
            texture.unit = i + 1;
            mb.textureSetters[i].setTexture(texture);
        }
    }

    @Deprecated
    public void loadMaterial(@NonNull MaterialBinding mb) {
        assetLoader.loadShaderScript(mb.shaderName);
        if(!mb.deferredTextureLoading) {
            if (mb.textureNames != null) {
                loadTextures(mb);
            }
        }else {
            Log.i(tag, "texture loading deferred.");
        }
        mb.material = getMaterial(mb);
    }

    @Deprecated
    private void determineDrawMethod(@NonNull RenderContext context){
        if(context.loadedModel.elemBased){
            context.setDrawMethod(new ElemDraw());
        } else {
            context.setDrawMethod(new ArrayDraw());
        }
    }



    @Deprecated
    private Material getMaterial(MaterialBinding mb){
        Material material = mb.material;
        material.setShader(shaderController.getShaderProgram(mb.shaderName));
        if(!mb.deferredTextureLoading) {
            if (mb.textureNames != null) {
                injectTextures(mb);
            }
        }else {
            TextureLoader loader = new TextureLoader() {
                @Override
                public void load() {

                }

                @Override
                public void inject() {
                    injectTextures(mb);
                }
            };
            material.setTextureLoader(loader);
        }
        return material;
    }

    @Deprecated
    private void initRenderContext(AssetBinding ab) {
        Log.i(tag,"initializing render context:"+ab.context.name);
        RenderContext.camera = camera;
        RenderContext context = ab.context;
        context.loadedModel = modelController.getLoadedModel(ab.modelName);
        Material material;
        if(ab.materialBinding!=null){
            material = ab.materialBinding.material;
            context.setMaterial(material);
        }else{
            material = context.getMaterial();
        }

        context.setGLResourceBinder(new CommonBinder());
        determineDrawMethod(context);
       // shaderController.bindShaderAttributePointers(material.getShader(), context.loadedModel);
        if(ab.transform!=null){
            context.setTransform(ab.transform);
        }
        context.initContext();
        AttributeBindingProcessor.generateShaderAttributeSetters(context,context.loadedModel,material.getShader());
        UniformBindingProcessor.generateShaderUniformSetters(context, material.getShader());
        context.contextId = ++lastContextId;
        renderContexts.add(context);
        submitTask(ab.pipelineName, context);
    }


 */

}

