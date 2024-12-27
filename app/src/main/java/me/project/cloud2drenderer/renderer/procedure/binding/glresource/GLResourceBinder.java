package me.project.cloud2drenderer.renderer.procedure.binding.glresource;

import me.project.cloud2drenderer.renderer.context.RenderContext;

@FunctionalInterface
public interface GLResourceBinder {
    void bind(RenderContext context);
}
