package me.project.cloud2drenderer;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import me.project.cloud2drenderer.databinding.ActivityMainBinding;
import me.project.cloud2drenderer.renderer.scene.input.InputController;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

/*    void showGLESVersion(){
        final ActivityManager activityManager=(ActivityManager)getSystemService(ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo=activityManager.getDeviceConfigurationInfo();
        //以16进制显示GLES版本
        String strResult = Integer.toString(configurationInfo.reqGlEsVersion, 16);
        binding.text.setText(strResult);
    }*/
    private GLSurfaceView.Renderer renderer;
    private GLSurfaceView surfaceView;

    private View view;

    private InputController inputController;

    private boolean initialize(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        surfaceView = binding.glSurfaceView;
        view = binding.getRoot();
        setContentView(view);
        inputController = new InputController(this,
                binding.moveImageButton,
                binding.rotateImageButton,
                binding.upDownImageButton,
                binding.resetImageButton);
        renderer = new GLRenderer(this,binding.fpsTextView,binding.cameraTextView,inputController);


    //    surfaceView.setEGLConfigChooser(new MultisampleConfigChooser());
        surfaceView.setEGLContextClientVersion(3);
        surfaceView.setRenderer(renderer);
        surfaceView.setOnTouchListener(inputController);
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