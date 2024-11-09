package me.project.cloud2drenderer;


import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static me.project.cloud2drenderer.opengl.statemanager.GLVertexBufferManager.loadVertexAttributeData;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Locale;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.project.cloud2drenderer.opengl.glresource.buffer.GLVertexBuffer;
import me.project.cloud2drenderer.opengl.statemanager.GLVertexBufferManager;
import me.project.cloud2drenderer.renderer.context.MixedTextureRenderContext;
import me.project.cloud2drenderer.renderer.context.SequenceFrameRenderContext;
import me.project.cloud2drenderer.renderer.controller.ModelController;
import me.project.cloud2drenderer.renderer.entity.AssetBinding;
import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.entity.material.DiffuseTextureMaterial;
import me.project.cloud2drenderer.renderer.entity.material.MixedImgMaterial;
import me.project.cloud2drenderer.renderer.entity.model.LoadedModel;
import me.project.cloud2drenderer.renderer.entity.model.MeshModel;
import me.project.cloud2drenderer.renderer.entity.others.SequenceFrameParams;
import me.project.cloud2drenderer.renderer.loader.AssetLoader;
import me.project.cloud2drenderer.renderer.procedure.pipeline.CommonPipeline;
import me.project.cloud2drenderer.renderer.procedure.pipeline.RenderPipeline;
import me.project.cloud2drenderer.renderer.scene.Scene;

public class GLRenderer implements GLSurfaceView.Renderer{


    private Scene scene;


    private Activity activity;

    private final TextView fpsTextView;

    private long lastDrawTimeNanos;

    public GLRenderer(Activity activity,TextView fpsTextView) {
        this.fpsTextView =fpsTextView;
        this.activity = activity;

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

    AssetBinding getCubeAssetBinding(float ratio,float scale,float[] position){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        ab.pipelineName = "non_blend";
        mb.textureNames = new String[]{"container","awesomeface"};
        mb.shaderName = "mixed_img";
        MixedImgMaterial material = new MixedImgMaterial();
        material.setRatio(ratio);
        mb.material = material;
        ab.modelName = "cube";
        ab.materialBinding = mb;
        MixedTextureRenderContext context = new MixedTextureRenderContext();
        float[] transform = context.getTransform();
        Matrix.scaleM(transform,0,scale,scale,scale);
        Matrix.translateM(transform,0,position[0],position[1],position[2]);
        ab.context = context;
        return ab;
    }


    AssetBinding getBillboardAssetBinding(float width,float height,float[] position){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        ab.pipelineName = "blend";
        mb.textureNames = new String[]{"SmokeLoopPNG"};
        mb.shaderName = "seq_frame";
        mb.material = new DiffuseTextureMaterial();
        ab.modelName = "rectangle";
        ab.materialBinding = mb;
        //CommonRenderContext context = new CommonRenderContext();
        SequenceFrameRenderContext context = new SequenceFrameRenderContext();
        float[] transform = context.getTransform();
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
        scene.load( getCubeAssetBinding(0.5f,1,new float[]{0,0,-5}));
        scene.load( getCubeAssetBinding(0.5f,1,new float[]{-1.0f,-1.5f,-5}));
        //需要注意：先画远物体，再画近的

        scene.load( getBillboardAssetBinding(3,3,new float[]{-1.0f,-1.0f,-7f}));
        scene.load( getBillboardAssetBinding(1,1,new float[]{0f,0f,-2.5f}));
        scene.load( getBillboardAssetBinding(0.5f,0.5f,new float[]{-1f,-1f,-2.5f}));
        scene.load( getBillboardAssetBinding(1.5f,1.5f,new float[]{-0.5f,0.15f,-2.3f}));
        scene.load( getBillboardAssetBinding(1,1,new float[]{-0.25f,0f,-2.0f}));
    //    scene.load( getBillboardAssetBinding(1.5f,1.5f,new float[]{-0.5f,-0.25f,-2.0f}));
        scene.freeAllTextureImages();
        scene.initRenderContexts();
       // scene.load(getBillboardAssetBinding(1,1,new float[]{0f,-0.5f,-5}));
       // scene.initRenderContexts();
        //scene.load(getCubeAssetBinding(0.5f,1,new float[]{0,1,-5}));
     //   scene.load(getCubeAssetBinding(0.5f,1,new float[]{0,-1,-5}));

       // scene.load(getBillboardAssetBinding(3,3,new float[]{0.5f,-0.5f,-20}));
       // scene.load(getBillboardAssetBinding(2,2,new float[]{0.5f,0.5f,-30}));
       // scene.load(getBillboardAssetBinding(1,1,new float[]{-0.5f,0.25f,-15}));
       // scene.load(getBillboardAssetBinding(1,1,new float[]{0.5f,0.0f,-20}));
       // scene.load(getBillboardAssetBinding(3,3,new float[]{0.75f,0.0f,-20}));
       // scene.load(getBillboardAssetBinding(5,5,new float[]{-0.75f,0.7f,-20}));
       // scene.load(getBillboardAssetBinding(5,5,new float[]{-0.75f,-1.7f,-20}));
       // scene.initRenderContexts();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float aspect = (float)width/height;
        scene.setViewport(width,height);
        scene.updateViewport();
        scene.camera.setFrustumAspect(aspect);
        scene.camera.update();
    }

    int count = 0;
    @Override
    public void onDrawFrame(GL10 gl) {
        updateFPS();
        scene.adjustObjects();
        scene.clear();
        scene.draw();
/*        if(count > 60*5){
            scene.load(getCubeAssetBinding(0.5f,1,new float[]{0,-1,-5}));
            scene.initRenderContexts();
        }*/
        count++;
    }
}
