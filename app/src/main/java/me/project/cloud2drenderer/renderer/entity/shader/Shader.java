package me.project.cloud2drenderer.renderer.entity.shader;

import java.util.Map;

import me.project.cloud2drenderer.opengl.glcomponent.shader.GLShaderProgram;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.ShaderUniformMeta;

public class Shader {

    public String name;

    public GLShaderProgram program;

    private Map<String,ShaderUniformMeta> uniformMetas;


    public Map<String, ShaderUniformMeta> getUniformMetas() {
        return uniformMetas;
    }

    public void setUniformMetas(Map<String, ShaderUniformMeta> uniformMetas) {
        this.uniformMetas = uniformMetas;
    }
}
