package me.project.cloud2drenderer.renderer.entity.model.shape;

import static android.opengl.GLES20.GL_STATIC_DRAW;

import me.project.cloud2drenderer.renderer.entity.model.MeshModel;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.VertexAttribute;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.VertexDataClass;

@VertexDataClass
public class Rectangle extends MeshModel {

    @VertexAttribute(order = 1, elemCnt = 3)
    private float[] aPosition;
    public Rectangle(){
        vertexData = new float[]{
          0,0,0,
          1,0,0,
          1,1,0,
          0,1,0
        };
        drawMethod = GL_STATIC_DRAW;
        vertexCount = 4;
        vertexIndices = new int[]{
                0,1,2,
                0,2,3,

        };
      //  vertexIndices = null;
    }
}
