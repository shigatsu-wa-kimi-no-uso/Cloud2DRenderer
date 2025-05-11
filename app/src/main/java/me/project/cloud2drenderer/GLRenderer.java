package me.project.cloud2drenderer;


import static android.opengl.GLES20.GL_SHADING_LANGUAGE_VERSION;
import static android.opengl.GLES20.glGetIntegerv;
import static android.opengl.GLES20.glGetString;
import static me.project.cloud2drenderer.util.SceneUtils.*;


import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.entity.material.SixWayLighting;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.FlipBookConfig;
import me.project.cloud2drenderer.renderer.entity.others.light.DistantLight;
import me.project.cloud2drenderer.renderer.entity.others.light.PointLight;
import me.project.cloud2drenderer.renderer.scene.Scene;
import me.project.cloud2drenderer.renderer.scene.input.CameraInputHandler;
import me.project.cloud2drenderer.renderer.scene.input.InputController;
import me.project.cloud2drenderer.renderer.scene.input.LightController;
import me.project.cloud2drenderer.renderer.scene.input.TextController;
import me.project.cloud2drenderer.util.ColorUtils;
import me.project.cloud2drenderer.util.MatUtils;

public class GLRenderer implements GLSurfaceView.Renderer {


    private Scene scene;


    private final Activity activity;

    private final TextView fpsTextView;

    private final TextView cameraTextView;

    private long lastDrawTimeNanos;


    private DistantLight distantLight = new DistantLight();

    private LightController lightController = new LightController(distantLight);

    private final Vector<FlipBookConfig> flipBookConfigs;

    private CameraInputHandler cameraInputHandler;

    public GLRenderer(Activity activity, TextView fpsTextView, TextView cameraTextView) {
        this.fpsTextView = fpsTextView;
        this.activity = activity;
        //  this.inputController = inputController;
        this.cameraTextView = cameraTextView;
        flipBookConfigs = new Vector<>();
        lightController.setLightDirectionFromRotate(0,0);
        distantLight.setIntensity(1.4f);
    }
    public GLRenderer(Activity activity, TextView fpsTextView, TextView cameraTextView, InputController inputController) {
        this.fpsTextView = fpsTextView;
        this.activity = activity;
       // this.inputController = inputController;
        this.cameraTextView = cameraTextView;
        flipBookConfigs = new Vector<>();

    }



    public LightController getLightController(){
        return lightController;
    }

