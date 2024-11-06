package me.project.cloud2drenderer.opengl;

import static android.opengl.GLES30.*;

public class GLErrorUtils {
    public static void assertNoError(){
        assert glGetError() == GL_NO_ERROR;
    }

    public static void checkShaderCompileErrors(int shaderId)  {
        final int[] compileStatus = new int[1]; //int引用
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0); //检查编译错误
        if (compileStatus[0] == GL_FALSE) {
            String str = glGetShaderInfoLog(shaderId);
            glDeleteShader(shaderId);
            throw new RuntimeException(str);
        }
    }
    public static void checkShaderLinkErrors(int programId)  {
        int[] success = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, success, 0); //检查链接错误
        if(success[0] == GL_FALSE){
            String str = glGetProgramInfoLog(programId);
            throw new RuntimeException(str);
        }
    }

}
