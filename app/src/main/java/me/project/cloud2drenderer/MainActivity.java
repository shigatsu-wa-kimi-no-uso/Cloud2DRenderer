package me.project.cloud2drenderer;

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import me.project.cloud2drenderer.databinding.ActivityMainBinding;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.FlipBookConfig;
import me.project.cloud2drenderer.renderer.scene.input.InputController;
import me.project.cloud2drenderer.renderer.scene.input.LightController;
import me.project.cloud2drenderer.renderer.scene.input.Rotation2;

public class MainActivity extends AppCompatActivity {

    private final static String tag = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;

    String getGLESVersion(){
        final ActivityManager activityManager=(ActivityManager)getSystemService(ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo=activityManager.getDeviceConfigurationInfo();
        return configurationInfo.getGlEsVersion();
    }
    private GLSurfaceView surfaceView;

    private View view;

    private InputController inputController;

    Runnable makeWidgetsInvisible = () -> {
        setWidgetsVisiblility(View.INVISIBLE);
    };

    private void setWidgetsVisiblility(int visibility){
        binding.seekBarLightRotateX.setVisibility(visibility);
        binding.seekBarLightRotateY.setVisibility(visibility);
        binding.seekBarLightIntensity.setVisibility(visibility);
        binding.seekBarPlaySpeed.setVisibility(visibility);
        binding.textView1.setVisibility(visibility);
        binding.textView2.setVisibility(visibility);
        binding.textView3.setVisibility(visibility);
        binding.textView4.setVisibility(visibility);
        binding.textView5.setVisibility(visibility);
        binding.textView6.setVisibility(visibility);
        binding.textView7.setVisibility(visibility);
        binding.textViewLightRotateX.setVisibility(visibility);
        binding.textViewLightRotateY.setVisibility(visibility);
        binding.textViewLightIntensity.setVisibility(visibility);
        binding.textViewPlaySpeed.setVisibility(visibility);
    }


    public void updateLightRotateTexts(Rotation2 rotation) {
        runOnUiThread(() -> {
            binding.textViewLightRotateX.setText(String.format(Locale.getDefault(),"%d°",(int)rotation.getHorizontal()));
            binding.textViewLightRotateY.setText(String.format(Locale.getDefault(),"%d°",(int)rotation.getVertical()));
        });
    }

    public void updateLightIntensityTexts(float intensity) {
        runOnUiThread(() -> {
            binding.textViewLightIntensity.setText(String.format(Locale.getDefault(),"%.1f",intensity));
        });
    }

    public void updatePlaySpeedTexts(float playSpeedMultiplier) {
        runOnUiThread(() -> {
            binding.textViewPlaySpeed.setText(String.format(Locale.getDefault(),"%.1f",playSpeedMultiplier));
        });
    }

    private void correctLightIntensitySeekBarProgress(LightController lightController, SeekBar seekBar) {
        int min = seekBar.getMin();
        int max = seekBar.getMax();
        float maxIntensity = lightController.getMaxLightIntensity();
        float minIntensity = lightController.getMinLightIntensity();
        float p = lightController.getLightIntensity()/(float)( maxIntensity - minIntensity);
        float progress = min + (max-min)*p;
        seekBar.setProgress((int)progress);
    }

    private void correctPlaySpeedSeekBarProgress(SeekBar seekBar) {
        int min = seekBar.getMin();
        int max = seekBar.getMax();
        float maxMultiplier = FlipBookConfig.getMaxPlaySpeedMultiplier();
        float minMiltiplier = FlipBookConfig.getMinPlaySpeedMultiplier();
        float p = FlipBookConfig.getPlaySpeedMultiplier()/(float)(maxMultiplier - minMiltiplier);
        float progress = min + (max-min)*p;
        seekBar.setProgress((int)progress);
    }

    private boolean initialize(){

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        surfaceView = binding.glSurfaceView;
        view = binding.getRoot();
        setContentView(view);

        /*inputController = new InputController(this,
                binding.moveImageButton,
                binding.rotateImageButton,
                binding.upDownImageButton,
                binding.resetImageButton);*/
        GLRenderer myRenderer = new GLRenderer(this,binding.fpsTextView,binding.cameraTextView);

    //    surfaceView.setEGLConfigChooser(new MultisampleConfigChooser());
        surfaceView.setEGLContextClientVersion(3);
        surfaceView.setRenderer(myRenderer);

        int vanishDelay = 5000;
        LightController lightController = myRenderer.getLightController();
        setWidgetsVisiblility(View.INVISIBLE);
        updateLightRotateTexts(lightController.getRotation2());
        updateLightIntensityTexts(lightController.getLightIntensity());
        updatePlaySpeedTexts(FlipBookConfig.getPlaySpeedMultiplier());
        correctLightIntensitySeekBarProgress(lightController,binding.seekBarLightIntensity);
        correctPlaySpeedSeekBarProgress(binding.seekBarPlaySpeed);

        surfaceView.setOnTouchListener((v, event) -> {

            setWidgetsVisiblility(View.VISIBLE);
            Rotation2 r = lightController.getRotation2();
            updateLightRotateTexts(r);
            v.removeCallbacks(makeWidgetsInvisible);
            v.postDelayed(makeWidgetsInvisible,  vanishDelay);
            return true;
        });



        abstract class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                setWidgetsVisiblility(View.VISIBLE);
                surfaceView.removeCallbacks(makeWidgetsInvisible);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                surfaceView.postDelayed(makeWidgetsInvisible,vanishDelay);
            }

        }

