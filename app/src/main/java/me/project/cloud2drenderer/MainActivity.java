package me.project.cloud2drenderer;

import static android.opengl.GLES20.glGetString;

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import me.project.cloud2drenderer.databinding.ActivityMainBinding;
import me.project.cloud2drenderer.renderer.scene.input.InputController;

public class MainActivity extends AppCompatActivity {

    private final static String tag = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;

    String getGLESVersion(){
        final ActivityManager activityManager=(ActivityManager)getSystemService(ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo=activityManager.getDeviceConfigurationInfo();
        return configurationInfo.getGlEsVersion();
    }
    private GLSurfaceView.Renderer renderer;
    private GLSurfaceView surfaceView;

    private View view;

    private InputController inputController;

    private boolean initialize(){

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        surfaceView = binding.glSurfaceView;
        view = binding.getRoot();
        setContentView(view);
        binding.moveImageButton.setVisibility(View.INVISIBLE);
        binding.rotateImageButton.setVisibility(View.INVISIBLE);
        binding.upDownImageButton.setVisibility(View.INVISIBLE);
        binding.resetImageButton.setVisibility(View.INVISIBLE);

        inputController = new InputController(this,
                binding.moveImageButton,
                binding.rotateImageButton,
                binding.upDownImageButton,
                binding.resetImageButton);
        renderer = new GLRenderer(this,binding.fpsTextView,binding.cameraTextView,inputController);


    //    surfaceView.setEGLConfigChooser(new MultisampleConfigChooser());
        surfaceView.setEGLContextClientVersion(3);
        surfaceView.setRenderer(renderer);
     //   surfaceView.setOnTouchListener(inputController);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!initialize()){
            System.exit(-1);
        }
   //     EdgeToEdge.enable(this);


/*        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }


}