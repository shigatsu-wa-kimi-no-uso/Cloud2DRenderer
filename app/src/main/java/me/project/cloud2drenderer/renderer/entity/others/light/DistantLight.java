package me.project.cloud2drenderer.renderer.entity.others.light;

import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformVar;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;

public class DistantLight {

    private float[] intensity = new float[]{1,1,1};

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
        this.intensity[0] = intensity[0];
        this.intensity[1] = intensity[1];
        this.intensity[2] = intensity[2];
        manualCommits.intensityWrapper.setValue(this.intensity);
    }

    public void setIntensity(float intensity) {
        this.intensity[0] = intensity;
        this.intensity[1] = intensity;
        this.intensity[2] = intensity;
        manualCommits.intensityWrapper.setValue(this.intensity);
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
