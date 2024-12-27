package me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader;

import me.project.cloud2drenderer.opengl.statemanager.GLShaderManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.GLResourceBinder;



public class ShaderBinder implements GLResourceBinder {
    @Override
    public void bind(RenderContext context) {
        GLShaderManager.use(context.getShader().program);
    }
}
