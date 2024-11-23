package me.project.cloud2drenderer.util;

public class ModelUtils {


    // 不进行归一化，不进行正交化
    public static float[][] getTangents(float[] pos1, float[] pos2, float[] pos3, float[] uv1, float[] uv2, float[] uv3){
        float[] edge1 = MatUtils.sub(pos2,pos1);
        float[] edge2 = MatUtils.sub(pos3,pos1);
        float[] deltaUV1 = MatUtils.sub(uv2,uv1);
        float[] deltaUV2 = MatUtils.sub(uv3,uv1);
        float f = 1.0f/(deltaUV1[0]*deltaUV2[1] - deltaUV2[0]*deltaUV1[1]);
        float[][] tangents = new float[3][2];
        float[] tangent = new float[3];
        float[] bitangent = new float[3];
        tangents[0] = tangent;
        tangents[1] = bitangent;

//        tangent1.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
//        tangent1.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
//        tangent1.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
//        tangent1 = glm::normalize(tangent1);
//
//        bitangent1.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
//        bitangent1.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
//        bitangent1.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
//        bitangent1 = glm::normalize(bitangent1);

        tangent[0] = f*(deltaUV2[1]*edge1[0] - deltaUV1[1]*edge2[0]);
        tangent[1] = f*(deltaUV2[1]*edge1[1] - deltaUV1[1]*edge2[1]);
        tangent[2] = f*(deltaUV2[1]*edge1[2] - deltaUV1[1]*edge2[2]);

        bitangent[0] = f*(-deltaUV2[0]*edge1[0] + deltaUV1[0]*edge2[0]);
        bitangent[1] = f*(-deltaUV2[0]*edge1[1] + deltaUV1[0]*edge2[1]);
        bitangent[2] = f*(-deltaUV2[0]*edge1[2] + deltaUV1[0]*edge2[2]);

        return tangents;
    }

    public static void orthogonalizeTangents(float[] tangent, float[] bitangent, float[] normal){
        float[] orthoTangent = MatUtils.orthogonalized(normal,tangent);
        float[] orthoBitangent  = MatUtils.normalized(MatUtils.cross(normal,tangent));
        MatUtils.arrayForEach((d,s)->s,tangent,orthoTangent);
        MatUtils.arrayForEach((d,s)->s,bitangent,orthoBitangent);
    }




}
