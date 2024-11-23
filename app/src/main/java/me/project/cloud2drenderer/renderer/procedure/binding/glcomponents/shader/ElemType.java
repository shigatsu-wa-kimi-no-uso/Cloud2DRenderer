package me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader;


import static android.opengl.GLES20.*;

public enum ElemType {

    AUTO(-1),
    INT(GL_INT),
    UNSIGNED_INT(GL_UNSIGNED_INT),
    FLOAT(GL_FLOAT),
    SHORT(GL_SHORT),
    UNSIGNED_SHORT(GL_UNSIGNED_SHORT),
    BYTE(GL_BYTE),
    UNSIGNED_BYTE(GL_UNSIGNED_BYTE),
    FIXED(GL_FIXED);

    final int glEnumCode;

    ElemType(int glEnumCode){
        this.glEnumCode = glEnumCode;
    }
}
