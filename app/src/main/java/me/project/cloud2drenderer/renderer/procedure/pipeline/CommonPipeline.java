package me.project.cloud2drenderer.renderer.procedure.pipeline;


import androidx.annotation.NonNull;

import java.util.Collection;

import me.project.cloud2drenderer.renderer.context.RenderContext;

public class CommonPipeline extends RenderPipeline {

    public void run(@NonNull Collection<RenderContext> contexts){
        for (RenderContext context : contexts){
            context.bindGLResources();
            context.applyUniformAssignments();
            context.draw();
        }
    }
}
