package me.project.cloud2drenderer.renderer.entity.model.shape;

import static android.opengl.GLES20.GL_STATIC_DRAW;

import me.project.cloud2drenderer.renderer.entity.model.MeshModel;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.VertexAttribute;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.VertexDataClass;

@VertexDataClass
public class Rectangle extends MeshModel {

    @VertexAttribute(order = 1, elemCnt = 3)
    private final float[] aPosition;

    @VertexAttribute(order = 2, elemCnt = 2)
    private final float[] aTexCoords;

    public Rectangle() {
        aPosition = new float[]{
                0, 0, 0,
                1, 0, 0,
                1, 1, 0,
                0, 1, 0
        };
        aTexCoords = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };
        vertexCount = aPosition.length/3;
        vertexIndices = new int[]{
                0, 1, 2,
                2, 3, 0
        };
        boolean invertV = true;
        if(invertV) {
            for (int i = 1; i < aTexCoords.length; i += 2) {
                aTexCoords[i] = 1 - aTexCoords[i];
            }
        }

        int totalSize = aPosition.length + aTexCoords.length;
        vertexData = new float[totalSize];
        int posIndex = 0;
        int texIndex = 0;
        for (int i = 0; i < totalSize; ) {
            vertexData[i++] = aPosition[posIndex++];
            vertexData[i++] = aPosition[posIndex++];
            vertexData[i++] = aPosition[posIndex++];
            vertexData[i++] = aTexCoords[texIndex++];
            vertexData[i++] = aTexCoords[texIndex++];
        }
        drawMethod = GL_STATIC_DRAW;
    }
}
