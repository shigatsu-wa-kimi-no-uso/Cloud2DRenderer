package me.project.cloud2drenderer.renderer.procedure.binding.glresource.material;

import me.project.cloud2drenderer.opengl.statemanager.GLTextureManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.GLResourceBinder;

public class TextureBinder implements GLResourceBinder {
    @Override
    public void bind(RenderContext context) {
        for (Texture texture : context.getTextures()) {
            GLTextureManager.bind(texture.texture,texture.unit);
        }
    }
}
