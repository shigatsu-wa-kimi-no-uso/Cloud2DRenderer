package me.project.cloud2drenderer.renderer.procedure.pipeline;


import androidx.annotation.NonNull;

import java.util.Collection;

import me.project.cloud2drenderer.renderer.context.RenderContext;

public class CommonPipeline extends RenderPipeline {

    @Override
    public void setContexts(Collection<RenderContext> contexts) {
        this.contexts = contexts;
    }

    @Override
    public void beforeTask() {

    }

    @Override
    public void run() {
        for(RenderContext context : contexts){
            context.bindGLResources();
            context.applyUniformAssignments();
            context.draw();
        }
    }



    @Override
    public void afterTask() {

    }
}
