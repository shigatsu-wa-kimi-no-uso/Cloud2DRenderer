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
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.others.light.DistantLight;
import me.project.cloud2drenderer.renderer.entity.others.light.PointLight;
import me.project.cloud2drenderer.renderer.scene.Scene;
import me.project.cloud2drenderer.renderer.scene.input.CameraInputHandler;
import me.project.cloud2drenderer.renderer.scene.input.InputController;
import me.project.cloud2drenderer.util.ColorUtils;

public class GLRenderer implements GLSurfaceView.Renderer{


    private Scene scene;


    private Activity activity;

    private final TextView fpsTextView;

    private final TextView cameraTextView;

    private long lastDrawTimeNanos;

    private final InputController inputController;

    private Map<String,MaterialBinding> materialBindings;

    public static Vector<Material> cloudMaterials;

    private CameraInputHandler cameraInputHandler;

    public GLRenderer(Activity activity,TextView fpsTextView,TextView cameraTextView,InputController inputController) {
        this.fpsTextView =fpsTextView;
        this.activity = activity;
        this.inputController = inputController;
        this.cameraTextView = cameraTextView;
        cloudMaterials = new Vector<>();
        materialBindings = new HashMap<>();
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

    public void loadMaterials(){
        for(MaterialBinding mb : materialBindings.values()){
            scene.loadMaterial(mb);
        }
    }



    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        init();

        MaterialBinding cumulonimbus_76_1 = getSixWayLightingMaterialBinding(
                "76_1_Albedo","76_1_A","76_1_B",8,10,24,"flipbook/six_way_lighting2");
        MaterialBinding cumulonimbus_252_3 = getSixWayLightingMaterialBinding(
                "252_3_Albedo","252_3_A","252_3_B",18,14,8,"flipbook/six_way_lighting2");
        MaterialBinding smokeBall = getSixWayLightingMaterialBinding(
                "SmokeBall_Albedo","SmokeBall_RLT","SmokeBall_BBF",8,8,24,"flipbook/six_way_lighting");
        MaterialBinding jap_cumulonimbus_1 = getSixWayLightingMaterialBinding(
                "jap_cumulonimbus_preset2_1_Albedo_resized","jap_cumulonimbus_preset2_1_A_resized","jap_cumulonimbus_preset2_1_B_resized",18,16,12,"flipbook/six_way_lighting2");
        MaterialBinding jap_cumulonimbus_2 = getSixWayLightingMaterialBinding(
                "jap_cumulonimbus_preset2_2_Albedo","jap_cumulonimbus_preset2_2_A","jap_cumulonimbus_preset2_2_B",16,16,24,"flipbook/six_way_lighting2");
        MaterialBinding jap_cumulonimbus_2_thin = getSixWayLightingMaterialBinding(
                "jap_cumulonimbus_preset2_2_thin_Albedo","jap_cumulonimbus_preset2_2_thin_A","jap_cumulonimbus_preset2_2_thin_B",16,16,24,"flipbook/six_way_lighting2");
        MaterialBinding jap_cumulus_3_thin = getSixWayLightingMaterialBinding(
                "jap_cumulus_preset3_1_thin_Albedo","jap_cumulus_preset3_1_thin_A","jap_cumulus_preset3_1_thin_B",8,7,24,"flipbook/six_way_lighting2");
        MaterialBinding jap_cumulus_4_thin = getSixWayLightingMaterialBinding(
                "jap_cumulus_preset3_2_thin_Albedo","jap_cumulus_preset3_2_thin_A","jap_cumulus_preset3_2_thin_B",16,11,24,"flipbook/six_way_lighting2");
        MaterialBinding jap_cumulo_5 = getSixWayLightingMaterialBinding(
                "jap_cumulo_preset5_1_Albedo","jap_cumulo_preset5_1_A","jap_cumulo_preset5_1_B",12,10,5,"flipbook/six_way_lighting2");
        MaterialBinding jap_cumulo_5_my = getSixWayLightingMaterialBinding(
                "jap_cumulo_preset5_1_my_Albedo","jap_cumulo_preset5_1_my_A","jap_cumulo_preset5_1_my_B",12,10,5,"flipbook/six_way_lighting2");





        //  materialBindings.put("cumulonimbus_76_1",cumulonimbus_76_1);
     //   materialBindings.put("cumulonimbus_252_3",cumulonimbus_252_3);
        materialBindings.put("jap_cumulonimbus_preset2_1",jap_cumulonimbus_1);
        materialBindings.put("jap_cumulonimbus_preset2_2",jap_cumulonimbus_2);
        materialBindings.put("jap_cumulonimbus_preset2_2_thin",jap_cumulonimbus_2_thin);
        materialBindings.put("jap_cumulus_preset3_1_thin",jap_cumulus_3_thin);
        materialBindings.put("jap_cumulus_preset3_2_thin",jap_cumulus_4_thin);
        materialBindings.put("jap_cumulo_preset5_1",jap_cumulo_5);
        materialBindings.put("jap_cumulo_preset5_1_my",jap_cumulo_5_my);
      //  materialBindings.put("SmokeBall",smokeBall);
        cloudMaterials.add(cumulonimbus_76_1.material);
      //  cloudMaterials.add(cumulonimbus_252_3.material);
        cloudMaterials.add(jap_cumulonimbus_1.material);
        cloudMaterials.add(jap_cumulonimbus_2.material);
        cloudMaterials.add(jap_cumulonimbus_2_thin.material);
        cloudMaterials.add(jap_cumulus_3_thin.material);
        cloudMaterials.add(jap_cumulus_4_thin.material);
        cloudMaterials.add(jap_cumulo_5.material);
        cloudMaterials.add(jap_cumulo_5_my.material);
        loadMaterials();
        PointLight pointLight = new PointLight();
        //255,235,205
        pointLight.setIntensity(ColorUtils.getIntensity(255,235,205,0));
        pointLight.setPosition(new float[]{-1.0f, -1.0f, 0f});

        DistantLight distantLight = new DistantLight();
        distantLight.setIntensity(ColorUtils.getIntensity(255,255,255,1f));
        distantLight.setDirection(new float[]{-1,-0.5f,0f});
      //  distantLight.setDirection(new float[]{-1,-1,0.2f});
//        DistantLight distantLight2 = new DistantLight();
//        distantLight2.setDirection(new float[]{-1,0,0});
//        distantLight2.setIntensity(ColorUtils.getIntensity(255,255,255,1.5f));
        //distantLight2.setDirection(new float[]{-1,-1,0.2f});
        //  scene.load( getTerrainAssetBinding(1f,1f,new float[]{0.0f,-0.8f,-1.3f}));
      //  scene.load( getCubeAssetBinding(0.5f,1,new float[]{0,0,-5}));
        scene.load(getCubeAssetBinding2("wood cube",new float[]{0.0f, 0.0f, 0f}, new float[]{1, 1, 1}, new float[]{-1f, -1.5f, -5}, pointLight,distantLight));
        scene.load(getLightCubeAssetBinding("light cube",new float[]{1.0f, 1.0f, -0.2f}, new float[]{0.05f, 0.05f, 0.05f}, pointLight));

        //需要注意：先画远物体，再画近的
        scene.load(getCheckerBoardAssetBinding("checker board",new float[]{0,-3,0},new float[]{30,30,30},pointLight,distantLight));
        //scene.load(getBillboardAssetBinding(1, 1, new float[]{-1f, 0f, -2.5f}));
        scene.load(getBillboardAssetBinding(1, 1, new float[]{0.5f, 0f, -2.5f},new float[]{0,0,0},jap_cumulo_5_my, pointLight,distantLight));
        //scene.load(getBillboardAssetBinding(1, 1, new float[]{0f, 0f, -2.5f},new float[]{0,0,0},jap_cumulo_5, pointLight,distantLight));
      //  scene.load(getBillboardAssetBinding(1, 1, new float[]{0.0f, 0.0f, 0f},new float[]{0,0,0},jap_cumulus_4_thin, pointLight));
       // scene.load(getBillboardAssetBinding(3f, 3f, new float[]{-1.3f, 1.0f, 0f},new float[]{0,0,0},jap_cumulus_3_thin, pointLight));

        scene.freeAllTextureImages();
        scene.initRenderContexts();
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
        scene.clear(213/255.0f,240/255.0f,247/255.0f,1);
        scene.draw();
        count++;
    }
}
