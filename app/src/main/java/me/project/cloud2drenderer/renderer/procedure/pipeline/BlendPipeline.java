package me.project.cloud2drenderer.renderer.procedure.pipeline;

import static android.opengl.GLES32.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.Vector;

import me.project.cloud2drenderer.opengl.statemanager.GLCanvasManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;

public class BlendPipeline extends RenderPipeline{

    private final Vector<RenderContext> sortedContexts = new Vector<>();

    @Override
    public void setContexts(Collection<RenderContext> contexts) {
        sortedContexts.clear();
        sortedContexts.addAll(contexts);
    }

    @Override
    public void beforeTask() {
        //具有透明度需要blend的物体, 按Z轴排序, 先画远的, 再画近的
        sortedContexts.sort((o1, o2) -> {
            float delta = o1.getTransform()[14] - o2.getTransform()[14];
            if (delta < 0) {
                return -1;
            } else if (delta == 0) {
                return 0;
            } else {
                return 1;
            }
        });
       /* GLCanvasManager.storeCapabilityStatus(GL_BLEND);
        GLCanvasManager.storeCapabilityStatus(GL_CULL_FACE);
        GLCanvasManager.enableGLFunction(GL_BLEND);
        GLCanvasManager.enableGLFunction(GL_CULL_FACE);
        GLCanvasManager.setBlendFunction(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);*/
    }


    @Override
    public void run() {
        for(RenderContext context : sortedContexts){
            context.bindGLResources();
            context.applyUniformAssignments();
            context.draw();
        }
    }

    @Override
    public void afterTask() {
      //  GLCanvasManager.restoreCapabilityStatus();
    }
}
