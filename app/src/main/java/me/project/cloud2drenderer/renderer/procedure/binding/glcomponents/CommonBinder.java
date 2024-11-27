package me.project.cloud2drenderer.renderer.procedure.binding.glcomponents;

import android.util.Log;

import me.project.cloud2drenderer.opengl.statemanager.GLShaderManager;
import me.project.cloud2drenderer.opengl.statemanager.GLTextureManager;
import me.project.cloud2drenderer.opengl.statemanager.GLVertexBufferManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.controller.ShaderController;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class CommonBinder implements ResBindingMethod {

    private static final String tag = CommonBinder.class.getSimpleName();

    @Override
    public void bind(RenderContext context) {
        //Log.i(tag,"binding render context:" + context.name);
        GLShaderManager.use(context.getShader().program);
        for (Texture texture : context.getTextures()) {
            GLTextureManager.bind(texture.texture,texture.unit);
        }
        GLVertexBufferManager.bind(context.loadedModel.vertexBuffer);
        GLVertexBufferManager.bindVertexBuffer();
        context.bindShaderAttributePointers();
    }
}
