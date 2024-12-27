package me.project.cloud2drenderer.renderer.scene;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Vector;

import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.controller.ModelController;
import me.project.cloud2drenderer.renderer.controller.ShaderController;
import me.project.cloud2drenderer.renderer.controller.TextureController;
import me.project.cloud2drenderer.renderer.entity.AssetBinding;
import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.loader.AssetLoader;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.CommonBinder;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.buffer.VertexBufferBinder;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.material.LoadStatus;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.material.TextureBinder;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.material.TextureLoader;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.AttributeBindingProcessor;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.ShaderBinder;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformBindingProcessor;
import me.project.cloud2drenderer.renderer.procedure.drawing.ArrayDraw;
import me.project.cloud2drenderer.renderer.procedure.drawing.ElemDraw;

public class SceneObjectLoader {

    private static final String tag = Scene.class.getSimpleName();

    private final AssetLoader assetLoader;

    private final ShaderController shaderController;

    private final TextureController textureController;

    private final ModelController modelController;

    private final Vector<AssetBinding> assetBindings;

    public SceneObjectLoader(Context context){

        shaderController = new ShaderController();
        textureController = new TextureController();
        modelController = new ModelController();
        assetLoader = new AssetLoader(context,shaderController,textureController,modelController);
        assetBindings = new Vector<>();


    }

    public void loadAsset(@NonNull AssetBinding assetBinding) {
        if(assetBinding.materialBinding!=null){
            loadMaterial(assetBinding.materialBinding);
        }
        assetLoader.loadModel(assetBinding.modelName);
        assetBindings.add(assetBinding);
    }

    private void loadTextures(MaterialBinding mb){
        for (String textureName : mb.textureNames) {
            Log.d(tag, "loading texture " + textureName + "...");
            if (assetLoader.loadTexture(textureName) != null) {
                Log.d(tag, "load texture " + textureName + " success.");
            } else {
                Log.e(tag, "load texture " + textureName + " failed.");
            }
        }
    }

    private void injectTextures(MaterialBinding mb){
        for (int i = 0; i < mb.textureNames.length; i++) {
            Texture texture = textureController.getTexture(mb.textureNames[i]); //若textureName==null 则返回null
            texture.unit = i + 1;
            mb.textureSetters[i].setTexture(texture);
        }
    }


    public void loadMaterial(@NonNull MaterialBinding mb) {
        assetLoader.loadShaderScript(mb.shaderName);
        if(!mb.deferredTextureLoading) {
            if (mb.textureNames != null) {
                loadTextures(mb);
            }
        }else {
            Log.i(tag, "texture loading deferred.");
        }
        mb.material = createMaterial(mb);
    }


    private void determineDrawMethod(@NonNull RenderContext context){
        if(context.loadedModel.elemBased){
            context.setDrawMethod(new ElemDraw());
        } else {
            context.setDrawMethod(new ArrayDraw());
        }
    }


    private Material createMaterial(MaterialBinding mb){
        Material material = mb.material;
        material.setShader(shaderController.getShaderProgram(mb.shaderName));
        if(!mb.deferredTextureLoading) {
            if (mb.textureNames != null) {
                injectTextures(mb);
            }
            material.setFullyLoaded(true);
        }else {
            material.setFullyLoaded(false);
            TextureLoader loader = new TextureLoader() {
                @Override
                public void load() {
                    status = LoadStatus.LOADING;
                    Thread thread = new Thread(() -> {
                        for (String textureName : mb.textureNames) {
                            Log.d(tag, "loading texture bitmap " + textureName + "...");
                            if (assetLoader.loadTextureBitmap(textureName) != null) {
                                Log.d(tag, "load texture bitmap " + textureName + " success.");
                            } else {
                                Log.e(tag, "load texture bitmap " + textureName + " failed.");
                            }
                        }
                        status = LoadStatus.LOADED;
                    });
                    thread.start();
                }

                @Override
                public void create() {
                    loadTextures(mb);
                    injectTextures(mb);
                }
            };
            material.setTextureLoader(loader);
        }
        return material;
    }


    public RenderContext createRenderContext(AssetBinding ab) {
        Log.i(tag,"creating render context: "+ab.context.name);
        RenderContext context = ab.context;
        context.loadedModel = modelController.getLoadedModel(ab.modelName);
        Material material;
        if(ab.materialBinding!=null){
            material = ab.materialBinding.material;
            context.setMaterial(material);
        }else{
            material = context.getMaterial();
        }
        context.setShaderBinder(new ShaderBinder());
        context.setTextureBinder(new TextureBinder());
        context.setVertexBufferBinder(new VertexBufferBinder());
        context.setGLResourceBinder(new CommonBinder());
        determineDrawMethod(context);
        // shaderController.bindShaderAttributePointers(material.getShader(), context.loadedModel);
        if(ab.transform!=null){
            context.setTransform(ab.transform);
        }
        AttributeBindingProcessor.generateShaderAttributeSetters(context,context.loadedModel,material.getShader());
        UniformBindingProcessor.generateShaderUniformSetters(context, material.getShader());
        return context;
    }

}
