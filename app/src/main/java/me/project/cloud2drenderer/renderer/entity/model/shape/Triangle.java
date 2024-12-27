package me.project.cloud2drenderer.renderer.entity.model.shape;


import static android.opengl.GLES20.GL_STATIC_DRAW;

import me.project.cloud2drenderer.renderer.entity.model.MeshModel;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.VertexAttribute;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.VertexDataClass;


@VertexDataClass
public class Triangle extends MeshModel {

    @VertexAttribute(order = 1,elemCnt = 3)
    private float[] aPosition;
    public Triangle(){
        vertexData = new float[]{
                0.0f,0.5f,0.0f,
                -0.5f,0.0f,0.0f,
                0.5f,0.0f,0.0f
        };
        vertexCount = 3;
        vertexIndices = null;
        drawMethod = GL_STATIC_DRAW;
    }

}
