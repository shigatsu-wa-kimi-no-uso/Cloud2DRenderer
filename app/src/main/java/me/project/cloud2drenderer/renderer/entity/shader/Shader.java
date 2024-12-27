package me.project.cloud2drenderer.renderer.entity.shader;

import java.util.Map;

import me.project.cloud2drenderer.opengl.glresource.shader.GLShaderProgram;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.ShaderVariableMeta;

public class Shader {

    public String name;

    public GLShaderProgram program;

    private Map<String, ShaderVariableMeta> uniformMetas;


    private Map<String, ShaderVariableMeta> attributeMetas;


    public Map<String, ShaderVariableMeta> getUniformMetas() {
        return uniformMetas;
    }

    public void setUniformMetas(Map<String, ShaderVariableMeta> uniformMetas) {
        this.uniformMetas = uniformMetas;
    }

    public Map<String, ShaderVariableMeta> getAttributeMetas() {
        return attributeMetas;
    }

    public void setAttributeMetas(Map<String, ShaderVariableMeta> attributeMetas) {
        this.attributeMetas = attributeMetas;
    }

}
