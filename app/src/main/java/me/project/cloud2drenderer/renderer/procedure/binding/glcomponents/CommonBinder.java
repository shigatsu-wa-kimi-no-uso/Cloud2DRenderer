package me.project.cloud2drenderer.renderer.procedure.binding.glcomponents;

import me.project.cloud2drenderer.opengl.statemanager.GLShaderManager;
import me.project.cloud2drenderer.opengl.statemanager.GLTextureManager;
import me.project.cloud2drenderer.opengl.statemanager.GLVertexBufferManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class CommonBinder implements ResBindingMethod {
    @Override
    public void bind(RenderContext context) {
        GLShaderManager.use(context.getShader().program);
        for (Texture texture : context.getTextures()) {
            GLTextureManager.bind(texture.texture,texture.unit);
        }
        GLVertexBufferManager.bind(context.loadedModel.vertexBuffer);
    }
}
