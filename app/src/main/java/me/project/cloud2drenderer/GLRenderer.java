package me.project.cloud2drenderer;


import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.widget.TextView;

import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.project.cloud2drenderer.renderer.context.SequenceFrameRenderContext;
import me.project.cloud2drenderer.renderer.entity.AssetBinding;
import me.project.cloud2drenderer.renderer.context.CommonRenderContext;
import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.entity.material.DiffuseTextureMaterial;
import me.project.cloud2drenderer.renderer.entity.others.SequenceFrameParams;
import me.project.cloud2drenderer.renderer.procedure.pipeline.CommonPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.RenderPipeline;
import me.project.cloud2drenderer.renderer.scene.Scene;

public class GLRenderer implements GLSurfaceView.Renderer{


    private Scene scene;

    private final RenderPipeline[] pipelines;

    private Activity activity;

    private final TextView fpsTextView;

    private long lastDrawTimeNanos;

    public GLRenderer(Activity activity,TextView fpsTextView) {
        this.fpsTextView =fpsTextView;
        this.activity = activity;
        pipelines = new RenderPipeline[1];
        pipelines[0] = new CommonPipeline();
    }

    void init(){
        scene = new Scene(activity);
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
        scene.turnOnBlend();
    }


    private void updateFPS() {
        long currTimeNanos = System.nanoTime();
        long duration = currTimeNanos - lastDrawTimeNanos;
        lastDrawTimeNanos = currTimeNanos;
        double fps = 1E9/duration;
        activity.runOnUiThread(()-> fpsTextView.setText(String.format(Locale.getDefault(),"FPS:%.2f",fps)));
    }

    AssetBinding getCubeAssetBinding(float ratio,float[] transform){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        mb.textureNames = new String[]{"container","awesomeface"};
        mb.shaderName = "basic";
        mb.materialClass = DiffuseTextureMaterial.class;
        ab.modelName = "cube";
        ab.materialBinding = mb;
        CommonRenderContext context = new CommonRenderContext();
        context.ratio.setValue(new float[]{ratio});
        context.transform = transform;
        ab.context = context;
        return ab;
    }


    AssetBinding getBillboardAssetBinding(int width,int height,float[] position){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        mb.textureNames = new String[]{"SmokeLoopPNG"};
        mb.shaderName = "seq_frame";
        mb.materialClass = DiffuseTextureMaterial.class;
        ab.modelName = "rectangle";
        ab.materialBinding = mb;
        //CommonRenderContext context = new CommonRenderContext();
        SequenceFrameRenderContext context = new SequenceFrameRenderContext();
        float[] transform = context.getTransform();
        Matrix.setIdentityM(transform,0);
        Matrix.scaleM(transform,0,width,height,1);
        Matrix.translateM(transform,0,position[0],position[1],position[2]);

        SequenceFrameParams seqFrameParams = context.getSeqFrameParams();
        seqFrameParams.setCurrentFrameIndex(0);
        seqFrameParams.setFlipBookShape(new float[]{8.0f,8.0f});
        seqFrameParams.setFrequency(1.0f/2.5f);
        ab.context = context;
        return ab;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        init();
        scene.load(getBillboardAssetBinding(4,4,new float[]{-1f,-0.5f,-25}));
        scene.load(getBillboardAssetBinding(3,3,new float[]{0.5f,-0.5f,-20}));
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
        updateFPS();
        scene.adjustObjects();
        scene.clear();
        scene.draw(pipelines);
    }
}
