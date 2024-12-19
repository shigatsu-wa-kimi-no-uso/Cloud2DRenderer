package me.project.cloud2drenderer;


import static android.opengl.GLES20.GL_MAX_TEXTURE_SIZE;
import static android.opengl.GLES20.GL_SHADING_LANGUAGE_VERSION;
import static android.opengl.GLES20.glGetIntegerv;
import static android.opengl.GLES20.glGetString;
import static me.project.cloud2drenderer.util.SceneUtils.*;


import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.material.SixWayLighting;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.FlipbookConfig;
import me.project.cloud2drenderer.renderer.entity.others.light.DistantLight;
import me.project.cloud2drenderer.renderer.entity.others.light.PointLight;
import me.project.cloud2drenderer.renderer.scene.Scene;
import me.project.cloud2drenderer.renderer.scene.input.CameraInputHandler;
import me.project.cloud2drenderer.renderer.scene.input.InputController;
import me.project.cloud2drenderer.util.ColorUtils;

public class GLRenderer implements GLSurfaceView.Renderer {


    private Scene scene;


    private Activity activity;

    private final TextView fpsTextView;

    private final TextView cameraTextView;

    private long lastDrawTimeNanos;

    private final InputController inputController;

    private final Map<String, MaterialBinding> materialBindings;

    private final Vector<FlipbookConfig> flipbookConfigs;

    private CameraInputHandler cameraInputHandler;

