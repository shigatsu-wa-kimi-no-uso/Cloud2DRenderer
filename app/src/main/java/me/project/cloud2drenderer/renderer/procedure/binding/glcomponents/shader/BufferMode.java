package me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader;


import static android.opengl.GLES30.GL_INTERLEAVED_ATTRIBS;
import static android.opengl.GLES30.GL_SEPARATE_ATTRIBS;

public enum BufferMode {


    INTERLEAVED(GL_INTERLEAVED_ATTRIBS),
    SEPARATE(GL_SEPARATE_ATTRIBS);

    final int glEnumCode;
    
    BufferMode(int glEnumCode){
        this.glEnumCode = glEnumCode;
    }
}
