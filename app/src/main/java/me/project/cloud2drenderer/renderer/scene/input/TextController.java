package me.project.cloud2drenderer.renderer.scene.input;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.TextView;

import java.util.Locale;
import java.util.logging.Handler;

public class TextController {

    private final Activity activity;

    private TextView rotateXText;
    private TextView rotateYText;

    private TextView rotateZText;

    public TextController(Activity activity)
    {
        this.activity = activity;
    }

    public void setLightRotateTexts(TextView rotateXText, TextView rotateYText, TextView intensityText)
    {
        this.rotateXText = rotateXText;
        this.rotateYText = rotateYText;
        this.rotateZText = intensityText;
        rotateXText.setVisibility(TextView.INVISIBLE);
        rotateYText.setVisibility(TextView.INVISIBLE);
        intensityText.setVisibility(TextView.INVISIBLE);
    }


    public void updateLightRotateTexts(Rotation rotation)
    {
        activity.runOnUiThread(() -> {
            rotateXText.setVisibility(TextView.VISIBLE);
            rotateYText.setVisibility(TextView.VISIBLE);
            rotateZText.setVisibility(TextView.VISIBLE);
            rotateXText.setText(String.format(Locale.getDefault(),"Rotate X: %.2f",rotation.x));
            rotateYText.setText(String.format(Locale.getDefault(),"Rotate Y: %.2f",rotation.y));
            rotateZText.setText(String.format(Locale.getDefault(),"Rotate Z: %.2f",rotation.z));
            rotateXText.postDelayed(() -> rotateXText.setVisibility(TextView.INVISIBLE),3000);
            rotateYText.postDelayed(() -> rotateYText.setVisibility(TextView.INVISIBLE),3000);
            rotateZText.postDelayed(() -> rotateZText.setVisibility(TextView.INVISIBLE),3000);
        });

    }

    public void updateLightRotateTexts(Rotation2 rotation)
    {
        activity.runOnUiThread(() -> {
            rotateXText.setVisibility(TextView.VISIBLE);
            rotateYText.setVisibility(TextView.VISIBLE);
            rotateZText.setVisibility(TextView.INVISIBLE);
            rotateXText.setText(String.format(Locale.getDefault(),"Azimuth: %d°",(int)rotation.getHorizontal()));
            rotateYText.setText(String.format(Locale.getDefault(),"Zenith: %d°",(int)rotation.getVertical()));

        //    rotateZText.postDelayed(() -> rotateZText.setVisibility(TextView.INVISIBLE),3000);
        });

    }


}
