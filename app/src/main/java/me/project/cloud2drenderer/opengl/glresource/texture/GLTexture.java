package me.project.cloud2drenderer.opengl.glresource.texture;


import static android.opengl.GLES20.GL_NONE;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_INVALID_INDEX;

public class GLTexture {

    public int textureId = GL_INVALID_INDEX;

    public int type;


    public static GLTexture nullTexture(){
        GLTexture texture = new GLTexture();
        texture.textureId = GL_NONE;
        texture.type = GL_TEXTURE_2D;
        return texture;
    }
}
