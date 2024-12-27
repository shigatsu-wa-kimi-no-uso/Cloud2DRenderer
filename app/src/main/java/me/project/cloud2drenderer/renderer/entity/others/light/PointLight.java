package me.project.cloud2drenderer.renderer.entity.others.light;

import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;

public class PointLight {

    private float[] intensity;

    private float[] position;

    @ShaderUniform(uniformName = "color")
    public float[] getColor() {
        return intensity;
    }

    @ShaderUniform(uniformName = "intensity")
    public float[] getIntensity() {
        return intensity;
    }



    public void setIntensity(float[] intensity) {
        this.intensity = intensity;
    }

    @ShaderUniform(uniformName = "position")
    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }
}
