package me.project.cloud2drenderer.renderer.scene.input;

import android.widget.SeekBar;
import android.widget.TextView;

import me.project.cloud2drenderer.renderer.context.flipbook.SixWayLightingRenderContext;
import me.project.cloud2drenderer.renderer.entity.others.light.DistantLight;

public class LightController {

    private DistantLight light;

    private Rotation2 rotation;



    public LightController(DistantLight light)
    {
        this.light = light;
        this.rotation = new Rotation2();
    }


    public void setLightDirectionFromRotate(float horizontal, float vertical)
    {
        rotation.setRotation2(horizontal,vertical);
        light.setDirection(rotation.getDirection());

    }

    public void setLightDirectionVertical(float vertical)
    {
        rotation.setVertical(vertical);
        light.setDirection(rotation.getDirection());
    }

    public void setLightIntensity(float intensity)
    {
        light.setIntensity(intensity);
    }

    public float getLightIntensity(){
        return light.getIntensity()[0];
    }

    public float getMaxLightIntensity(){
        return 2f;
    }
    public float getMinLightIntensity(){
        return 0.1f;
    }


    public Rotation2 getRotation2()
    {
        return rotation;
    }

    public void setLightDirectionHorizontal(float horizontal)
    {
        rotation.setHorizontal(horizontal);
        light.setDirection(rotation.getDirection());
    }

    public void updateToShader(){
        SixWayLightingRenderContext.updateDistinctLightToShader();
    }


}
