package me.project.cloud2drenderer.renderer.procedure.binding.glresource;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;

import me.project.cloud2drenderer.opengl.glresource.shader.GLShaderProgram;
import me.project.cloud2drenderer.opengl.glresource.texture.GLTexture;
import me.project.cloud2drenderer.opengl.statemanager.GLShaderManager;
import me.project.cloud2drenderer.opengl.statemanager.GLTextureManager;
import me.project.cloud2drenderer.opengl.statemanager.GLVertexBufferManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
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
