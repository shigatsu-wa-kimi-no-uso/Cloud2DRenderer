package me.project.cloud2drenderer.renderer.procedure.drawing;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_INT;

import me.project.cloud2drenderer.opengl.statemanager.GLCanvasManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;

public class ElemDraw implements DrawMethod{
    @Override
    public void draw(RenderContext context) {
        GLCanvasManager.drawElements(GL_TRIANGLES,context.loadedModel.drawnVertexCount,GL_UNSIGNED_INT,0);
    }
}