    void init() {
        cameraTextView.setVisibility(View.INVISIBLE);
        fpsTextView.setVisibility(View.INVISIBLE);

        Log.d(GLRenderer.class.getSimpleName(), "GLES Version: " + glGetString(GLES20.GL_VERSION));
        Log.d(GLRenderer.class.getSimpleName(), "GLSL Version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
        scene = new Scene(activity);
        int width = 100;
        int height = 250;
        float aspect = (float) width / height;
        scene.setViewport(width, height);
        scene.updateViewport();
        scene.camera.setFrustum(60, aspect, 0.001f, 100);
        scene.camera.setPosition(new float[]{0.0f, 0.0f, 0.0f});
       // scene.camera.setOrientation(new float[]{0.0f, 0.0f, -1.0f});
        scene.camera.setOrientation(new float[]{0.0f, 0.36f, -1.0f});
        scene.camera.rotate(0, 0);
        scene.camera.update();

        scene.enableBlend();
        scene.setClearColor(213 / 255.0f, 240 / 255.0f, 247 / 255.0f, 1);
        //  scene.enableCullFace();
    //    cameraInputHandler = new CameraInputHandler();
  //      inputController.setCameraInputHandler(cameraInputHandler);
   //     cameraInputHandler.setCamera(scene.camera);
  //      updateCameraMotionSensitivity();

    }


    private void updateCameraMotionSensitivity() {
        cameraInputHandler.setCameraMoveSensitivity(10.0f / scene.getViewportWidth(), 10.0f / scene.getViewportHeight());
        cameraInputHandler.setCameraRotationSensitivity(30.0f / scene.getViewportHeight(), 30.0f / scene.getViewportWidth());
        cameraInputHandler.setCameraUpDownSensitivity(10.0f / scene.getViewportHeight());
    }

    private void updateFPSText() {
        long currTimeNanos = System.nanoTime();
        long duration = currTimeNanos - lastDrawTimeNanos;
        lastDrawTimeNanos = currTimeNanos;
        double fps = 1E9 / duration;
        activity.runOnUiThread(() -> fpsTextView.setText(String.format(Locale.getDefault(), "FPS:%.2f", fps)));
    }

    private void updateCameraText() {
        activity.runOnUiThread(() -> {
            float[] cameraPos = scene.camera.getPosition();
            float[] orient = scene.camera.getOrientation();
            String posText = String.format(Locale.getDefault(), "%25s(%.1f ,%.1f, %.1f)", "CamPos:", cameraPos[0], cameraPos[1], cameraPos[2]);
            String orientText = String.format(Locale.getDefault(), "%25s(%.1f ,%.1f ,%.1f)", "CamOrient:", orient[0], orient[1], orient[2]);
            String text = posText + "\n" + orientText;
            cameraTextView.setText(text);
        });
    }





    void initializeAssetConfigs() {
        MaterialBinding jap_cumulo_5_my = getSixWayLightingMaterialBinding(
                "jap_cumulo_preset5_1_my_Albedo",
                "jap_cumulo_preset5_1_my_A",
                "jap_cumulo_preset5_1_my_B",
                "flipbook/six_way_lighting2");
        Vector<MaterialBinding> cumulos = new Vector<>();
        Thread[] threads = new Thread[8];
        MaterialBinding[] bindings = new MaterialBinding[8];

        bindings[0] = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset6_512x256_1_Albedo",
                "cloud/jap_cumulo_preset6_512x256_1_A",
                "cloud/jap_cumulo_preset6_512x256_1_B",
                "flipbook/six_way_lighting2");

        FlipBookConfig config = new FlipBookConfig(0,
                bindings[0],
                (SixWayLighting) bindings[0].material,
                new int[]{16, 32},
                new float[]{2f, 1.5f, -2f},
               // new float[]{-1f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
      //  config.setRefreshPositionLB(new float[]{0.5f, 1.5f, 0});
      //  config.setRefreshPositionUB(new float[]{0.5f, 1.5f, 0});
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
       // config.setScaleUB(1.5f);
     //   config.setScaleLB(1.5f);
        config.setScaleUB(1.5f);
       config.setScaleLB(0.8f);
        config.setVelocityLB(0);
        config.setVelocityUB(0);
        config.setMoveDirection(new float[]{1,0,0});
        flipBookConfigs.add(config);
        cumulos.add(bindings[0]);

      /*  bindings[0] = getSixWayLightingMaterialBinding(
                "cloud/presentation_Albedo",
                "cloud/presentation_A",
                "cloud/presentation_B",
                "flipbook/six_way_lighting2");

        config = new FlipBookConfig(0,
                bindings[0],
                (SixWayLighting) bindings[0].material,
                new int[]{1, 1},
                new float[]{0f, 0f, -2f},
                // new float[]{-1f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{0.25f, 0f, 0});
        config.setRefreshPositionUB(new float[]{0.25f, 0f, 0});
        //  config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        // config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(1f);
        config.setScaleLB(1f);
        //  config.setScaleUB(2);
        //  config.setScaleLB(1f);
        config.setVelocityLB(0);
        config.setVelocityUB(0);
        config.setMoveDirection(new float[]{1,0,0});
        flipBookConfigs.add(config);
        cumulos.add(bindings[0]);*/
      //  threads[0] = new Thread(() -> scene.loadMaterial(bindings[0]));

        bindings[1] = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset6_512x256_2_Albedo",
                "cloud/jap_cumulo_preset6_512x256_2_A",
                "cloud/jap_cumulo_preset6_512x256_2_B",
                "flipbook/six_way_lighting2");
        config = new FlipBookConfig(1,
                bindings[1],
                (SixWayLighting) bindings[1].material,
                new int[]{15, 21},
                new float[]{0f, 1.5f, -2.5f},
                new float[]{2, 1f, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
       // config.setRefreshPositionLB(new float[]{0.25f, 3.5f, 0f});
        //config.setRefreshPositionUB(new float[]{0.25f, 3.5f, 0f});
        config.setRefreshPositionLB(new float[]{-1,1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
      //  config.setScaleLB(1.2f);
       // config.setScaleUB(1.2f);
        config.setScaleUB(1.5f);
        config.setScaleLB(0.8f);
        config.setVelocityLB(0);
        config.setVelocityUB(0);
        config.setMoveDirection(new float[]{1,0,0});
        flipBookConfigs.add(config);
        cumulos.add(bindings[1]);
      //  threads[1] = new Thread(() -> scene.loadMaterial(bindings[1]));
     //   threads[1].start();

        bindings[2] = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset6_512x256_3_Albedo",
                "cloud/jap_cumulo_preset6_512x256_3_A",
                "cloud/jap_cumulo_preset6_512x256_3_B",
                "flipbook/six_way_lighting2");
        config = new FlipBookConfig(2,
                bindings[2],
                (SixWayLighting) bindings[2].material,
                new int[]{12, 10},
                new float[]{0f, 1.5f, -4f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(1.5f);
        config.setScaleLB(0.5f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipBookConfigs.add(config);
        cumulos.add(bindings[2]);
      //  threads[2] = new Thread(() -> scene.loadMaterial(bindings[2]));
       // threads[2].start();

        bindings[3] = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset7_512x256_1_Albedo",
                "cloud/jap_cumulo_preset7_512x256_1_A",
                "cloud/jap_cumulo_preset7_512x256_1_B",
                "flipbook/six_way_lighting2");
        config = new FlipBookConfig(3,
                bindings[3],
                (SixWayLighting) bindings[3].material,
                new int[]{16, 31},
                new float[]{0f, 1.5f, -3f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{0f, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
       // config.setRefreshPositionLB(new float[]{-0.5f, 1.5f, 0});
        //config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
         config.setScaleUB(1.5f);
         config.setScaleLB(0.5f);
       // config.setScaleUB(2);
     //   config.setScaleLB(0.85f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipBookConfigs.add(config);
        cumulos.add(bindings[3]);
      //  threads[3] = new Thread(() -> scene.loadMaterial(bindings[3]));
     //   threads[3].start();

        bindings[4] = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset7_512x256_2_Albedo",
                "cloud/jap_cumulo_preset7_512x256_2_A",
                "cloud/jap_cumulo_preset7_512x256_2_B",
                "flipbook/six_way_lighting2");
        config = new FlipBookConfig(4,
                bindings[4],
                (SixWayLighting) bindings[4].material,
                new int[]{12, 28},
                new float[]{0f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(1.5f);
        config.setScaleLB(0.5f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipBookConfigs.add(config);
        cumulos.add(bindings[4]);
     //   threads[4] = new Thread(() -> scene.loadMaterial(bindings[4]));
     //   threads[4].start();

        bindings[5] = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset8_336x168_1_Albedo",
                "cloud/jap_cumulo_preset8_336x168_1_A",
                "cloud/jap_cumulo_preset8_336x168_1_B",
                "flipbook/six_way_lighting2");
        config = new FlipBookConfig(5,
                bindings[5],
                (SixWayLighting) bindings[5].material,
                new int[]{24, 27},
                new float[]{0f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(1.5f);
        config.setScaleLB(0.5f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipBookConfigs.add(config);
        cumulos.add(bindings[5]);
    //    threads[5] = new Thread(() -> scene.loadMaterial(bindings[5]));
    //    threads[5].start();

        bindings[6] = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset9_464x232_1_Albedo",
                "cloud/jap_cumulo_preset9_464x232_1_A",
                "cloud/jap_cumulo_preset9_464x232_1_B",
                "flipbook/six_way_lighting2");
        config = new FlipBookConfig(6,
                bindings[6],
                (SixWayLighting) bindings[6].material,
                new int[]{16, 35},
                new float[]{0f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(1.5f);
        config.setScaleLB(0.5f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipBookConfigs.add(config);
        cumulos.add(bindings[6]);
      //  threads[6] = new Thread(() -> scene.loadMaterial(bindings[6]));
   //     threads[6].start();

        bindings[7] = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset9_512x256_2_Albedo",
                "cloud/jap_cumulo_preset9_512x256_2_A",
                "cloud/jap_cumulo_preset9_512x256_2_B",
                "flipbook/six_way_lighting2");
        config = new FlipBookConfig(7,
                bindings[7],
                (SixWayLighting) bindings[7].material,
                new int[]{16, 22},
                new float[]{0f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(1.5f);
        config.setScaleLB(0.5f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipBookConfigs.add(config);
        cumulos.add(bindings[7]);
       // threads[7] = new Thread(() -> scene.loadMaterial(bindings[7]));
     //   threads[7].start();
/*
        for(Thread thread:threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/
        for(MaterialBinding mb : cumulos){
            mb.deferredTextureLoading = true;
            scene.loadMaterial(mb);
        }
    }




    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        init();
        initializeAssetConfigs();

        //255,235,205
       // pointLight.setIntensity(ColorUtils.getIntensity(255, 235, 225, 0));
       // pointLight.setPosition(new float[]{-1.0f, -1.0f, 0f});

       // float[] rotation = MatUtils.newRotationMatrix(50,20,55);
        float[] dir = {0,0,-1,0};
      //  dir = MatUtils.matVecMultiply(rotation,dir,4);
        //double theta = Math.toRadians(time*scalar);
       // float[] point2 ={(float)Math.sin(theta),0,(float)Math.cos(theta)};
        //return MatUtils.normalized(rotation);

      //  distantLight.setDirection(new float[]{-1, 0.4f, 0.1f});
     //   distantLight.setDirection(new float[]{-1, -0.4f, -0.2f});
    //    distantLight.setDirection(dir);
        float[] color1 = ColorUtils.normalizeColor(255,255,255);
        float[] color2 = ColorUtils.normalizeColor(255,255,255);
        float[] color3 = ColorUtils.normalizeColor(255,255,255);
        float[] color4 = ColorUtils.normalizeColor(255,255,255);
        scene.load(getBackgroundAssetBinding("sky",new float[]{3.5f,10.5f,1},new float[]{0,-0.5f,-5.0f}));
        int[] totalTakenCnt = new int[1];

       // scene.load(getCubeAssetBinding(2,new float[]{2,2,2},new float[]{0,0,0}));
        scene.load(getBillboardAssetBinding("billboard1", flipBookConfigs,0, 0,totalTakenCnt,color1,0,-2.2f,-2f, null, distantLight));

        scene.load(getBillboardAssetBinding("billboard2", flipBookConfigs,1, 3,totalTakenCnt,color2,8,-2.55f,-2.35f, null, distantLight));
        scene.load(getBillboardAssetBinding("billboard3", flipBookConfigs, 2,2,totalTakenCnt,color3, 12,-2.9f,-2.7f,null, distantLight));
        scene.load(getBillboardAssetBinding("billboard4", flipBookConfigs, 3,3,totalTakenCnt,color4,16 ,-3.25f,-3.05f,null, distantLight));
        //scene.load(getBillboardAssetBinding("billboard2",flipbookConfigs, pointLight,distantLight));
        //   scene.load(getBillboardAssetBinding("billboard3",flipbookConfigs, pointLight,distantLight));
        totalTakenCnt[0] = 4;
        scene.initRenderContexts();
        scene.loadRenderContextsToPipeline();
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float aspect = (float) width / height;
        scene.setViewport(width, height);
        scene.updateViewport();
        scene.camera.setFrustumAspect(aspect);
        scene.camera.update();
    //    updateCameraMotionSensitivity();
    }


    int count = 0;

    @Override
    public void onDrawFrame(GL10 gl) {
      //  updateFPSText();
       // updateCameraText();

        scene.adjustObjects();
        scene.clear(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1);
        scene.clear();
        scene.draw();
        count++;
    }
}