        binding.seekBarLightRotateX.setOnSeekBarChangeListener(new MySeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lightController.setLightDirectionHorizontal(progress);
                lightController.updateToShader();
                Rotation2 r = lightController.getRotation2();
                updateLightRotateTexts(r);

            }


        });

        binding.seekBarLightRotateY.setOnSeekBarChangeListener(new MySeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lightController.setLightDirectionVertical(progress);
                lightController.updateToShader();
                Rotation2 r = lightController.getRotation2();
                updateLightRotateTexts(r);
            }


        });




        binding.seekBarLightIntensity.setOnSeekBarChangeListener(new MySeekBarChangeListener() {
            private float seekBarProgressToIntensity(SeekBar seekBar,int progress){
                int min = seekBar.getMin();
                int max = seekBar.getMax();
                float maxIntensity = lightController.getMaxLightIntensity();
                float minIntensity = lightController.getMinLightIntensity();
                float p = (float)progress/(float)(max - min);
                float intensity = minIntensity + (maxIntensity - minIntensity)*p;
                return intensity;
            }



            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float intensity = seekBarProgressToIntensity(seekBar,progress);
                lightController.setLightIntensity(intensity);
                lightController.updateToShader();
                updateLightIntensityTexts(intensity);
            }

        });

        binding.seekBarPlaySpeed.setOnSeekBarChangeListener(new MySeekBarChangeListener() {

            private float seekBarProgressToPlaySpeedMultiplier(SeekBar seekBar,int progress){
                int min = seekBar.getMin();
                int max = seekBar.getMax();
                float maxMultiplier = FlipBookConfig.getMaxPlaySpeedMultiplier();
                float minMultiplier = FlipBookConfig.getMinPlaySpeedMultiplier();
                float p = (float)progress/(float)(max - min);
                float multiplier = minMultiplier + (maxMultiplier - minMultiplier)*p;
                return multiplier;
            }


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float multiplier = seekBarProgressToPlaySpeedMultiplier(seekBar,progress);
                FlipBookConfig.setPlaySpeedMultiplier(multiplier);
                updatePlaySpeedTexts(multiplier);
            }
        });


        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Debug.startMethodTracing("sample");
        super.onCreate(savedInstanceState);
        if(!initialize()){
            System.exit(-1);
        }
        Debug.stopMethodTracing();
   //     EdgeToEdge.enable(this);


/*        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }


}