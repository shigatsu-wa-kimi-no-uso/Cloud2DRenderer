package me.project.cloud2drenderer.renderer.controller;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.project.cloud2drenderer.opengl.statemanager.GLTextureManager;
import me.project.cloud2drenderer.opengl.glresource.texture.GLTexture;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;

public class TextureController {

    private final Map<String, Texture> textures;

    public TextureController(){
        textures=new HashMap<>();
    }

    public Texture createTexture2D(String name, @NonNull Bitmap bitmap) {
        return textures.compute(name,
                (k, v) -> Objects.requireNonNullElseGet(v, () -> {
                            GLTexture texture = GLTextureManager.genTexture2D();
                            GLTextureManager.bind(texture);
                            GLTextureManager.loadTexture2D(bitmap);
                            GLTextureManager.unbind();
                            Texture texture1 = new Texture();
                            texture1.texture = texture;
                            texture1.unit = 0;
                            return texture1;
                        }
                )
        );
    }

    public Texture getTexture(String name){
        if(name == null || name.isEmpty()){
            return null;
        }
        Texture texture = textures.get(name);
        assert texture!=null;
        return texture;
    }

    public void bindTexture(String name){
        Texture texture = textures.get(name);
        assert texture != null;
        bindTexture(texture.texture);
    }

    public void bindTexture(GLTexture texture){
        assert texture != null;
        GLTextureManager.bind(texture);
    }

}
