package me.project.cloud2drenderer.opengl.statemanager;

import static android.opengl.GLES30.*;
import android.graphics.Bitmap;
import android.opengl.GLUtils;
import androidx.annotation.NonNull;
import me.project.cloud2drenderer.opengl.GLErrorUtils;
import me.project.cloud2drenderer.opengl.glcomponent.texture.GLTexture;

public class GLTextureManager {

    private static GLTexture currentTexture = null;

    private GLTextureManager(){
    }

    private static void assertBound(){
        assert currentTexture!=null;
    }

    public static void bind(@NonNull GLTexture texture,int unit){
        currentTexture = texture;
        active(unit);
        glBindTexture(texture.type, texture.textureId);
        GLErrorUtils.assertNoError();
    }

    public static void bind(@NonNull GLTexture texture) {
        bind(texture, 0);
    }

    public static void active(int unit){
        glActiveTexture(GL_TEXTURE0 + unit);
    }
    public static void unbind(){
        assertBound();
        glBindTexture(currentTexture.type, GL_NONE);
        currentTexture = null;
    }

    @NonNull
    public static GLTexture genTexture2D(){
        final int[] textureId = new int[1];
        glGenTextures(1, textureId, 0);
        if (textureId[0] == GL_NONE)
        {
            throw new RuntimeException("Error generating texture.");
        }
        GLTexture texture = new GLTexture();
        texture.textureId = textureId[0];
        texture.type = GL_TEXTURE_2D;
        GLErrorUtils.assertNoError();
        return texture;
    }



    //使用前先bind
    public static void loadTexture2D(@NonNull Bitmap bitmap) {
        assertBound();
    //    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
       // glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, )
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        glGenerateMipmap(GL_TEXTURE_2D);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        GLErrorUtils.assertNoError();
    }

    //使用前先bind
    public static void loadTextureCubeMap(@NonNull Bitmap[] bitmaps) {
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        for (int i = 0; i < bitmaps.length; i++) {
            if (bitmaps[i] != null) {
                GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, bitmaps[i], 0);
            }
        }
        GLErrorUtils.assertNoError();
    }
}
