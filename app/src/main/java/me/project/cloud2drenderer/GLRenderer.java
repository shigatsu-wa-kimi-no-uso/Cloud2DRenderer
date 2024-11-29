package me.project.cloud2drenderer;


import static android.opengl.GLES20.GL_SHADING_LANGUAGE_VERSION;
import static android.opengl.GLES20.glGetString;
import static me.project.cloud2drenderer.util.SceneUtils.*;


import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.project.cloud2drenderer.renderer.entity.others.light.PointLight;
import me.project.cloud2drenderer.renderer.scene.Scene;
import me.project.cloud2drenderer.renderer.scene.input.CameraInputHandler;
import me.project.cloud2drenderer.renderer.scene.input.InputController;
import me.project.cloud2drenderer.util.ColorUtils;
import me.project.cloud2drenderer.util.SceneUtils;

public class GLRenderer implements GLSurfaceView.Renderer{


    private Scene scene;


    private Activity activity;

    private final TextView fpsTextView;

    private final TextView cameraTextView;

    private long lastDrawTimeNanos;

    private final InputController inputController;

    private CameraInputHandler cameraInputHandler;
    public GLRenderer(Activity activity,TextView fpsTextView,TextView cameraTextView,InputController inputController) {
        this.fpsTextView =fpsTextView;
        this.activity = activity;
        this.inputController = inputController;
        this.cameraTextView = cameraTextView;
    }

    void init(){

        Log.d(GLRenderer.class.getSimpleName(),"GLES Version: " +  glGetString(GLES20.GL_VERSION));
        Log.d(GLRenderer.class.getSimpleName(),"GLSL Version: " +  glGetString(GL_SHADING_LANGUAGE_VERSION));
        scene = new Scene(activity);
        int width = 100;
        int height = 250;
        float aspect = (float)width/height;
        scene.setViewport(width,height);
        scene.updateViewport();
        scene.camera.setFrustum(60,aspect,0.001f,100);
        scene.camera.setPosition(new float[]{0.0f,0.0f,0.0f});
        scene.camera.setOrientation(new float[]{0.0f,0.0f,-1.0f});
        scene.camera.rotate(0,0);
        scene.camera.update();
    //    scene.enableBlend();
      //  scene.enableCullFace();
        cameraInputHandler = new CameraInputHandler();
        inputController.setCameraInputHandler(cameraInputHandler);
        cameraInputHandler.setCamera(scene.camera);
        updateCameraMotionSensitivity();
    }


    private void updateCameraMotionSensitivity(){
        cameraInputHandler.setCameraMoveSensitivity(10.0f/scene.getViewportWidth(),10.0f/scene.getViewportHeight());
        cameraInputHandler.setCameraRotationSensitivity(30.0f/scene.getViewportHeight(),30.0f/scene.getViewportWidth());
        cameraInputHandler.setCameraUpDownSensitivity(10.0f/scene.getViewportHeight());
    }

    private void updateFPSText() {
        long currTimeNanos = System.nanoTime();
        long duration = currTimeNanos - lastDrawTimeNanos;
        lastDrawTimeNanos = currTimeNanos;
        double fps = 1E9/duration;
        activity.runOnUiThread(()-> fpsTextView.setText(String.format(Locale.getDefault(),"FPS:%.2f",fps)));
    }

    private void updateCameraText(){
        activity.runOnUiThread(() -> {
            float[] cameraPos = scene.camera.getPosition();
            float[] orient = scene.camera.getOrientation();
            String posText = String.format(Locale.getDefault(),"%25s(%.1f ,%.1f, %.1f)","CamPos:",cameraPos[0],cameraPos[1],cameraPos[2]);
            String orientText = String.format(Locale.getDefault(),"%25s(%.1f ,%.1f ,%.1f)","CamOrient:",orient[0],orient[1],orient[2]);
            String text = posText+"\n"+orientText;
            cameraTextView.setText(text);
        });
    }



    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        init();
        PointLight pointLight = new PointLight();
        //255,235,205
        pointLight.setIntensity(ColorUtils.getIntensity(255,235,205,3));
        pointLight.setPosition(new float[]{-1.0f, -1.0f, 0f});
        //  scene.load( getTerrainAssetBinding(1f,1f,new float[]{0.0f,-0.8f,-1.3f}));
      //  scene.load( getCubeAssetBinding(0.5f,1,new float[]{0,0,-5}));
        scene.load(getCubeAssetBinding2("wood cube",new float[]{0.0f, 0.0f, 0f}, new float[]{1, 1, 1}, new float[]{-1f, -1.5f, -5}, pointLight));
        scene.load(getLightCubeAssetBinding("light cube",new float[]{0.0f, 0.0f, 0f}, new float[]{0.05f, 0.05f, 0.05f}, pointLight));

        //需要注意：先画远物体，再画近的
        scene.load(getCheckerBoardAssetBinding("checker board",new float[]{0,-3,0},new float[]{30,30,30},pointLight));
        scene.load(getBillboardAssetBinding(1, 1, new float[]{-1f, 0f, -2.5f}));
        scene.load(getBillboardAssetBinding(1, 1, new float[]{0f, 0f, -2.5f},new float[]{0,0,0}, pointLight));
        scene.load(getBillboardAssetBinding(1, 1, new float[]{0.0f, 0.0f, 0f},new float[]{0,0,0}, pointLight));
        scene.load(getBillboardAssetBinding(1f, 1f, new float[]{-1.3f, 0.0f, 0f},new float[]{0,0,0}, pointLight));
        //scene.load( getBillboardAssetBinding(0.5f,0.5f,new float[]{-1f,-1f,-2.5f}));
        //scene.load( getBillboardAssetBinding(1.5f,1.5f,new float[]{-0.5f,0.15f,-2.3f}));
        //scene.load( getBillboardAssetBinding(1,1,new float[]{-0.25f,0f,-2.0f}));
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
        updateCameraMotionSensitivity();
    }

    int count = 0;
    @Override
    public void onDrawFrame(GL10 gl) {
        updateFPSText();
        updateCameraText();
        scene.adjustObjects();
        scene.clear(0.1f,0.1f,0.1f,1);
        scene.draw();
        count++;
    }
}
