package me.project.cloud2drenderer.renderer.procedure.binding.glresource.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;


@FunctionalInterface
public interface TextureSetter {
    void setTexture(Texture texture);
}
