package me.project.cloud2drenderer.renderer.entity.others.light;

import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformVar;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;

public class DistantLight {

    private float[] intensity;

    private float[] direction;

    public class ManualCommitUniform{
        @ShaderUniform(uniformName = "color",flags = {})
        final public UniformVar<float[]> colorWrapper = new UniformVar<>();

        @ShaderUniform(uniformName = "intensity",flags = {})
        final public UniformVar<float[]> intensityWrapper = new UniformVar<>();

        @ShaderUniform(uniformName = "direction",flags = {})
        final public UniformVar<float[]> directionWrapper = new UniformVar<>();
    }

    @ShaderUniform(flags = UniformFlag.IS_STRUCT)
    public ManualCommitUniform manualCommits = new ManualCommitUniform();

   // @ShaderUniform(uniformName = "color")
    public float[] getColor() {
        return intensity;
    }

   // @ShaderUniform(uniformName = "intensity")
    public float[] getIntensity() {
        return intensity;
    }



    public void setIntensity(float[] intensity) {
        this.intensity = intensity;
        manualCommits.intensityWrapper.setValue(intensity);
    }

   // @ShaderUniform(uniformName = "direction")
    public float[] getDirection() {
        return direction;
    }

    public void setDirection(float[] direction) {
        this.direction = direction;
        manualCommits.directionWrapper.setValue(direction);
    }
}
