package me.project.cloud2drenderer.renderer.procedure.drawing;

import static android.opengl.GLES20.GL_TRIANGLES;

import me.project.cloud2drenderer.opengl.statemanager.GLCanvasManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;

public class ArrayDraw implements DrawMethod{

    @Override
    public void draw(RenderContext context) {
        GLCanvasManager.drawArrays(GL_TRIANGLES,0,context.loadedModel.vertexCount);
    }
}
