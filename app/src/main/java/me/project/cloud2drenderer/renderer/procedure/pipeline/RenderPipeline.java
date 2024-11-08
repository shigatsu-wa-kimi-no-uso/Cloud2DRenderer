package me.project.cloud2drenderer.renderer.procedure.pipeline;

import java.util.Collection;

import me.project.cloud2drenderer.renderer.context.RenderContext;



public abstract class RenderPipeline {

    public abstract void beforeTask();
    public abstract void run(RenderContext context);
    public abstract void afterTask();

}