    public GLRenderer(Activity activity, TextView fpsTextView, TextView cameraTextView, InputController inputController) {
        this.fpsTextView = fpsTextView;
        this.activity = activity;
        this.inputController = inputController;
        this.cameraTextView = cameraTextView;
        flipbookConfigs = new Vector<>();
        materialBindings = new HashMap<>();
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
        scene.camera.setOrientation(new float[]{0.0f, 0.36f, -1.0f});
        scene.camera.rotate(0, 0);
        scene.camera.update();

        scene.enableBlend();
        scene.setClearColor(213 / 255.0f, 240 / 255.0f, 247 / 255.0f, 1);
        //  scene.enableCullFace();
        cameraInputHandler = new CameraInputHandler();
        inputController.setCameraInputHandler(cameraInputHandler);
        cameraInputHandler.setCamera(scene.camera);
        updateCameraMotionSensitivity();

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

    public void loadMaterials(Collection<MaterialBinding> materialBindings) {
        for (MaterialBinding mb : materialBindings) {
            scene.loadMaterial(mb);
        }
    }


    void initializeAssetConfigs() {
        MaterialBinding jap_cumulo_5_my = getSixWayLightingMaterialBinding(
                "jap_cumulo_preset5_1_my_Albedo",
                "jap_cumulo_preset5_1_my_A",
                "jap_cumulo_preset5_1_my_B",
                "flipbook/six_way_lighting2");
        Vector<MaterialBinding> cumulos = new Vector<>();

        MaterialBinding binding = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset6_512x256_1_Albedo",
                "cloud/jap_cumulo_preset6_512x256_1_A",
                "cloud/jap_cumulo_preset6_512x256_1_B",
                "flipbook/six_way_lighting2");
        FlipbookConfig config = new FlipbookConfig(0,
                (SixWayLighting) binding.material,
                new float[]{16, 32},
                new float[]{-1f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(2);
        config.setScaleLB(1);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipbookConfigs.add(config);
        cumulos.add(binding);


        binding = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset6_512x256_2_Albedo",
                "cloud/jap_cumulo_preset6_512x256_2_A",
                "cloud/jap_cumulo_preset6_512x256_2_B",
                "flipbook/six_way_lighting2");
        config = new FlipbookConfig(1,
                (SixWayLighting) binding.material,
                new float[]{15, 21},
                new float[]{0f, 1.5f, -2.5f},
                new float[]{2, 1f, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(2);
        config.setScaleLB(1);
        config.setVelocityLB(0);
        config.setVelocityUB(0);
        config.setMoveDirection(new float[]{1,0,0});
        flipbookConfigs.add(config);
        cumulos.add(binding);

        binding = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset6_512x256_3_Albedo",
                "cloud/jap_cumulo_preset6_512x256_3_A",
                "cloud/jap_cumulo_preset6_512x256_3_B",
                "flipbook/six_way_lighting2");
        config = new FlipbookConfig(2,
                (SixWayLighting) binding.material,
                new float[]{12, 10},
                new float[]{0f, 1.5f, -4f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(2);
        config.setScaleLB(0.75f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipbookConfigs.add(config);
        cumulos.add(binding);

        binding = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset7_512x256_1_Albedo",
                "cloud/jap_cumulo_preset7_512x256_1_A",
                "cloud/jap_cumulo_preset7_512x256_1_B",
                "flipbook/six_way_lighting2");
        config = new FlipbookConfig(3,
                (SixWayLighting) binding.material,
                new float[]{16, 31},
                new float[]{0f, 1.5f, -3f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-0.5f, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(2);
        config.setScaleLB(0.85f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipbookConfigs.add(config);
        cumulos.add(binding);

        binding = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset7_512x256_2_Albedo",
                "cloud/jap_cumulo_preset7_512x256_2_A",
                "cloud/jap_cumulo_preset7_512x256_2_B",
                "flipbook/six_way_lighting2");
        config = new FlipbookConfig(4,
                (SixWayLighting) binding.material,
                new float[]{12, 28},
                new float[]{0f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(2);
        config.setScaleLB(0.85f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipbookConfigs.add(config);
        cumulos.add(binding);


        binding = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset8_336x168_1_Albedo",
                "cloud/jap_cumulo_preset8_336x168_1_A",
                "cloud/jap_cumulo_preset8_336x168_1_B",
                "flipbook/six_way_lighting2");
        config = new FlipbookConfig(5,
                (SixWayLighting) binding.material,
                new float[]{24, 27},
                new float[]{0f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(2);
        config.setScaleLB(1f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipbookConfigs.add(config);
        cumulos.add(binding);

        binding = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset9_464x232_1_Albedo",
                "cloud/jap_cumulo_preset9_464x232_1_A",
                "cloud/jap_cumulo_preset9_464x232_1_B",
                "flipbook/six_way_lighting2");
        config = new FlipbookConfig(6,
                (SixWayLighting) binding.material,
                new float[]{16, 35},
                new float[]{0f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(2);
        config.setScaleLB(1f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipbookConfigs.add(config);
        cumulos.add(binding);

        binding = getSixWayLightingMaterialBinding(
                "cloud/jap_cumulo_preset9_512x256_2_Albedo",
                "cloud/jap_cumulo_preset9_512x256_2_A",
                "cloud/jap_cumulo_preset9_512x256_2_B",
                "flipbook/six_way_lighting2");
        config = new FlipbookConfig(7,
                (SixWayLighting) binding.material,
                new float[]{16, 22},
                new float[]{0f, 1.5f, -2f},
                new float[]{2, 1, 1});
        config.setFramesPerSecondLB(2);
        config.setFramesPerSecondUB(6);
        config.setRefreshPositionLB(new float[]{-1, 1.5f, 0});
        config.setRefreshPositionUB(new float[]{0f, 1.5f, 0});
        config.setScaleUB(2);
        config.setScaleLB(1f);
        config.setVelocityLB(0);
        config.setVelocityUB(1/32f);
        config.setMoveDirection(new float[]{1,0,0});
        flipbookConfigs.add(config);
        cumulos.add(binding);

        loadMaterials(cumulos);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        init();
        initializeAssetConfigs();

        PointLight pointLight = new PointLight();
        //255,235,205
        pointLight.setIntensity(ColorUtils.getIntensity(255, 235, 205, 0));
        pointLight.setPosition(new float[]{-1.0f, -1.0f, 0f});

        DistantLight distantLight = new DistantLight();
        distantLight.setIntensity(ColorUtils.getIntensity(255, 255, 255, 1.35f));
        distantLight.setDirection(new float[]{-1, 0f, 0f});

        float[] color1 = ColorUtils.normalizeColor(255,255,255);
        float[] color2 = ColorUtils.normalizeColor(255,255,255);
        float[] color3 = ColorUtils.normalizeColor(255,255,255);
        scene.load(getCheckerBoardAssetBinding("checkerboard",new float[]{30,30,0},new float[]{0,-1,0},pointLight,distantLight));
        int[] totalTakenCnt = new int[1];
        scene.load(getBillboardAssetBinding("billboard1", flipbookConfigs,0, 0,totalTakenCnt,color1,0,-2.2f,-2f, pointLight, distantLight));
        scene.load(getBillboardAssetBinding("billboard2", flipbookConfigs,1, 1,totalTakenCnt,color2,3,-2.5f,-2.21f, pointLight, distantLight));
        scene.load(getBillboardAssetBinding("billboard3", flipbookConfigs, 2,2,totalTakenCnt,color3, 6,-2.75f,-2.51f,pointLight, distantLight));
        scene.load(getBillboardAssetBinding("billboard3", flipbookConfigs, 3,3,totalTakenCnt,color3,9 ,-3f,-2.76f,pointLight, distantLight));
        //  scene.load(getBillboardAssetBinding("billboard2",flipbookConfigs, pointLight,distantLight));
        //   scene.load(getBillboardAssetBinding("billboard3",flipbookConfigs, pointLight,distantLight));
        totalTakenCnt[0] = 4;

        scene.freeAllTextureImages();
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
        updateCameraMotionSensitivity();
    }


    int count = 0;

    @Override
    public void onDrawFrame(GL10 gl) {
        //updateFPSText();
      //  updateCameraText();
        scene.adjustObjects();
        //scene.clear(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1);
        scene.clear();
        scene.draw();
        count++;
    }
}
