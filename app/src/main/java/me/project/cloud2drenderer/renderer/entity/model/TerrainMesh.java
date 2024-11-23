package me.project.cloud2drenderer.renderer.entity.model;

import static android.opengl.GLES20.GL_STATIC_DRAW;

import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.VertexAttribute;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.VertexDataClass;

@VertexDataClass
public class TerrainMesh extends MeshModel{

    @VertexAttribute(order = 1, elemCnt = 3)
    private final float[] aPosition;

    @VertexAttribute(order = 2, elemCnt = 2)
    private final float[] aTexCoords;
    private void setupTerrainMesh(float[] xbound,float[] zbound,float[] ubound,float[] vbound,int xCount,int zCount) {
        int gridCntX =  xCount - 1;
        int gridCntZ = zCount - 1;
        float gridLenX = (xbound[1] - xbound[0]) / gridCntX;
        float gridLenZ = (zbound[1] - zbound[0]) / gridCntZ;
        float gridLenU = (ubound[1] - ubound[0])/ gridCntX;
        float gridLenV = (vbound[1] - vbound[0]) / gridCntZ;
        int vertIdx = 0;
        int texCoordIdx = 0;
        for (int i = 0; i < xCount; i++) {
            float xPos = xbound[0] + i * gridLenX;
            float uPos = ubound[0] + i * gridLenU;
            for (int j = 0; j < zCount; j++) {
                float zPos = zbound[0] + j * gridLenZ;
                float vPos = vbound[0] + j * gridLenV;
                aPosition[vertIdx++] = xPos; //x
                aPosition[vertIdx++] = 0;   //y
                aPosition[vertIdx++] = zPos; //z
                aTexCoords[texCoordIdx++] = uPos;
                aTexCoords[texCoordIdx++] = vPos;
            }
        }
        int gridIdx = 0;
        for (int i = 0; i < gridCntX; ++i) {
            for (int j = 0; j < gridCntZ; ++j) {
                vertexIndices[gridIdx++] = i * gridCntZ + j;
                vertexIndices[gridIdx++] = i * gridCntZ + j + 1;
                vertexIndices[gridIdx++] = (i + 1)*gridCntZ + j;
                vertexIndices[gridIdx++] = (i + 1)*gridCntZ + j;
                vertexIndices[gridIdx++] = i * gridCntZ + j + 1;
                vertexIndices[gridIdx++] = (i + 1)*gridCntZ + j + 1;
            }
        }
    }

    public TerrainMesh(int xCount,int zCount) {
        int vertCnt = xCount * zCount;
        int gridCnt =  (xCount - 1) * (zCount - 1);
        aPosition = new float[vertCnt*3];
        aTexCoords = new float[vertCnt*2];
        vertexCount = aPosition.length/3;
        vertexIndices = new int[gridCnt * 6];
        float[] xbound = new float[]{-0.5f,0.5f};
        float[] zbound = new float[]{-0.5f,0.5f};
        float[] ubound = new float[]{0f,1f};
        float[] vbound = new float[]{0f,1f};
        setupTerrainMesh(xbound,zbound,ubound,vbound,xCount,zCount);
        boolean invertV = false;
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
