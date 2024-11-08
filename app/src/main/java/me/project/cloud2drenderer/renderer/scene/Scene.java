package me.project.cloud2drenderer.renderer.scene;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
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

    private int lastContextId = 0;

    Vector<AssetBinding> assetBindings;
    Map<Integer,RenderContext> renderContexts;


    public Scene(Context context){
        canvasController = new CanvasController();
        shaderController = new ShaderController();
        textureController = new TextureController();
        modelController = new ModelController();
        assetLoader = new AssetLoader(context,shaderController,textureController,modelController);
        camera = new Camera();
        assetBindings = new Vector<>();
        renderContexts = new HashMap<>();
        canvasController.enableDepthTest();
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

    public void initRenderContexts() {
        RenderContext.camera = camera;
        for (AssetBinding ab : assetBindings) {
            MaterialBinding mb = ab.materialBinding;
            RenderContext context = ab.context;
            Material material;
            try {
                material = ab.materialBinding.materialClass.newInstance();
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
            context.contextId = ++lastContextId;
            context.initContext();
            renderContexts.put(lastContextId, context);
        }
    }


    public void adjustObjects(){
        for(RenderContext context: renderContexts.values()){
            context.adjustContext();
        }
    }

    public void draw(RenderPipeline[] pipelines){
        for(RenderPipeline pipeline:pipelines){
            pipeline.run(renderContexts.values());
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