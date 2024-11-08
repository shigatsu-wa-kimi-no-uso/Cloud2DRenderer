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
import me.project.cloud2drenderer.renderer.controller.ModelController;
import me.project.cloud2drenderer.renderer.controller.ShaderController;
import me.project.cloud2drenderer.renderer.controller.TextureController;
import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.loader.AssetLoader;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.CommonBinder;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformBindingProcessor;
import me.project.cloud2drenderer.renderer.procedure.pipeline.BlendPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.CommonPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.NonBlendPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.RenderPipeline;
import me.project.cloud2drenderer.renderer.procedure.drawing.ArrayDraw;
import me.project.cloud2drenderer.renderer.procedure.drawing.ElemDraw;

public class Scene {

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

    private Vector<RenderPipeline> pipelines;   //pipelineId to pipeline

    private Map<String,Integer> pipelineMap; // pipeline name to pipelineId
    
    private Map<Integer,Integer> pipelineTaskBatchId; // pipilineId to batchTaskId


    private Vector<AssetBinding> assetBindings;

    private Vector<RenderContext> renderContexts;
    
    private Map<Integer, RenderBatchTask> renderBatchTasks;   // batchTaskId to batchTask

    static class RenderBatchTask {
        public int taskId;
        public int pipelineId;
        public Map<Integer,RenderContext> contexts; //contextId in this batch
    }

    public void initPipeline(String name,RenderPipeline pipeline){
        int pipelineId = ++lastPipelineId;
        pipelines.add(pipeline);
        pipelineMap.put(name,pipelineId);
        RenderBatchTask task = new RenderBatchTask();
        task.taskId = pipelineId;
        task.pipelineId = pipelineId;
        task.contexts = new HashMap<>();
        renderBatchTasks.put(pipelineId,task);
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
        renderBatchTasks = new HashMap<>();
        pipelines = new Vector<>();
        pipelineMap = new HashMap<>();
        canvasController.enableDepthTest();
        initPipeline("common",new CommonPipeline());
        initPipeline("non_blend",new NonBlendPipeline());
        initPipeline("blend",new BlendPipeline());
    }

    public void turnOnBlend(){
        canvasController.enableBlend();
    }

    public void clear(){
        canvasController.clearCanvas(0.2f, 0.3f, 0.3f, 1.0f);
    }
    public void setViewport(int width,int height){
        viewportWidth = width;
        viewportHeight = height;
    }

    public void updateViewport(){
        canvasController.setCanvasSize(viewportWidth,viewportHeight);
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
        Objects.requireNonNull(renderBatchTasks.get(pipelineId)).contexts.put(context.contextId,context);
    }

    public void initRenderContexts() {
        RenderContext.camera = camera;
        for (AssetBinding ab : assetBindings) {
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
                    material.getTextures()[i] = textureController.getTexture(mb.textureNames[i]); //若textureName==null 则返回null
                    material.getTextures()[i].unit = i;
                }
            } else {
                material.getTextures()[0] = Texture.nullTexture();
            }
            material.distribute(); //把数组中的texture对象引用分配到对应的字段
            context.setGLResourceBinder(new CommonBinder());
            determineDrawMethod(context);
            shaderController.bindShaderAttributePointers(material.getShader(), context.loadedModel);
            UniformBindingProcessor.generateShaderUniformSetter(context,material.getShader());
            context.setMaterial(material);
            context.initContext();
            context.contextId = ++lastContextId;
            renderContexts.add(context);
            submitTask(ab.pipelineName,context);
        }
        assetBindings.clear();
    }



    public void adjustObjects(){
        for(RenderContext context: renderContexts){
            context.adjustContext();
        }
    }

    public void draw(){
        for(RenderBatchTask tasks: renderBatchTasks.values()){
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