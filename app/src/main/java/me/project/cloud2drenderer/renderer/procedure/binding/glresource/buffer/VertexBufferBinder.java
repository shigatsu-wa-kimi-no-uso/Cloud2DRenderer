package me.project.cloud2drenderer.renderer.procedure.binding.glresource.buffer;

import me.project.cloud2drenderer.opengl.statemanager.GLVertexBufferManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.GLResourceBinder;

public class VertexBufferBinder implements GLResourceBinder {


    @Override
    public void bind(RenderContext context) {
        GLVertexBufferManager.bind(context.loadedModel.vertexBuffer);
        GLVertexBufferManager.bindVertexBuffer();
    }
}
