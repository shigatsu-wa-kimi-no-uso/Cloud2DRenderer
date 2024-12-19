package me.project.cloud2drenderer.util;

import android.opengl.Matrix;

import androidx.annotation.NonNull;

public class MatUtils {

    public static void setAffine(float[] mat4){
        mat4[15] = 1;
    }


    public static void arrayCopy(float[] dest,float[] src){
        System.arraycopy(src, 0, dest, 0, src.length);
    }


    public static float[] newTransform(float[] baseX,float[] baseY,float[] baseZ, float[] position) {
        float[] transform = new float[16];
        transform[0] = baseX[0];
        transform[1] = baseX[1];
        transform[2] = baseX[2];
        transform[4] = baseY[0];
        transform[5] = baseY[1];
        transform[6] = baseY[2];
        transform[8] = baseZ[0];
        transform[9] = baseZ[1];
        transform[10] = baseZ[2];
        transform[12] = position[0];
        transform[13] = position[1];
        transform[14] = position[2];
        transform[15] = 1;
        return transform;
    }


    public static void setTransform(float[] transform,float[] position, float[] scale, float[] rotation) {
        float[] scaleMat = newScaleMatrix(scale[0], scale[1], scale[2]);
        float[] rotMat = newRotationMatrix(rotation[0], rotation[1], rotation[2]);
        float[] transMat = newTranslationMatrix(position[0], position[1], position[2]);
        //注意！opengl库中的Matrix是列序存储，与shader中的对应，因此multiplyMM的实现是针对列序的，
        // 与正常行序存储是反过来的
        Matrix.multiplyMM(transform, 0, rotMat, 0, scaleMat, 0);
        Matrix.multiplyMM(transform, 0, transMat, 0, transform, 0);
    }

    public static float[] newTransform(float[] position, float[] scale, float[] rotation) {
        float[] transform = new float[16];
        setTransform(transform,position,scale,rotation);
        return transform;
    }

    public static float[] newTranslationMatrix(float posX, float posY, float posZ) {
        float[] translateMat = new float[16];
        Matrix.setIdentityM(translateMat, 0);
        Matrix.translateM(translateMat, 0, posX, posY, posZ);
        return translateMat;
    }

    // copied from package android.opengl.Matrix.setRotateEulerM2;
    public static void setRotateEuler(@NonNull float[] rm, int rmOffset,
                                      float x, float y, float z){
        x *= (float) (Math.PI / 180.0f);
        y *= (float) (Math.PI / 180.0f);
        z *= (float) (Math.PI / 180.0f);
        float cx = (float) Math.cos(x);
        float sx = (float) Math.sin(x);
        float cy = (float) Math.cos(y);
        float sy = (float) Math.sin(y);
        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);
        float cxsy = cx * sy;
        float sxsy = sx * sy;

        rm[rmOffset + 0]  =  cy * cz;
        rm[rmOffset + 1]  = -cy * sz;
        rm[rmOffset + 2]  =  sy;
        rm[rmOffset + 3]  =  0.0f;

        rm[rmOffset + 4]  =  sxsy * cz + cx * sz;
        rm[rmOffset + 5]  = -sxsy * sz + cx * cz;
        rm[rmOffset + 6]  = -sx * cy;
        rm[rmOffset + 7]  =  0.0f;

        rm[rmOffset + 8]  = -cxsy * cz + sx * sz;
        rm[rmOffset + 9]  =  cxsy * sz + sx * cz;
        rm[rmOffset + 10] =  cx * cy;
        rm[rmOffset + 11] =  0.0f;

