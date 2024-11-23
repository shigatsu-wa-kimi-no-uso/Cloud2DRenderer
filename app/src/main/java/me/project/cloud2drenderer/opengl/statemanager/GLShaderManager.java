package me.project.cloud2drenderer.opengl.statemanager;

import static android.opengl.GLES32.*;

import android.util.Log;

import androidx.annotation.NonNull;


import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

import me.project.cloud2drenderer.opengl.GLErrorUtils;
import me.project.cloud2drenderer.opengl.glcomponent.shader.ShaderCode;
import me.project.cloud2drenderer.opengl.glcomponent.shader.GLShaderProgram;

public class GLShaderManager {

    private static GLShaderProgram currentProgram = null;

    private static String tag = GLShaderManager.class.getSimpleName();

/*
    private interface SetUniformFunctor{
        void set(String name,float[] buf,int col,int row,boolean transpose);
    }
    private interface SetUniformMatrixFunctor {
        void setUniformMatrix(String name,@NonNull float[] val,boolean transpose);
    }

    private interface SetUniformIVectorFunctor {
        void setUniformIVector(String name,int[] val);
    }

    private interface SetUniformVectorFunctor {
        void setUniformVector(String name,float[] val);
    }
    private static final SetUniformMatrixFunctor[][] setUniformMatrixFunctors = new SetUniformMatrixFunctor[5][5];
    private static final SetUniformIVectorFunctor[] setUniformIVectorFunctors = new SetUniformIVectorFunctor[5];
    private static final SetUniformVectorFunctor[] setUniformVectorFunctors = new SetUniformVectorFunctor[5];
*/


    private static final Set<Integer> floatTypeSet = new HashSet<>();
    private static final Set<Integer> intTypeSet = new HashSet<>();



    static {
        int[] floatTypes = new int[]{
                GL_FLOAT,GL_FLOAT_VEC2,GL_FLOAT_VEC3,GL_FLOAT_VEC4, GL_FLOAT_MAT2,
                GL_FLOAT_MAT3, GL_FLOAT_MAT4, GL_FLOAT_MAT2x3, GL_FLOAT_MAT2x4,
                GL_FLOAT_MAT3x2, GL_FLOAT_MAT3x4, GL_FLOAT_MAT4x2, GL_FLOAT_MAT4x3
        };
        int[] intTypes = new int[]{
                GL_INT, GL_INT_VEC2, GL_INT_VEC3, GL_INT_VEC4, GL_UNSIGNED_INT,
                GL_UNSIGNED_INT_VEC2, GL_UNSIGNED_INT_VEC3, GL_UNSIGNED_INT_VEC4,
                GL_BOOL, GL_BOOL_VEC2, GL_BOOL_VEC3, GL_BOOL_VEC4, GL_SAMPLER_2D,
                GL_SAMPLER_3D, GL_SAMPLER_CUBE, GL_SAMPLER_2D_SHADOW, GL_SAMPLER_2D_ARRAY,
                GL_SAMPLER_2D_ARRAY_SHADOW, GL_SAMPLER_2D_MULTISAMPLE, GL_SAMPLER_2D_MULTISAMPLE_ARRAY,
                GL_SAMPLER_CUBE_SHADOW, GL_SAMPLER_CUBE_MAP_ARRAY, GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW,
                GL_SAMPLER_BUFFER, GL_INT_SAMPLER_2D, GL_INT_SAMPLER_3D, GL_INT_SAMPLER_CUBE,
                GL_INT_SAMPLER_2D_ARRAY, GL_INT_SAMPLER_2D_MULTISAMPLE, GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY,
                GL_INT_SAMPLER_CUBE_MAP_ARRAY, GL_INT_SAMPLER_BUFFER, GL_UNSIGNED_INT_SAMPLER_2D,
                GL_UNSIGNED_INT_SAMPLER_3D, GL_UNSIGNED_INT_SAMPLER_CUBE, GL_UNSIGNED_INT_SAMPLER_2D_ARRAY,
                GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE, GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY,
                GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY, GL_UNSIGNED_INT_SAMPLER_BUFFER, GL_IMAGE_2D,
                GL_IMAGE_3D, GL_IMAGE_CUBE, GL_IMAGE_2D_ARRAY, GL_IMAGE_CUBE_MAP_ARRAY, GL_IMAGE_BUFFER,
                GL_INT_IMAGE_2D, GL_INT_IMAGE_3D, GL_INT_IMAGE_CUBE, GL_INT_IMAGE_2D_ARRAY, GL_INT_IMAGE_CUBE_MAP_ARRAY,
                GL_INT_IMAGE_BUFFER, GL_UNSIGNED_INT_IMAGE_2D, GL_UNSIGNED_INT_IMAGE_3D, GL_UNSIGNED_INT_IMAGE_CUBE,
                GL_UNSIGNED_INT_IMAGE_2D_ARRAY, GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY, GL_UNSIGNED_INT_IMAGE_BUFFER,
                GL_UNSIGNED_INT_ATOMIC_COUNTER
        };
        for(int type:floatTypes){
            floatTypeSet.add(type);
        }
        for(int type:intTypes){
            intTypeSet.add(type);
        }
    }
/*
    static {
        setUniformVectorFunctors[2] = GLShaderManager::setUniformVector2;
        setUniformVectorFunctors[3] = GLShaderManager::setUniformVector3;
        setUniformVectorFunctors[4] = GLShaderManager::setUniformVector4;
        setUniformIVectorFunctors[2] = GLShaderManager::setUniformVector2;
        setUniformIVectorFunctors[3] = GLShaderManager::setUniformVector3;
        setUniformIVectorFunctors[4] = GLShaderManager::setUniformVector4;
        setUniformMatrixFunctors[2][2] = GLShaderManager::setUniformMatrix2f;
        setUniformMatrixFunctors[2][3] = GLShaderManager::setUniformMatrix2x3f;
        setUniformMatrixFunctors[2][4] = GLShaderManager::setUniformMatrix2x4f;
        setUniformMatrixFunctors[3][2] = GLShaderManager::setUniformMatrix3x2f;
        setUniformMatrixFunctors[3][3] = GLShaderManager::setUniformMatrix3f;
        setUniformMatrixFunctors[3][4] = GLShaderManager::setUniformMatrix3x4f;
        setUniformMatrixFunctors[4][2] = GLShaderManager::setUniformMatrix4x2f;
        setUniformMatrixFunctors[4][3] = GLShaderManager::setUniformMatrix4x3f;
        setUniformMatrixFunctors[4][4] = GLShaderManager::setUniformMatrix4f;
    }*/

