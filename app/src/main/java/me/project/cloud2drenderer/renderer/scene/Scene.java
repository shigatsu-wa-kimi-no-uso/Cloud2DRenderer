package me.project.cloud2drenderer.renderer.scene;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import me.project.cloud2drenderer.renderer.entity.AssetBinding;
import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.controller.CanvasController;
import me.project.cloud2drenderer.renderer.controller.ModelController;
import me.project.cloud2drenderer.renderer.controller.ShaderController;
import me.project.cloud2drenderer.renderer.controller.TextureController;
import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.loader.AssetLoader;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.CommonBinder;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.AttributeBindingProcessor;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.UniformBindingProcessor;
import me.project.cloud2drenderer.renderer.procedure.pipeline.BlendPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.CommonPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.NonBlendPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.RenderPipeline;
import me.project.cloud2drenderer.renderer.procedure.drawing.ArrayDraw;
import me.project.cloud2drenderer.renderer.procedure.drawing.ElemDraw;

public class Scene {

    private static final String tag = Scene.class.getSimpleName();
    private final AssetLoader assetLoader;

    private final CanvasController canvasController;

    private final ShaderController shaderController;

    private final TextureController textureController;

    private final ModelController modelController;

    public final Camera camera;

    private int viewportWidth;

    private int viewportHeight;


    private int lastContextId = -1;

    private int lastPipelineId = -1;

    private final Vector<RenderPipeline> pipelines;   //pipelineId to pipeline

    private final Map<String,Integer> pipelineMap; // pipeline name to pipelineId
    
    private Map<Integer,Integer> pipelineTaskBatchId; // pipilineId to batchTaskId


    private final Vector<AssetBinding> assetBindings;

    private final Vector<RenderContext> renderContexts;
    
    private final Map<Integer, RenderBatch> renderBatches;   // batchTaskId to batchTask

    static class RenderBatch {
        public int taskId;
        public int pipelineId;
        public Map<Integer,RenderContext> contexts; //contextId in this batch
    }



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
        canvasController = new CanvasController();
        shaderController = new ShaderController();
        textureController = new TextureController();
        modelController = new ModelController();
        assetLoader = new AssetLoader(context,shaderController,textureController,modelController);
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

    public AssetLoader getAssetLoader(){
        return assetLoader;
    }
    public void load(@NonNull AssetBinding assetBinding) {
        MaterialBinding mb = assetBinding.materialBinding;
        assetLoader.loadShaderScript(mb.shaderName);
        if(mb.textureNames != null){
            for(String textureName : mb.textureNames){
                assetLoader.loadTexture(textureName);
            }
        }
        assetLoader.loadModel(assetBinding.modelName);
        assetBindings.add(assetBinding);
    }


    public void determineDrawMethod(@NonNull RenderContext context){
        if(context.loadedModel.elemBased){
            context.setDrawMethod(new ElemDraw());
        } else {
            context.setDrawMethod(new ArrayDraw());
        }
    }


    private void submitTask(String pipelineName,RenderContext context){
        int pipelineId = pipelineMap.get(pipelineName);
        Objects.requireNonNull(renderBatches.get(pipelineId)).contexts.put(context.contextId,context);
    }

    private void initRenderContext(AssetBinding ab) {
        Log.i(tag,"initializing render context:"+ab.context.name);
        RenderContext.camera = camera;
        MaterialBinding mb = ab.materialBinding;
        RenderContext context = ab.context;
        Material material;
        try {
            material = ab.materialBinding.material;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        material.setShader(shaderController.getShaderProgram(mb.shaderName));
        context.loadedModel = modelController.getLoadedModel(ab.modelName);

        if (mb.textureNames != null) {
            for (int i = 0; i < mb.textureNames.length; i++) {
                Texture texture = textureController.getTexture(mb.textureNames[i]); //若textureName==null 则返回null
                texture.unit = i + 1;
                mb.textureSetters[i].setTexture(texture);
            }
        }
//        } else {
//            material.getTextures()[0] = Texture.nullTexture();
//        }

        context.setGLResourceBinder(new CommonBinder());
        determineDrawMethod(context);
       // shaderController.bindShaderAttributePointers(material.getShader(), context.loadedModel);
        context.setMaterial(material);
        context.setTransform(ab.transform);
        context.initContext();
        AttributeBindingProcessor.generateShaderAttributeSetters(context,context.loadedModel,material.getShader());
        UniformBindingProcessor.generateShaderUniformSetters(context, material.getShader());
        context.contextId = ++lastContextId;
        renderContexts.add(context);
        submitTask(ab.pipelineName, context);
    }

    public void initRenderContexts() {
        RenderContext.camera = camera;
        for (AssetBinding ab : assetBindings) {
            initRenderContext(ab);
        }
        assetBindings.clear();
    }

    public void freeAllTextureImages(){
        assetLoader.freeAllBitmaps();
    }

    public void adjustObjects(){
        for(RenderContext context: renderContexts){
            context.adjustContext();
        }
    }

    public void draw(){
        for(RenderBatch tasks: renderBatches.values()){
            RenderPipeline pipeline = pipelines.get(tasks.pipelineId);
            pipeline.beforeTask();
            for (RenderContext context : tasks.contexts.values()){
                pipeline.run(context);
            }
            pipeline.afterTask();
        }
    }

}

//0.22  0  0.97  0
//0     1     0  0
//-0.97 0  0.22  0
//0     0     0  1

//1             0   -4.37*10^-8     0
//0             1             0     0
//4.37*10^-8    0             1     0
//0             0             0     1