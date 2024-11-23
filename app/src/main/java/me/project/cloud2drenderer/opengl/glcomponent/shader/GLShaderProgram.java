package me.project.cloud2drenderer.opengl.glcomponent.shader;


import static android.opengl.GLES30.GL_INVALID_INDEX;

public class GLShaderProgram {
    public int programId = GL_INVALID_INDEX;

    public GLShaderProgram(){

    }

    public GLShaderProgram(int programId){
        this.programId = programId;
    }


}