    private GLShaderManager(){
    }

    private static void assertBound(){
        assert currentProgram!=null;
    }


    public static boolean typeIsFloatBased(int type){
        return floatTypeSet.contains(type);
    }

    public static boolean typeIsIntBased(int type){
        return intTypeSet.contains(type);
    }


    public static int getUniformLocation(String name){
        assertBound();
        return glGetUniformLocation(currentProgram.programId,name);
    }
    public static void getShaderUniformInfo(String[][] names,int[][] types,int[][] sizes){
        assertBound();
        int[] buf = new int[1];
        int programId = currentProgram.programId;
        glGetProgramiv(programId, GL_ACTIVE_UNIFORMS, buf,0);
        int count = buf[0];
        names[0] = new String[count];
        types[0] = new int[count];
        sizes[0] = new int[count];
        for(int i = 0; i < count; i++){
            int[] type = new int[1];
            int[] size = new int[1];
            names[0][i]  = glGetActiveUniform(programId,i,size,0,type,0);
            types[0][i] = type[0];
            sizes[0][i] = size[0];
        }
    }

    public static int compileShader(@NonNull ShaderCode shaderCode) {
        return compileShader(shaderCode.code,shaderCode.type);

    }

    @NonNull
    public static int[] compileShaders(@NonNull ShaderCode[] shaderCodes) {
        int[] shaderIds = new int[shaderCodes.length];
        for (int i = 0; i < shaderCodes.length; i++) {
            shaderIds[i] = compileShader(shaderCodes[i].code, shaderCodes[i].type);
        }
        return shaderIds;
    }

