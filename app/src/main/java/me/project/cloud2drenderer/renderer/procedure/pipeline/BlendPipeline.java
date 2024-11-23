package me.project.cloud2drenderer.renderer.procedure.pipeline;

import static android.opengl.GLES32.*;

import me.project.cloud2drenderer.opengl.statemanager.GLCanvasManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;

public class BlendPipeline extends RenderPipeline{
    @Override
    public void beforeTask() {
        GLCanvasManager.storeCapabilityStatus(GL_BLEND);
        GLCanvasManager.storeCapabilityStatus(GL_CULL_FACE);
        GLCanvasManager.enableGLFunction(GL_BLEND);
        GLCanvasManager.enableGLFunction(GL_CULL_FACE);
        GLCanvasManager.setBlendFunction(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void run(RenderContext context) {
        context.bindGLResources();
        context.applyUniformAssignments();
        context.draw();
    }

    @Override
    public void afterTask() {
        GLCanvasManager.restoreCapabilityStatus();
    }
}
