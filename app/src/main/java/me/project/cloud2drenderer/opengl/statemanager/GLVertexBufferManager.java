package me.project.cloud2drenderer.opengl.statemanager;

import static android.opengl.GLES30.*;

import androidx.annotation.NonNull;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import me.project.cloud2drenderer.opengl.GLErrorUtils;
import me.project.cloud2drenderer.opengl.glresource.buffer.GLVertexBuffer;

public class GLVertexBufferManager {

    private static GLVertexBuffer currentVertexBuffer;

    private static int currentBoundVBO;

    private static int currentBoundEBO;

    private GLVertexBufferManager(){

    }

    public static void assertBound(){
        assert currentVertexBuffer!=null;
    }

    public static void assertVBOBindingConsistency() {
        assert currentVertexBuffer.vbo == currentBoundVBO;
    }

    public static void assertEBOBindingConsistency() {
        assert currentVertexBuffer.ebo == currentBoundEBO;
    }
    @NonNull
    public static GLVertexBuffer genVertexBuffer(){
        return genVertexBuffer(true);
    }

    public static void bindVertexBuffer(){
        glBindBuffer(GL_ARRAY_BUFFER, currentVertexBuffer.vbo);
        currentBoundVBO = currentVertexBuffer.vbo;
    }

    public static void bindElementBuffer(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,currentVertexBuffer.ebo);
        currentBoundEBO = currentVertexBuffer.ebo;
    }
    @NonNull
    public static GLVertexBuffer genVertexBuffer(boolean useEBO){
        GLVertexBuffer buffer = new GLVertexBuffer();
        int[] tmp = new int[1];
        glGenVertexArrays(1, tmp, 0);
        buffer.vao = tmp[0];
        glGenBuffers(1, tmp, 0);
        buffer.vbo = tmp[0];
        if(useEBO){
            glGenBuffers(1, tmp, 0);
            buffer.ebo = tmp[0];
        }
        GLErrorUtils.assertNoError();
        return buffer;
    }

    public static void freeVertexBuffer(@NonNull GLVertexBuffer buffer){
        int[] tmp = new int[1];
        tmp[0] = buffer.vao;
        glDeleteVertexArrays(1,tmp,0);
        tmp[0] = buffer.vbo;
        glDeleteBuffers(1, tmp,0);
        if(buffer.ebo != GL_NONE){
            tmp[0] = buffer.ebo;
            glDeleteBuffers(1, tmp,0);
        }
        GLErrorUtils.assertNoError();
    }


    public static void loadVertexAttributeData(@NonNull ByteBuffer data, int drawMethod){
        loadVertexAttributeData(data,drawMethod,data.capacity());
    }

    public static void loadVertexAttributeData(@NonNull ByteBuffer data, int drawMethod, int size){
        // FloatBuffer verticesData = verticesDataBytes.asFloatBuffer();
        // verticesData.put(data).position(0);
        assertBound();
       // glBindVertexArray(0);
        bindVertexBuffer();
        glBufferData(GL_ARRAY_BUFFER, size, data, drawMethod);
        GLErrorUtils.assertNoError();
    }

    public static void loadElementIndices(@NonNull ByteBuffer data,int drawMethod,int size){
        assertBound();
     //   bindVertexBuffer();
       // glBindVertexArray(0);
        bindElementBuffer();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, size, data, drawMethod);
        GLErrorUtils.assertNoError();
    }

    public static void loadElementIndices(@NonNull IntBuffer data,int drawMethod,int size){
        assertBound();
        bindElementBuffer();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, size, data, drawMethod);
        GLErrorUtils.assertNoError();
    }

    public static void loadElementIndices(@NonNull IntBuffer data,int drawMethod){
        assertBound();
        bindElementBuffer();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data.capacity()*Integer.BYTES, data, drawMethod);
       // glBindVertexArray(currentVertexBuffer.vao);
        GLErrorUtils.assertNoError();
    }

    public static void loadElementIndices(@NonNull ShortBuffer data,int drawMethod){
        assertBound();
        bindElementBuffer();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data.capacity()*Short.BYTES, data, drawMethod);
        GLErrorUtils.assertNoError();
    }

    public static void loadElementIndices(@NonNull short[] data, int drawMethod){
        ByteBuffer elemIndicesBytes = ByteBuffer.allocateDirect(data.length*Short.BYTES).order(ByteOrder.nativeOrder());
        ShortBuffer dataInShorts = elemIndicesBytes.asShortBuffer();
        dataInShorts.put(data).position(0);
        loadElementIndices(dataInShorts,drawMethod);
    }

    public static void loadElementIndices(@NonNull int[] data,int drawMethod){
        ByteBuffer elemIndicesBytes = ByteBuffer.allocateDirect(data.length*Integer.BYTES).order(ByteOrder.nativeOrder());
        IntBuffer dataInInts = elemIndicesBytes.asIntBuffer();
        dataInInts.put(data).position(0);
        loadElementIndices(dataInInts,drawMethod);
    }


    public static void loadElementIndices(@NonNull byte[] data,int drawMethod,int size){
        ByteBuffer elemIndicesBytes = ByteBuffer.allocateDirect(data.length).order(ByteOrder.nativeOrder());
        loadElementIndices(elemIndicesBytes,drawMethod,size);
    }

    public static void loadVertexAttributeData(@NonNull FloatBuffer data,int drawMethod){
        assertBound();
   //     glBindVertexArray(0);
        bindVertexBuffer();
        glBufferData(GL_ARRAY_BUFFER,
                data.capacity() * Float.BYTES,
                data,
                drawMethod);
        GLErrorUtils.assertNoError();
    }
    public static void loadVertexAttributeData(@NonNull float[] data,int drawMethod){
        ByteBuffer dataInBytes = ByteBuffer.allocateDirect(data.length* Float.BYTES)
                .order(ByteOrder.nativeOrder());
        FloatBuffer dataInFloats = dataInBytes.asFloatBuffer();
        dataInFloats.put(data).position(0);
        loadVertexAttributeData(dataInFloats,drawMethod);
    }


    public static void loadVertexAttributeData(@NonNull byte[] data, int drawMethod){
        ByteBuffer verticesAttributesBytes = ByteBuffer.allocateDirect(data.length).order(ByteOrder.nativeOrder());

       /* FloatBuffer verticesData = verticesDataBytes.asFloatBuffer();
          verticesData.put(data).position(0);*/
        loadVertexAttributeData(verticesAttributesBytes,drawMethod,data.length);
    }

    public static void setAttributePointer(int index, int elemCount,int elemType,boolean normalized, int strideInBytes, int startOffset){
        assertBound();
        assertVBOBindingConsistency();
        glVertexAttribPointer(index, elemCount, elemType, normalized,
                strideInBytes,
                startOffset);
        glEnableVertexAttribArray(index);
        GLErrorUtils.assertNoError();
    }

    public static void bind(@NonNull GLVertexBuffer buffer){
        glBindVertexArray(buffer.vao);
        currentVertexBuffer = buffer;
    }

    public static void unbind(){
        assertBound();
        glBindVertexArray(GL_NONE);
        glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_NONE);
        currentVertexBuffer = null;
    }
}