    public static int compileShader(String code, int type) {
        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, code);
        glCompileShader(shaderId);
        GLErrorUtils.checkShaderCompileErrors(shaderId);
        GLErrorUtils.assertNoError();
        return shaderId;
    }

    @NonNull
    @Contract("_, _ -> new")
    public static GLShaderProgram createProgram(int vertShaderId, int fragShaderId) {
        int programId = glCreateProgram();
        glAttachShader(programId, vertShaderId);
        glAttachShader(programId, fragShaderId);
        glLinkProgram(programId);
        GLErrorUtils.checkShaderLinkErrors(programId);
        GLErrorUtils.assertNoError();
        return new GLShaderProgram(programId);
    }

    @NonNull
    public static GLShaderProgram[] createPrograms(@NonNull int[] vertShaderIds, @NonNull int[] fragShaderIds) {
        GLShaderProgram[] shaderPrograms = new GLShaderProgram[vertShaderIds.length];
        for (int i = 0; i < vertShaderIds.length; i++) {
            shaderPrograms[i] = createProgram(vertShaderIds[i],fragShaderIds[i]);
        }
        return shaderPrograms;
    }

    @NonNull
    public static GLShaderProgram createProgram(String vertShaderCode, String fragShaderCode)  {
        int vertShaderId = compileShader(vertShaderCode,GL_VERTEX_SHADER);
        int fragShaderId = compileShader(fragShaderCode,GL_FRAGMENT_SHADER);
        return createProgram(vertShaderId,fragShaderId);
    }

    public static void use(@NonNull GLShaderProgram program) {
        glUseProgram(program.programId);
        currentProgram = program;
    }

    private static int getAttribLocation(String name){
        return glGetAttribLocation(currentProgram.programId, name);
    }

    public static void setAttributeSequential(String name, int length,int type,boolean normalized, int strideInBytes, int offset){
        assertBound();
        final int index = getAttribLocation(name);
        glVertexAttribPointer(index, length, type, normalized,
                strideInBytes,
                offset);
        glEnableVertexAttribArray(index);
        GLErrorUtils.assertNoError();
    }


    public static void setAttribute(String name, int elemCount,int elemType,boolean normalized, int strideInBytes, int startOffset){
        assertBound();
        GLVertexBufferManager.assertBound();
        GLVertexBufferManager.assertVBOBindingConsistency();
          //注意！设置顶点属性时，shader和vertexbuffer都要绑定！若vertexbuffer不绑定，GL不会报错！
        final int index = getAttribLocation(name);
        if(index == GL_INVALID_INDEX){
            Log.e(tag,"getAttribLocation('"+name+"') returned -1");
            return;
        }

        glVertexAttribPointer(index, elemCount, elemType, normalized,
                strideInBytes,
                startOffset);
        glEnableVertexAttribArray(index);
        GLErrorUtils.assertNoError();
        Log.d(tag,"setAttribLocation('"+name+"') success");
    }


    // uniform setters: scalar
    public static void setUniformScalar(int location,int val){
        assertBound();
        glUniform1i(location,val);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformScalar(int location,float val){
        assertBound();
        glUniform1f(location,val);
        GLErrorUtils.assertNoError();
    }


    public static void setUniformScalar(String name,int val){
        setUniformScalar(getUniformLocation(name),val);
    }
   
    public static void setUniformScalar(String name,float val){
        setUniformScalar(getUniformLocation(name),val);
    }

    // uniform setters: scalar array
    public static void setUniformScalarArray(int location,int[] vals,int count,int offset){
        assertBound();
        glUniform1iv(location,count,vals,offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformScalarArray(int location,float[] vals,int count,int offset){
        assertBound();
        glUniform1fv(location,count,vals,offset);
        GLErrorUtils.assertNoError();
    }
    
    public static void setUniformScalarArray(int location,int[] vals){
        setUniformScalarArray(location,vals,vals.length,0);
    }
    public static void setUniformScalarArray(String name,int[] vals,int count,int offset){
        setUniformScalarArray(getUniformLocation(name),vals,count,offset);
    }
    
    public static void setUniformScalarArray(String name,int[] vals){
        setUniformScalarArray(name,vals,vals.length,0);
    }
    

    public static void setUniformScalarArray(int location,float[] vals){
        setUniformScalarArray(location,vals,vals.length,0);
    }
    public static void setUniformScalarArray(String name,float[] vals,int count,int offset){
        setUniformScalarArray(getUniformLocation(name),vals,count,offset);
    }

    public static void setUniformScalarArray(String name, float[] vals){
        setUniformScalarArray(name,vals,vals.length,0);
    }

    // uniform setters: vector2

    public static void setUniformVector2(int location, int x,int y){
        assertBound();
        glUniform2i(location,x,y);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector2(int location, float x,float y){
        assertBound();
        glUniform2f(location,x,y);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector2(String name, int x,int y){
        setUniformVector2(getUniformLocation(name),x,y);
    }
    public static void setUniformVector2(int location, @NonNull int[] val){
        setUniformVector2(location,val[0],val[1]);
    }
    
    public static void setUniformVector2(String name, @NonNull int[] val){
        setUniformVector2(getUniformLocation(name),val);
    }

    public static void setUniformVector2(String name, float x,float y){
        setUniformVector2(getUniformLocation(name),x,y);
    }
    public static void setUniformVector2(int location, @NonNull float[] val){
        setUniformVector2(location,val[0],val[1]);
    }

    public static void setUniformVector2(String name, @NonNull float[] val){
        setUniformVector2(getUniformLocation(name),val);
    }


    // uniform setters: vector3

    public static void setUniformVector3(int location, int x,int y,int z){
        assertBound();
        glUniform3i(location,x,y,z);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector3(int location, float x,float y,float z){
        assertBound();
        glUniform3f(location,x,y,z);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector3(String name, int x,int y,int z){
        setUniformVector3(getUniformLocation(name),x,y,z);
    }
    public static void setUniformVector3(int location, @NonNull int[] val){
        setUniformVector3(location,val[0],val[1],val[2]);
    }

    public static void setUniformVector3(String name, @NonNull int[] val){
        setUniformVector3(getUniformLocation(name),val);
    }

    public static void setUniformVector3(String name, float x,float y,float z){
        setUniformVector3(getUniformLocation(name),x,y,z);
    }
    public static void setUniformVector3(int location, @NonNull float[] val){
        setUniformVector3(location,val[0],val[1],val[2]);
    }

    public static void setUniformVector3(String name, @NonNull float[] val){
        setUniformVector3(getUniformLocation(name),val);
    }



    // uniform setters: vector4
    public static void setUniformVector4(int location, int x,int y,int z,int w){
        assertBound();
        glUniform4i(location,x,y,z,w);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector4(int location, float x,float y,float z,float w){
        assertBound();
        glUniform4f(location,x,y,z,w);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector4(String name, int x,int y,int z,int w){
        setUniformVector4(getUniformLocation(name),x,y,z,w);
    }
    public static void setUniformVector4(int location, @NonNull int[] val){
        setUniformVector4(location,val[0],val[1],val[2],val[3]);
    }

    public static void setUniformVector4(String name, @NonNull int[] val){
        setUniformVector4(getUniformLocation(name),val);
    }

    public static void setUniformVector4(String name, float x,float y,float z,float w){
        setUniformVector4(getUniformLocation(name),x,y,z,w);
    }
    public static void setUniformVector4(int location, @NonNull float[] val){
        setUniformVector4(location,val[0],val[1],val[2],val[3]);
    }

    public static void setUniformVector4(String name, @NonNull float[] val){
        setUniformVector4(getUniformLocation(name),val);
    }


    // uniform setters: vector2 array

    public static void setUniformVector2(int location, @NonNull int[] vals,int count,int offset){
        assertBound();
        glUniform2iv(location,count,vals,offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector2(int location, @NonNull float[] vals,int count,int offset){
        assertBound();
        glUniform2fv(location,count,vals,offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector2(String name, @NonNull int[] vals,int count,int offset){
       setUniformVector2(getUniformLocation(name),vals,count,offset);
    }

    public static void setUniformVector2(String name, @NonNull float[] vals,int count,int offset){
        setUniformVector2(getUniformLocation(name),vals,count,offset);
    }





    // uniform setters: vector3 array

    public static void setUniformVector3(int location, @NonNull int[] vals,int count,int offset){
        assertBound();
        glUniform3iv(location,count,vals,offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector3(int location, @NonNull float[] vals,int count,int offset){
        assertBound();
        glUniform3fv(location,count,vals,offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector3(String name, @NonNull int[] vals,int count,int offset){
        setUniformVector3(getUniformLocation(name),vals,count,offset);
    }

    public static void setUniformVector3(String name, @NonNull float[] vals,int count,int offset){
        setUniformVector3(getUniformLocation(name),vals,count,offset);
    }





    // uniform setters: vector4 array

    public static void setUniformVector4(int location, @NonNull int[] vals,int count,int offset){
        assertBound();
        glUniform4iv(location,count,vals,offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector4(int location, @NonNull float[] vals,int count,int offset){
        assertBound();
        glUniform4fv(location,count,vals,offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformVector4(String name, @NonNull int[] vals,int count,int offset){
        setUniformVector4(getUniformLocation(name),vals,count,offset);
    }

    public static void setUniformVector4(String name, @NonNull float[] vals,int count,int offset){
        setUniformVector4(getUniformLocation(name),vals,count,offset);
    }



    // uniform setters: matrix2 array

    public static void setUniformMatrix2f(int location,@NonNull float[] vals,int count,int offset,boolean transpose){
        assertBound();
        glUniformMatrix2fv(location, count,  transpose, vals, offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformMatrix2f(String name,@NonNull float[] vals,int count,int offset,boolean transpose){
        setUniformMatrix2f(getUniformLocation(name),vals,count,offset,transpose);
    }





    // uniform setters: matrix2x3 array

    public static void setUniformMatrix2x3f(int location,@NonNull float[] vals,int count,int offset,boolean transpose){
        assertBound();
        glUniformMatrix2x3fv(location, count,  transpose, vals, offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformMatrix2x3f(String name,@NonNull float[] vals,int count,int offset,boolean transpose){
        setUniformMatrix2x3f(getUniformLocation(name),vals,count,offset,transpose);
    }




    // uniform setters: matrix2x4 array

    public static void setUniformMatrix2x4f(int location,@NonNull float[] vals,int count,int offset,boolean transpose){
        assertBound();
        glUniformMatrix2x4fv(location, count,  transpose, vals, offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformMatrix2x4f(String name,@NonNull float[] vals,int count,int offset,boolean transpose){
        setUniformMatrix2x4f(getUniformLocation(name),vals,count,offset,transpose);
    }





    // uniform setters: matrix3x2 array

    public static void setUniformMatrix3x2f(int location,@NonNull float[] vals,int count,int offset,boolean transpose){
        assertBound();
        glUniformMatrix3x2fv(location, count,  transpose, vals, offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformMatrix3x2f(String name,@NonNull float[] vals,int count,int offset,boolean transpose){
        setUniformMatrix3x2f(getUniformLocation(name),vals,count,offset,transpose);
    }
    
    

    // uniform setters: matrix3 array

    public static void setUniformMatrix3f(int location,@NonNull float[] vals,int count,int offset,boolean transpose){
        assertBound();
        glUniformMatrix3fv(location, count,  transpose, vals, offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformMatrix3f(String name,@NonNull float[] vals,int count,int offset,boolean transpose){
        setUniformMatrix3f(getUniformLocation(name),vals,count,offset,transpose);
    }




    // uniform setters: matrix3x4 array

    public static void setUniformMatrix3x4f(int location,@NonNull float[] vals,int count,int offset,boolean transpose){
        assertBound();
        glUniformMatrix3x4fv(location, count,  transpose, vals, offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformMatrix3x4f(String name,@NonNull float[] vals,int count,int offset,boolean transpose){
        setUniformMatrix3x4f(getUniformLocation(name),vals,count,offset,transpose);
    }





    // uniform setters: matrix4x2 array

    public static void setUniformMatrix4x2f(int location,@NonNull float[] vals,int count,int offset,boolean transpose){
        assertBound();
        glUniformMatrix4x2fv(location, count,  transpose, vals, offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformMatrix4x2f(String name,@NonNull float[] vals,int count,int offset,boolean transpose){
        setUniformMatrix4x2f(getUniformLocation(name),vals,count,offset,transpose);
    }




    // uniform setters: matrix4x3 array

    public static void setUniformMatrix4x3f(int location,@NonNull float[] vals,int count,int offset,boolean transpose){
        assertBound();
        glUniformMatrix4x3fv(location, count,  transpose, vals, offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformMatrix4x3f(String name,@NonNull float[] vals,int count,int offset,boolean transpose){
        setUniformMatrix4x3f(getUniformLocation(name),vals,count,offset,transpose);
    }





    // uniform setters: matrix4 array

    public static void setUniformMatrix4f(int location,@NonNull float[] vals,int count,int offset,boolean transpose){
        assertBound();
        glUniformMatrix4fv(location, count,  transpose, vals, offset);
        GLErrorUtils.assertNoError();
    }

    public static void setUniformMatrix4f(String name,@NonNull float[] vals,int count,int offset,boolean transpose){
        setUniformMatrix4f(getUniformLocation(name),vals,count,offset,transpose);
    }
    



}
