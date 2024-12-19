package me.project.cloud2drenderer.renderer.procedure.pipeline;

import java.util.Collection;
import java.util.Map;

import me.project.cloud2drenderer.renderer.context.RenderContext;



public abstract class RenderPipeline {

    protected Collection<RenderContext> contexts;

    public abstract void setContexts(Collection<RenderContext> contexts);
    public abstract void beforeTask();
    public abstract void run();
    public abstract void afterTask();

}
