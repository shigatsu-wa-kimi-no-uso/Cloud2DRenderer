package me.project.cloud2drenderer.renderer.procedure.pipeline;


import androidx.annotation.NonNull;

import java.util.Collection;

import me.project.cloud2drenderer.renderer.context.RenderContext;

public class CommonPipeline extends RenderPipeline {

    @Override
    public void beforeTask() {

    }

    public void run(RenderContext context) {
        context.bindGLResources();
        context.applyUniformAssignments();
        context.draw();
    }

    @Override
    public void afterTask() {

    }
}
