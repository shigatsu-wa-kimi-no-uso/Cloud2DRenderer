package me.project.cloud2drenderer.renderer.entity.others.light;

import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.ShaderUniform;

public class DistantLight {

    private float[] intensity;

    private float[] direction;

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

    @ShaderUniform(uniformName = "direction")
    public float[] getDirection() {
        return direction;
    }

    public void setDirection(float[] direction) {
        this.direction = direction;
    }
}
