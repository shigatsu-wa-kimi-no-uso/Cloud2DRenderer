package me.project.cloud2drenderer.renderer.procedure.binding.glcomponents;

import me.project.cloud2drenderer.opengl.statemanager.GLShaderManager;
import me.project.cloud2drenderer.opengl.statemanager.GLVertexBufferManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;

public class NonTextureBinder implements ResBindingMethod {
    @Override
    public void bind(RenderContext context) {
        GLShaderManager.use(context.getShader().program);
        GLVertexBufferManager.bind(context.loadedModel.vertexBuffer);
    }
}