        rm[rmOffset + 12] =  0.0f;
        rm[rmOffset + 13] =  0.0f;
        rm[rmOffset + 14] =  0.0f;
        rm[rmOffset + 15] =  1.0f;
    }

    public static float[] newRotationMatrix(float degX, float degY, float degZ) {
        float[] rotMat = new float[16];
        Matrix.setIdentityM(rotMat, 0);
        setRotateEuler(rotMat, 0, degX, degY, degZ);
        return rotMat;
    }

    public static float[] newScaleMatrix(float scaleX, float scaleY, float scaleZ) {
        float[] scaleMat = new float[16];
        Matrix.setIdentityM(scaleMat, 0);
        Matrix.scaleM(scaleMat, 0, scaleX, scaleY, scaleZ);
        return scaleMat;
    }

    public static float[] newTransform(float[] position, float[] scale) {
        return newTransform(position, scale, new float[]{0, 0, 0});
    }

    public static float[] newTransform(float[] position) {
        return newTransform(position, new float[]{0, 0, 0});
    }

    public static float[] newTransform() {
        return newTransform(new float[]{0, 0, 0});
    }

    public static float[] newIdentityMatrix(int dim) {
        assert dim > 0;
        float[] mat = new float[dim * dim]; //每个元素已经自动初始化为0
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                mat[i * dim + j] = 1;
            }
        }
        return mat;
    }



    public static float[] vec4(float x,float y,float z,float w){
        return new float[]{x,y,z,w};
    }

    public static float[] vec4(float[] vec3,float w){
        return vec4(vec3[0],vec3[1],vec3[2],w);
    }

    public static float[] vec3(float x,float y,float z){
        return new float[]{x,y,z};
    }

    public static float[] vec3(float[] vec){
        return vec3(vec[0],vec[1],vec[2]);
    }


    public static void setRotateEulerM2(@NonNull float[] rm, int rmOffset,
                                        float x, float y, float z) {
        if (rm == null) {
            throw new IllegalArgumentException("rm == null");
        }
        if (rmOffset < 0) {
            throw new IllegalArgumentException("rmOffset < 0");
        }
        if (rm.length < rmOffset + 16) {
            throw new IllegalArgumentException("rm.length < rmOffset + 16");
        }

        x *= (float) (Math.PI / 180.0f);
        y *= (float) (Math.PI / 180.0f);
        z *= (float) (Math.PI / 180.0f);
        float cx = (float) Math.cos(x);
        float sx = (float) Math.sin(x);
        float cy = (float) Math.cos(y);
        float sy = (float) Math.sin(y);
        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);
        float cxsy = cx * sy;
        float sxsy = sx * sy;

        rm[rmOffset + 0] = cy * cz;
        rm[rmOffset + 1] = -cy * sz;
        rm[rmOffset + 2] = sy;
        rm[rmOffset + 3] = 0.0f;

        rm[rmOffset + 4] = sxsy * cz + cx * sz;
        rm[rmOffset + 5] = -sxsy * sz + cx * cz;
        rm[rmOffset + 6] = -sx * cy;
        rm[rmOffset + 7] = 0.0f;

        rm[rmOffset + 8] = -cxsy * cz + sx * sz;
        rm[rmOffset + 9] = cxsy * sz + sx * cz;
        rm[rmOffset + 10] = cx * cy;
        rm[rmOffset + 11] = 0.0f;

        rm[rmOffset + 12] = 0.0f;
        rm[rmOffset + 13] = 0.0f;
        rm[rmOffset + 14] = 0.0f;
        rm[rmOffset + 15] = 1.0f;
    }

    public static void set4MatrixIdentity(float[] mat) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mat[i * 4 + j] = (i == j) ? 1 : 0;
            }
        }
    }

    public static void scaleMatColumn(float[] mat, int colIdx, int nColumns, float scale) {
        for (int i = colIdx; i < mat.length; i += nColumns) {
            mat[i] *= scale;
        }
    }

    public static void shiftMatColumn(float[] mat, int colIdx, int nColumns, float offset) {
        for (int i = colIdx; i < mat.length; i += nColumns) {
            mat[i] += offset;
        }
    }

    public static void copyMatColumn(float[] src, int srcColIdx, int srcNCols,
                                     float[] dest, int destColIdx, int destNCols) {
        for (int i = destColIdx, j = srcColIdx; i < dest.length; i += destNCols, j += srcNCols) {
            dest[i] = src[j];
        }
    }

    public static void translateMat4(float[] mat4, float[] xyz) {
        for (int i = 0; i < 3; i++) {
            mat4[12 + i] += xyz[i];
        }
    }

    public static void setTranslationMat4(float[] mat4, float[] xyz) {
        for (int i = 0; i < 3; i++) {
            mat4[12 + i] = xyz[i];
        }
    }

    public static float[] add(float[] a, float[] b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = a[i] + b[i];
        }
        return ret;
    }

    public static float[] sub(float[] a, float[] b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = a[i] - b[i];
        }
        return ret;
    }

    public static void scale(float[] a, float s) {
        for (int i = 0; i < a.length; i++) {
            a[i] *= s;
        }
    }

    public static float[] scaled(float[] a, float s) {
        float[] result = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i]*s;
        }
        return result;
    }

    public static float norm(float[] a) {
        float n = 0F;
        for (int i = 0; i < a.length; i++) {
            n += a[i] * a[i];
        }
        return (float) Math.sqrt(n);
    }

    public static void normalize(float[] a) {
        float s = norm(a);
        if (s != 0) {
            scale(a, 1.0F / s);
        }
    }

    public static float[] normalized(float[] a){
        float s = norm(a);
        if (s != 0) {
            return scaled(a, 1.0F / s);
        }
        return a;
    }

    public static float[] cross(float[] a, float[] b) {
        float x = a[1] * b[2] - a[2] * b[1];
        float y = a[2] * b[0] - a[0] * b[2];
        float z = a[0] * b[1] - a[1] * b[0];
        return new float[]{x, y, z};
    }


    @FunctionalInterface
    public interface Operator<T>{
        T apply(T dest,T src);
    }

    @FunctionalInterface
    public interface FloatOperator{
        float apply(float dest,float src);
    }

    public static <T> void arrayForEach(Operator<T> functor,T[] dest, T[] src, int destBegin, int srcBegin, int srcEnd){
        DebugUtils.checkRange(srcBegin,0,src.length);
        DebugUtils.checkRange(srcEnd,0,src.length);
        DebugUtils.checkRange(destBegin,0,dest.length);
        DebugUtils.checkRange(srcEnd - srcBegin,0,Integer.MAX_VALUE);
        DebugUtils.checkRange(destBegin + srcEnd - srcBegin,0,dest.length);
        int destIdx = destBegin;
        int srcIdx = srcBegin;
        while (srcIdx < srcEnd){
            dest[destIdx] = functor.apply(dest[destIdx],src[srcIdx]);
            srcIdx++;
            destIdx++;
        }
    }

    public static <T> void arrayForEach(Operator<T> functor,T[] dest, T[] src, int destBegin, int srcBegin){
        arrayForEach(functor,dest,src,destBegin,srcBegin,src.length);
    }

    public static <T> void arrayForEach(Operator<T> functor,T[] dest, T[] src, int destBegin){
        arrayForEach(functor,dest,src,destBegin,0,src.length);
    }

    public static <T> void arrayForEach(Operator<T> functor,T[] dest, T[] src){
        arrayForEach(functor,dest,src,0,0,src.length);
    }


    public static void arrayForEach(FloatOperator functor,float[] dest, float[] src, int destBegin, int srcBegin, int srcEnd){
        DebugUtils.checkRange(srcBegin,0,src.length);
        DebugUtils.checkRange(srcEnd,0,src.length);
        DebugUtils.checkRange(destBegin,0,dest.length);
        DebugUtils.checkRange(srcEnd - srcBegin,0,Integer.MAX_VALUE);
        DebugUtils.checkRange(destBegin + srcEnd - srcBegin,0,dest.length);
        int destIdx = destBegin;
        int srcIdx = srcBegin;
        while (srcIdx < srcEnd){
            dest[destIdx] = functor.apply(dest[destIdx],src[srcIdx]);
            srcIdx++;
            destIdx++;
        }
    }

    public static void arrayForEach(FloatOperator functor,float[] dest, float[] src, int destBegin, int srcBegin){
        arrayForEach(functor,dest,src,destBegin,srcBegin,src.length);
    }

    public static void arrayForEach(FloatOperator functor,float[] dest, float[] src, int destBegin){
        arrayForEach(functor,dest,src,destBegin,0,src.length);
    }

    public static void arrayForEach(FloatOperator functor,float[] dest, float[] src){
        arrayForEach(functor,dest,src,0,0,src.length);
    }




    public static float[] makeNewCopy(float[] a) {
        float[] ret = new float[a.length];
        System.arraycopy(a, 0, ret, 0, ret.length);
        return ret;
    }

    public static float[] rotateVec3(float[] v, float angleDegree, float[] axis) {
        float[] m = new float[16];
        set4MatrixIdentity(m);
        Matrix.rotateM(m, 0, angleDegree, axis[0], axis[1], axis[2]);
        return matVecMultiply(m, v, 4);
    }

    public static float[] matVecMultiply(float[] m, float[] v, int nRowsInMatrix) {
        int l = v.length;
        float[] res = new float[l];
        for (int i = 0; i < l; i++) {
            res[i] = 0;
            for (int j = 0; j < l; j++) {
                res[i] += m[j * nRowsInMatrix + i] * v[j];
            }
        }
        return res;
    }

    public static float[] vecMultiply(float[] lhsVec, float[] rhsVec) {
        float[] rhs = lhsVec;
        float[] lhs = rhsVec;
        float[] result = new float[lhs.length*rhs.length];
        int rowLen = rhs.length;
        for(int i=0;i<lhs.length;i++){
            for (int j=0;j<rhs.length;j++){
                result[i*rowLen+j] = lhs[i]*rhs[j];
            }
        }
        return result;
    }

    public static float angle(float[] a, float[] b) {
        float d = dot(a, b);
        float na = norm(a);
        float nb = norm(b);
        if ((na != 0) && (nb != 0)) {
            float cosAngle = d / (na * nb);
            return (float) ((Math.acos(cosAngle) - Math.PI / 2) * 180 / Math.PI);
        }
        return 0;
    }

    public static float dot(float[] a, float[] b) {
        float d = 0;
        for (int i = 0; i < a.length; i++) {
            d += a[i] * b[i];
        }
        return d;
    }

    public static float[] orthogonalized(float[] base,float[] target){
        return normalized(sub(target,scaled(base,dot(base,target))));
    }

    public static void orthogonalize(float[] base,float[] target){
        normalize(sub(target,scaled(base,dot(base,target))));
    }

}
