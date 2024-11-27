package me.project.cloud2drenderer.renderer.entity.model.shape;

import static android.opengl.GLES20.GL_STATIC_DRAW;

import me.project.cloud2drenderer.renderer.entity.model.MeshModel;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.VertexAttribute;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.VertexDataClass;
import me.project.cloud2drenderer.util.MatUtils;
import me.project.cloud2drenderer.util.ModelUtils;

@VertexDataClass
public class Rectangle extends MeshModel {

    @VertexAttribute(order = 1, elemCnt = 3)
    private final float[] aPosition;

    @VertexAttribute(order = 2, elemCnt = 2)
    private final float[] aTexCoords;

    @VertexAttribute(order = 3, elemCnt = 3)
    private final float[] aNormal;

    @VertexAttribute(order = 4, elemCnt = 3)
    private final float[] aTangent;

    @VertexAttribute(order = 5, elemCnt = 3)
    private final float[] aBitangent;


    void copyToVertexData(){
        int totalSize = aPosition.length + aTexCoords.length + aNormal.length + aTangent.length + aBitangent.length;
        vertexData = new float[totalSize];
        int posIndex = 0;
        int texIndex = 0;
        int normalIndex = 0;
        int tangentIndex = 0;
        int bitangentIndex = 0;
        for (int i = 0; i < totalSize; ) {
            vertexData[i++] = aPosition[posIndex++];
            vertexData[i++] = aPosition[posIndex++];
            vertexData[i++] = aPosition[posIndex++];
            vertexData[i++] = aTexCoords[texIndex++];
            vertexData[i++] = aTexCoords[texIndex++];
            vertexData[i++] = aNormal[normalIndex++];
            vertexData[i++] = aNormal[normalIndex++];
            vertexData[i++] = aNormal[normalIndex++];
            vertexData[i++] = aTangent[tangentIndex++];
            vertexData[i++] = aTangent[tangentIndex++];
            vertexData[i++] = aTangent[tangentIndex++];
            vertexData[i++] = aBitangent[bitangentIndex++];
            vertexData[i++] = aBitangent[bitangentIndex++];
            vertexData[i++] = aBitangent[bitangentIndex++];
        }
    }



    public void generateTangents(){
        float[][][] tangents = new float[vertexIndices.length][][];
        for(int i = 0;i<vertexIndices.length;i++){
            tangents[i] = new float[2][3];
        }

        for(int i = 0;i<vertexIndices.length;) {
            int vertIdx1 = vertexIndices[i++];
            int vertIdx2 = vertexIndices[i++];
            int vertIdx3 = vertexIndices[i++];
            float[] pos1 = new float[3];
            float[] uv1 = new float[2];
            float[] pos2 = new float[3];
            float[] uv2 = new float[2];
            float[] pos3 = new float[3];
            float[] uv3 = new float[2];
            int posIdx = vertIdx1*3;
            int texCoordsIdx = vertIdx1*2;
            MatUtils.arrayForEach((d,s)->s,pos1, aPosition,0,posIdx,posIdx+3);
            MatUtils.arrayForEach((d,s)->s,uv1, aTexCoords,0,texCoordsIdx,texCoordsIdx+2);
            posIdx = vertIdx2*3;
            texCoordsIdx = vertIdx2*2;
            MatUtils.arrayForEach((d,s)->s,pos2, aPosition,0,posIdx,posIdx+3);
            MatUtils.arrayForEach((d,s)->s,uv2, aTexCoords,0,texCoordsIdx,texCoordsIdx+2);
            posIdx = vertIdx3*3;
            texCoordsIdx = vertIdx3*2;
            MatUtils.arrayForEach((d,s)->s,pos3, aPosition,0,posIdx,posIdx+3);
            MatUtils.arrayForEach((d,s)->s,uv3, aTexCoords,0,texCoordsIdx,texCoordsIdx+2);

            float[][] tan = ModelUtils.getTangents(pos1, pos2, pos3, uv1, uv2, uv3);
            MatUtils.arrayForEach(Float::sum,tangents[vertIdx1][0],tan[0]);
            MatUtils.arrayForEach(Float::sum,tangents[vertIdx1][1],tan[1]);
            MatUtils.arrayForEach(Float::sum,tangents[vertIdx2][0],tan[0]);
            MatUtils.arrayForEach(Float::sum,tangents[vertIdx2][1],tan[1]);
            MatUtils.arrayForEach(Float::sum,tangents[vertIdx3][0],tan[0]);
            MatUtils.arrayForEach(Float::sum,tangents[vertIdx3][1],tan[1]);
        }
        for (int vertIdx : vertexIndices) {
            float[] normal = new float[3];
            MatUtils.arrayForEach((d, s) -> s, normal, aNormal, 0, vertIdx * 3, vertIdx * 3 + 3);
            ModelUtils.orthogonalizeTangents(tangents[vertIdx][0], tangents[vertIdx][1], normal);
            MatUtils.arrayForEach((d,s)->s,aTangent,tangents[vertIdx][0],vertIdx*3);
            MatUtils.arrayForEach((d,s)->s,aBitangent,tangents[vertIdx][1],vertIdx*3);
        }

    }

    public static float[] getCentroid(){
        return new float[]{0,0,0};
    }

    public static float[] getNormal(){
        return new float[]{0,0,1};
    }

    public static float[] getBillboardTransform(float[] eyePosOS,float[] position){
        float[] centroid = getCentroid();
        float[] zNormBS = MatUtils.vec4(MatUtils.sub(eyePosOS,centroid),0);
        MatUtils.normalize(zNormBS);
        float[] yUp = Math.abs(zNormBS[1]) > 0.999 ? MatUtils.vec3(0, 0, 1) : MatUtils.vec3(0, 1, 0);
        float[] xRight = MatUtils.cross(yUp, zNormBS);
        MatUtils.normalize(xRight);
        float[] realY = MatUtils.cross(zNormBS,xRight);
        float[] offsetVec = MatUtils.sub(position,centroid);
        return MatUtils.newTransform(xRight,realY,zNormBS,offsetVec);
    }


    public Rectangle() {
        aPosition = new float[]{
                -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0.5f, 0.5f, 0,
                -0.5f, 0.5f, 0
        };
        aTexCoords = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };
        aNormal = new float[]{
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1
        };

        vertexCount = aPosition.length/3;
        aTangent = new float[vertexCount*3];
        aBitangent = new float[vertexCount*3];

        vertexIndices = new int[]{
                0, 1, 2,
                2, 3, 0
        };

        generateTangents();

        boolean invertV = false;
        boolean rightHandedTS = false;
        if(invertV) {
            for (int i = 1; i < aTexCoords.length; i += 2) {
                aTexCoords[i] = 1 - aTexCoords[i];
            }
        }
        if(rightHandedTS){
            for(int i=0;i<aBitangent.length;i++){
                aBitangent[i] = -aBitangent[i];
            }
        }

        copyToVertexData();
        drawMethod = GL_STATIC_DRAW;
    }
}
