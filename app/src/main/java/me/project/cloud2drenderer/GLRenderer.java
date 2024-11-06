package me.project.cloud2drenderer;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.project.cloud2drenderer.renderer.entity.AssetBinding;
import me.project.cloud2drenderer.renderer.context.CommonRenderContext;
import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.entity.material.ImgTextureMaterial;
import me.project.cloud2drenderer.renderer.procedure.pipeline.CommonPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.RenderPipeline;
import me.project.cloud2drenderer.renderer.scene.Scene;

public class GLRenderer implements GLSurfaceView.Renderer{


    private Scene scene;

    private final Context context;

    private final RenderPipeline[] pipelines;
    public GLRenderer(Context context) {
        this.context = context;
        pipelines = new RenderPipeline[1];
        pipelines[0] = new CommonPipeline();
    }

    void init(){
        scene = new Scene(context);
        int width = 100;
        int height = 250;
        float aspect = (float)width/height;
        scene.setViewport(width,height);
        scene.updateViewport();
        scene.camera.setFrustum(45,aspect,0.001f,100);
        scene.camera.setPosition(new float[]{0,0,2});
        scene.camera.setOrientation(new float[]{0,0,-1});
        scene.camera.setOrientation(0,0);
        scene.camera.update();
    }

    AssetBinding getCubeAssetBinding(float ratio,float[] transform){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        mb.textureNames = new String[]{"container","awesomeface"};
        mb.shaderName = "basic";
        mb.materialClass = ImgTextureMaterial.class;
        ab.modelName = "cube";
        ab.materialBinding = mb;
        CommonRenderContext context = new CommonRenderContext();
        context.ratio.setValue(new float[]{ratio});
        context.transform = transform;
        ab.context = context;
        return ab;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        init();
        float[] transform = new float[16];
        Matrix.setIdentityM(transform,0);
        Matrix.translateM(transform,0,-1.0f,0f,-6f);
        scene.load(getCubeAssetBinding(0.8f,transform));
        transform = new float[16];
        Matrix.setIdentityM(transform,0);
        Matrix.translateM(transform,0,1.0f,0f,-6f);
        scene.load(getCubeAssetBinding(0.2f,transform));
        scene.initRenderContexts();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float aspect = (float)width/height;
        scene.setViewport(width,height);
        scene.updateViewport();
        scene.camera.setFrustumAspect(aspect);
        scene.camera.update();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        scene.adjustObjects();
        scene.clear();
        scene.draw(pipelines);
    }
}
