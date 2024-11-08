package me.project.cloud2drenderer.renderer.procedure.pipeline;

import static android.opengl.GLES20.*;

import me.project.cloud2drenderer.opengl.statemanager.GLCanvasManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;

public class NonBlendPipeline extends RenderPipeline{




    @Override
    public void beforeTask() {
        GLCanvasManager.storeCapabilityStatus(GL_BLEND);
        GLCanvasManager.disableGLFunction(GL_BLEND);
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
